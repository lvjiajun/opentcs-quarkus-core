/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.strategies.basic.dispatching;

import static java.util.Objects.requireNonNull;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.youbai.opentcs.components.Lifecycle;
import org.youbai.opentcs.strategies.basic.dispatching.phase.AssignReservedOrdersPhase;
import org.youbai.opentcs.strategies.basic.dispatching.phase.AssignSequenceSuccessorsPhase;
import org.youbai.opentcs.strategies.basic.dispatching.phase.CheckNewOrdersPhase;
import org.youbai.opentcs.strategies.basic.dispatching.phase.FinishWithdrawalsPhase;
import org.youbai.opentcs.strategies.basic.dispatching.phase.assignment.AssignFreeOrdersPhase;

import org.youbai.opentcs.strategies.basic.dispatching.phase.assignment.AssignNextDriveOrdersPhase;
import org.youbai.opentcs.strategies.basic.dispatching.phase.parking.ParkIdleVehiclesPhase;
import org.youbai.opentcs.strategies.basic.dispatching.phase.parking.PrioritizedParkingPhase;
import org.youbai.opentcs.strategies.basic.dispatching.phase.parking.PrioritizedReparkPhase;
import org.youbai.opentcs.strategies.basic.dispatching.phase.recharging.RechargeIdleVehiclesPhase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Performs a full dispatch run.
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
@Singleton
public class FullDispatchTask
    implements Runnable,
        Lifecycle {

  /**
   * This class's logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(FullDispatchTask.class);

  @Inject
  CheckNewOrdersPhase checkNewOrdersPhase;
  @Inject
  FinishWithdrawalsPhase finishWithdrawalsPhase;
  @Inject
  AssignNextDriveOrdersPhase assignNextDriveOrdersPhase;
  @Inject
  AssignReservedOrdersPhase assignReservedOrdersPhase;
  @Inject
  AssignSequenceSuccessorsPhase assignSequenceSuccessorsPhase;
  @Inject
  AssignFreeOrdersPhase assignFreeOrdersPhase;
  @Inject
  RechargeIdleVehiclesPhase rechargeIdleVehiclesPhase;
  @Inject
  PrioritizedReparkPhase prioritizedReparkPhase;
  @Inject
  PrioritizedParkingPhase prioritizedParkingPhase;
  @Inject
  ParkIdleVehiclesPhase parkIdleVehiclesPhase;
  /**
   * Indicates whether this component is enabled.
   */
  private boolean initialized;


  public FullDispatchTask() {
  }

  @Override
  public void initialize() {
    if (isInitialized()) {
      return;
    }

    checkNewOrdersPhase.initialize();
    finishWithdrawalsPhase.initialize();
    assignNextDriveOrdersPhase.initialize();
    assignReservedOrdersPhase.initialize();
    assignSequenceSuccessorsPhase.initialize();
    assignFreeOrdersPhase.initialize();
    rechargeIdleVehiclesPhase.initialize();
    prioritizedReparkPhase.initialize();
    prioritizedParkingPhase.initialize();
    parkIdleVehiclesPhase.initialize();

    initialized = true;
  }

  @Override
  public void terminate() {
    if (!isInitialized()) {
      return;
    }

    checkNewOrdersPhase.terminate();
    finishWithdrawalsPhase.terminate();
    assignNextDriveOrdersPhase.terminate();
    assignReservedOrdersPhase.terminate();
    assignSequenceSuccessorsPhase.terminate();
    assignFreeOrdersPhase.terminate();
    rechargeIdleVehiclesPhase.terminate();
    prioritizedReparkPhase.terminate();
    prioritizedParkingPhase.terminate();
    parkIdleVehiclesPhase.terminate();

    initialized = false;
  }

  @Override
  public boolean isInitialized() {
    return initialized;
  }

  @Override
  public final void run() {
    LOG.debug("Starting full dispatch run...");

    checkNewOrdersPhase.run();
    // Check what vehicles involved in a process should do.
    finishWithdrawalsPhase.run();
    assignNextDriveOrdersPhase.run();
    assignSequenceSuccessorsPhase.run();
    // Check what vehicles not already in a process should do.
    assignOrders();
    rechargeVehicles();
    parkVehicles();

    LOG.debug("Finished full dispatch run.");
  }

  /**
   * Assignment of orders to vehicles.
   * <p>
   * Default: Assigns reserved and then free orders to vehicles.
   * </p>
   */
  protected void assignOrders() {
    assignReservedOrdersPhase.run();
    assignFreeOrdersPhase.run();
  }

  /**
   * Recharging of vehicles.
   * <p>
   * Default: Sends idle vehicles with a degraded energy level to recharge locations.
   * </p>
   */
  protected void rechargeVehicles() {
    rechargeIdleVehiclesPhase.run();
  }

  /**
   * Parking of vehicles.
   * <p>
   * Default: Sends idle vehicles to parking positions.
   * </p>
   */
  protected void parkVehicles() {
    prioritizedReparkPhase.run();
    prioritizedParkingPhase.run();
    parkIdleVehiclesPhase.run();
  }
}
