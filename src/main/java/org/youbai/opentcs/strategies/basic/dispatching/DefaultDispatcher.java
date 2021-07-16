/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.strategies.basic.dispatching;

import static com.google.common.base.Preconditions.checkState;
import static java.util.Objects.requireNonNull;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;

import io.quarkus.runtime.Startup;
import org.youbai.opentcs.components.kernel.Dispatcher;
import org.youbai.opentcs.components.kernel.services.InternalTransportOrderService;
import org.youbai.opentcs.components.kernel.services.InternalVehicleService;
import org.youbai.opentcs.customizations.ApplicationEventBus;
import org.youbai.opentcs.customizations.kernel.KernelExecutor;
import org.youbai.opentcs.data.model.Vehicle;
import org.youbai.opentcs.data.order.TransportOrder;
import org.youbai.opentcs.kernel.annotations.SimpleEventBusAnnotation;
import org.youbai.opentcs.util.event.EventSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Dispatches transport orders and vehicles.
 * 派遣运送订单给车辆
 * @author Stefan Walter (Fraunhofer IML)
 */
@ApplicationScoped
public class DefaultDispatcher
    implements Dispatcher {

  /**
   * This class's Logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(DefaultDispatcher.class);
  /**
   * Stores reservations of transport orders for vehicles.
   */
  @Inject
  OrderReservationPool orderReservationPool;
  /**
   * Provides services/utility methods for working with transport orders.
   */
  @Inject
  TransportOrderUtil transportOrderUtil;
  /**
   * The transport order service.
   */
  @Inject
  InternalTransportOrderService transportOrderService;
  /**
   * The vehicle service.
   */
  @Inject
  InternalVehicleService vehicleService;
  /**
   * Where we register for application events.
   */
  @Inject@SimpleEventBusAnnotation
  EventSource eventSource;
  /**
   * The kernel's executor.
   */
  @Inject@Named("ExecutorService")
  ScheduledExecutorService kernelExecutor;

  @Inject
  FullDispatchTask fullDispatchTask;

  @Inject
  PeriodicVehicleRedispatchingTask periodicDispatchTaskProvider;

  private final DefaultDispatcherConfiguration configuration;

  @Inject
  RerouteUtil rerouteUtil;
  /**
   *
   */
  private ImplicitDispatchTrigger implicitDispatchTrigger;

  private ScheduledFuture<?> periodicDispatchTaskFuture;
  /**
   * Indicates whether this component is enabled.
   */
  private boolean initialized;

  /**
   * Creates a new instance.
   *
   */

  public DefaultDispatcher(DefaultDispatcherConfiguration configuration) {
    this.configuration = requireNonNull(configuration, "configuration");
  }

  @Override
  public void initialize() {
    if (isInitialized()) {
      return;
    }

    LOG.debug("Initializing...");

    transportOrderUtil.initialize();
    orderReservationPool.clear();

    fullDispatchTask.initialize();

    implicitDispatchTrigger = new ImplicitDispatchTrigger(this);
    eventSource.subscribe(implicitDispatchTrigger);

    LOG.debug("Scheduling periodic dispatch task with interval of {} ms...",
              configuration.idleVehicleRedispatchingInterval());
    periodicDispatchTaskFuture = kernelExecutor.scheduleAtFixedRate(
        periodicDispatchTaskProvider,
        configuration.idleVehicleRedispatchingInterval(),
        configuration.idleVehicleRedispatchingInterval(),
        TimeUnit.MILLISECONDS
    );

    initialized = true;
  }

  @Override
  public void terminate() {
    if (!isInitialized()) {
      return;
    }

    LOG.debug("Terminating...");

    periodicDispatchTaskFuture.cancel(false);
    periodicDispatchTaskFuture = null;

    eventSource.unsubscribe(implicitDispatchTrigger);
    implicitDispatchTrigger = null;

    fullDispatchTask.terminate();

    initialized = false;
  }

  @Override
  public boolean isInitialized() {
    return initialized;
  }

  @Override
  public void dispatch() {
    LOG.debug("Scheduling dispatch task...");
    // Schedule this to be executed by the kernel executor.
    kernelExecutor.submit(fullDispatchTask);
  }

  @Override
  public void withdrawOrder(TransportOrder order, boolean immediateAbort) {
    requireNonNull(order, "order");
    checkState(isInitialized(), "Not initialized");

    // Schedule this to be executed by the kernel executor.
    kernelExecutor.submit(() -> {
      LOG.debug("Scheduling withdrawal for transport order '{}' (immediate={})...",
                order.getName(),
                immediateAbort);
      transportOrderUtil.abortOrder(order, immediateAbort, false);
    });
  }

  @Override
  public void withdrawOrder(Vehicle vehicle, boolean immediateAbort) {
    requireNonNull(vehicle, "vehicle");
    checkState(isInitialized(), "Not initialized");

    // Schedule this to be executed by the kernel executor.
    kernelExecutor.submit(() -> {
      LOG.debug("Scheduling withdrawal for vehicle '{}' (immediate={})...",
                vehicle.getName(),
                immediateAbort);
      transportOrderUtil.abortOrder(vehicle, immediateAbort, false, false);
    });
  }

  @Override
  public void topologyChanged() {
    if (configuration.rerouteTrigger() == DefaultDispatcherConfiguration.RerouteTrigger.TOPOLOGY_CHANGE) {
      LOG.debug("Scheduling reroute task...");
      kernelExecutor.submit(() -> {
        LOG.debug("Rerouting vehicles due to topology change...");
        rerouteUtil.reroute(vehicleService.fetchObjects(Vehicle.class));
      });
    }
  }

  private static boolean vehicleDispatchable(Vehicle vehicle) {
    requireNonNull(vehicle, "vehicle");

    if (vehicle.getCurrentPosition() == null) {
      LOG.debug("Vehicle '{}' unknown position -> not dispatchable", vehicle.getName());
      return false;
    }
    if (vehicle.getIntegrationLevel() != Vehicle.IntegrationLevel.TO_BE_UTILIZED) {
      LOG.debug("Vehicle '{}' is not to be utilized.", vehicle.getName());
      return false;
    }
    // ProcState IDLE, State CHARGING and energy level not high enough? Then let
    // it charge a bit longer.
    if (vehicle.hasProcState(Vehicle.ProcState.IDLE)
        && vehicle.hasState(Vehicle.State.CHARGING)
        && vehicle.isEnergyLevelCritical()) {
      LOG.debug("Vehicle '{}' is CHARGING, energy level {}<={} -> not (yet) dispatchable.",
                vehicle.getName(),
                vehicle.getEnergyLevel(),
                vehicle.getEnergyLevelCritical());
      return false;
    }
    // Only dispatch vehicles that are either not processing any order at all or
    // are waiting for the next drive order.
    if (!vehicle.hasProcState(Vehicle.ProcState.IDLE)
        && !vehicle.hasProcState(Vehicle.ProcState.AWAITING_ORDER)) {
      LOG.debug("Vehicle '{}' is in processing state {} -> not dispatchable",
                vehicle.getName(),
                vehicle.getProcState());
      return false;
    }
    return true;
  }

}
