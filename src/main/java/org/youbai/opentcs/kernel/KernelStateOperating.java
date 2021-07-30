/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.kernel;

import com.google.common.util.concurrent.Uninterruptibles;
import static java.util.Objects.requireNonNull;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnull;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.youbai.opentcs.access.Kernel;
import org.youbai.opentcs.components.kernel.Dispatcher;
import org.youbai.opentcs.components.kernel.KernelExtension;
import org.youbai.opentcs.components.kernel.PeripheralJobDispatcher;
import org.youbai.opentcs.components.kernel.Router;
import org.youbai.opentcs.components.kernel.Scheduler;
import org.youbai.opentcs.components.kernel.services.InternalVehicleService;
import org.youbai.opentcs.data.model.Vehicle;
import org.youbai.opentcs.kernel.annotations.ScheduledExecutorServiceAnnotations;
import org.youbai.opentcs.kernel.annotations.StandardVehicleServiceAnnotations;
import org.youbai.opentcs.kernel.annotations.XMLFileModelAnnotations;
import org.youbai.opentcs.kernel.extensions.controlcenter.vehicles.AttachmentManager;
import org.youbai.opentcs.kernel.peripherals.LocalPeripheralControllerPool;
import org.youbai.opentcs.kernel.peripherals.PeripheralAttachmentManager;
import org.youbai.opentcs.kernel.persistence.ModelPersister;
import org.youbai.opentcs.kernel.vehicles.LocalVehicleControllerPool;
import org.youbai.opentcs.kernel.workingset.Model;
import org.youbai.opentcs.kernel.workingset.PeripheralJobPool;
import org.youbai.opentcs.kernel.workingset.TCSObjectPool;
import org.youbai.opentcs.kernel.workingset.TransportOrderPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements the standard openTCS kernel in normal operation.
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
@Singleton
public class KernelStateOperating
    extends KernelStateOnline {

  /**
   * This class's Logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(KernelStateOperating.class);
  /**
   * The kernel application's configuration.
   */
  private final KernelApplicationConfiguration configuration;
  /**
   * The order facade to the object pool.
   */
  @Inject@Nonnull
  TransportOrderPool orderPool;
  /**
   * The peripheral job facade to the object pool.
   */
  @Inject@Nonnull
  PeripheralJobPool jobPool;
  /**
   * This kernel's router.
   */
  @Inject@Nonnull
  Router router;
  /**
   * This kernel's scheduler.
   */
  @Inject@Nonnull@Named("bindScheduler")
  Scheduler scheduler;
  /**
   * This kernel's dispatcher.
   */
  @Inject@Nonnull
  Dispatcher dispatcher;
  /**
   * This kernel's peripheral job dispatcher.
   */
  @Inject@Nonnull
  PeripheralJobDispatcher peripheralJobDispatcher;
  /**
   * A pool of vehicle controllers.
   */
  @Inject@Nonnull
  LocalVehicleControllerPool vehicleControllerPool;
  /**
   * A pool of peripheral controllers.
   */
  @Inject@Nonnull
  LocalPeripheralControllerPool peripheralControllerPool;
  /**
   * The kernel's executor.
   */
  @Inject@Nonnull@ScheduledExecutorServiceAnnotations
  ScheduledExecutorService kernelExecutor;
  /**
   * A task for periodically getting rid of old orders.
   */
  @Inject@Nonnull
  OrderCleanerTask orderCleanerTask;
  /**
   * This kernel state's local extensions.
   */
  @Inject@Nonnull
  Instance<KernelExtension> extensions;
  /**
   * The kernel's attachment manager.
   */
  @Inject@Nonnull
  AttachmentManager attachmentManager;
  /**
   * The kernel's peripheral attachment manager.
   */
  @Inject@Nonnull
  PeripheralAttachmentManager peripheralAttachmentManager;
  /**
   * The vehicle service.
   */
  @Inject@Nonnull@StandardVehicleServiceAnnotations
  InternalVehicleService vehicleService;
  /**
   * A handle for the cleaner task.
   */
  private ScheduledFuture<?> cleanerTaskFuture;
  /**
   * This instance's <em>initialized</em> flag.
   */
  private boolean initialized;

  /**
   * Creates a new KernelStateOperating.
   *
   * @param objectPool The object pool to be used.
   * @param modelPersister The model persister to be used.
   * @param configuration This class's configuration.
   */

  KernelStateOperating(GlobalSyncObject globalSyncObject,
                       TCSObjectPool objectPool,
                       Model model,
                       @XMLFileModelAnnotations ModelPersister modelPersister,
                       KernelApplicationConfiguration configuration) {
    super(globalSyncObject,
          objectPool,
          model,
          modelPersister,
          configuration.saveModelOnTerminateOperating());
    this.configuration = requireNonNull(configuration, "configuration");
  }

  // Implementation of interface Kernel starts here.
  @Override
  public void initialize() {
    if (initialized) {
      LOG.debug("Already initialized.");
      return;
    }
    LOG.debug("Initializing operating state...");

    // Reset vehicle states to ensure vehicles are not dispatchable initially.
    for (Vehicle curVehicle : vehicleService.fetchObjects(Vehicle.class)) {
      vehicleService.updateVehicleProcState(curVehicle.getReference(), Vehicle.ProcState.IDLE);
      vehicleService.updateVehicleIntegrationLevel(curVehicle.getReference(),
                                                   Vehicle.IntegrationLevel.TO_BE_RESPECTED);
      vehicleService.updateVehicleState(curVehicle.getReference(), Vehicle.State.UNKNOWN);
      vehicleService.updateVehicleTransportOrder(curVehicle.getReference(), null);
      vehicleService.updateVehicleOrderSequence(curVehicle.getReference(), null);
    }

    LOG.debug("Initializing scheduler '{}'...", scheduler);
    scheduler.initialize();
    LOG.debug("Initializing router '{}'...", router);
    router.initialize();
    LOG.debug("Initializing dispatcher '{}'...", dispatcher);
    dispatcher.initialize();
    LOG.debug("Initializing peripheral job dispatcher '{}'...", peripheralJobDispatcher);
    peripheralJobDispatcher.initialize();
    LOG.debug("Initializing vehicle controller pool '{}'...", vehicleControllerPool);
    vehicleControllerPool.initialize();
    LOG.debug("Initializing peripheral controller pool '{}'...", peripheralControllerPool);
    peripheralControllerPool.initialize();
    LOG.debug("Initializing attachment manager '{}'...", attachmentManager);
    attachmentManager.initialize();
    LOG.debug("Initializing peripheral attachment manager '{}'...", peripheralAttachmentManager);
    peripheralAttachmentManager.initialize();

    // Start a task for cleaning up old orders periodically.
    cleanerTaskFuture = kernelExecutor.scheduleAtFixedRate(orderCleanerTask,
                                                           orderCleanerTask.getSweepInterval(),
                                                           orderCleanerTask.getSweepInterval(),
                                                           TimeUnit.MILLISECONDS);

    // Start kernel extensions.
    for (KernelExtension extension : extensions) {
      LOG.debug("Initializing kernel extension '{}'...", extension);
      extension.initialize();
    }
    LOG.debug("Finished initializing kernel extensions.");

    initialized = true;

    LOG.debug("Operating state initialized.");
  }

  @Override
  public boolean isInitialized() {
    return initialized;
  }

  @Override
  public void terminate() {
    if (!initialized) {
      LOG.debug("Not initialized.");
      return;
    }
    LOG.debug("Terminating operating state...");
    super.terminate();

    // Terminate everything that may still use resources.
    for (KernelExtension extension : extensions) {
      LOG.debug("Terminating kernel extension '{}'...", extension);
      extension.terminate();
    }
    LOG.debug("Terminated kernel extensions.");

    // No need to clean up any more - it's all going to be cleaned up very soon.
    cleanerTaskFuture.cancel(false);
    cleanerTaskFuture = null;

    // Terminate strategies.
    LOG.debug("Terminating peripheral job dispatcher '{}'...", peripheralJobDispatcher);
    peripheralJobDispatcher.terminate();
    LOG.debug("Terminating dispatcher '{}'...", dispatcher);
    dispatcher.terminate();
    LOG.debug("Terminating router '{}'...", router);
    router.terminate();
    LOG.debug("Terminating scheduler '{}'...", scheduler);
    scheduler.terminate();
    LOG.debug("Terminating peripheral controller pool '{}'...", peripheralControllerPool);
    peripheralControllerPool.terminate();
    LOG.debug("Terminating vehicle controller pool '{}'...", vehicleControllerPool);
    vehicleControllerPool.terminate();
    LOG.debug("Terminating attachment manager '{}'...", attachmentManager);
    attachmentManager.terminate();
    LOG.debug("Terminating peripheral attachment manager '{}'...", peripheralAttachmentManager);
    peripheralAttachmentManager.terminate();
    // Grant communication adapters etc. some time to settle things.
    Uninterruptibles.sleepUninterruptibly(500, TimeUnit.MILLISECONDS);

    // Ensure that vehicles do not reference orders any more.
    for (Vehicle curVehicle : vehicleService.fetchObjects(Vehicle.class)) {
      vehicleService.updateVehicleProcState(curVehicle.getReference(), Vehicle.ProcState.IDLE);
      vehicleService.updateVehicleIntegrationLevel(curVehicle.getReference(),
                                                   Vehicle.IntegrationLevel.TO_BE_RESPECTED);
      vehicleService.updateVehicleState(curVehicle.getReference(), Vehicle.State.UNKNOWN);
      vehicleService.updateVehicleTransportOrder(curVehicle.getReference(), null);
      vehicleService.updateVehicleOrderSequence(curVehicle.getReference(), null);
    }

    // Remove all orders and order sequences from the pool.
    orderPool.clear();
    // Remove all peripheral jobs from the pool.
    jobPool.clear();

    initialized = false;

    LOG.debug("Operating state terminated.");
  }

  @Override
  public Kernel.State getState() {
    return Kernel.State.OPERATING;
  }
}
