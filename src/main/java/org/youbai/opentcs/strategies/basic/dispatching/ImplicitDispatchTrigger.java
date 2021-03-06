/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.strategies.basic.dispatching;

import static java.util.Objects.requireNonNull;
import org.youbai.opentcs.components.kernel.Dispatcher;
import org.youbai.opentcs.data.TCSObjectEvent;
import org.youbai.opentcs.data.model.Vehicle;
import org.youbai.opentcs.util.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An event listener that triggers dispatching of vehicles and transport orders on certain events.
 * 事件监听器，在某些事件上触发车辆调度和运输命令
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
public class ImplicitDispatchTrigger
    implements EventHandler {

  /**
   * This class's Logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(ImplicitDispatchTrigger.class);
  /**
   * The dispatcher in use.
   */
  private final Dispatcher dispatcher;

  /**
   * Creates a new instance.
   *
   * @param dispatcher The dispatcher in use.
   */
  public ImplicitDispatchTrigger(Dispatcher dispatcher) {
    this.dispatcher = requireNonNull(dispatcher, "dispatcher");
  }

  @Override
  public void onEvent(Object event) {
    if (!(event instanceof TCSObjectEvent)) {
      return;
    }
    TCSObjectEvent objectEvent = (TCSObjectEvent) event;
    if (objectEvent.getCurrentOrPreviousObjectState() instanceof Vehicle) {
      checkVehicleChange((Vehicle) objectEvent.getPreviousObjectState(),
                         (Vehicle) objectEvent.getCurrentObjectState());
    }
  }

  private void checkVehicleChange(Vehicle oldVehicle, Vehicle newVehicle) {
    if ((newVehicle.getIntegrationLevel() == Vehicle.IntegrationLevel.TO_BE_UTILIZED
         || newVehicle.getIntegrationLevel() == Vehicle.IntegrationLevel.TO_BE_RESPECTED)
        && (idleAndEnergyLevelChanged(oldVehicle, newVehicle)
            || awaitingNextOrder(oldVehicle, newVehicle)
            || orderSequenceNulled(oldVehicle, newVehicle))) {
      LOG.debug("Dispatching for {}...", newVehicle);
      dispatcher.dispatch();
    }
  }

  private boolean idleAndEnergyLevelChanged(Vehicle oldVehicle, Vehicle newVehicle) {
    // If the vehicle is idle and its energy level changed, we may want to order it to recharge.
    return newVehicle.hasProcState(Vehicle.ProcState.IDLE)
        && (newVehicle.hasState(Vehicle.State.IDLE) || newVehicle.hasState(Vehicle.State.CHARGING))
        && newVehicle.getEnergyLevel() != oldVehicle.getEnergyLevel();
  }

  private boolean awaitingNextOrder(Vehicle oldVehicle, Vehicle newVehicle) {
    // If the vehicle's processing state changed to IDLE or AWAITING_ORDER, it is waiting for
    // its next order, so look for one.
    return newVehicle.getProcState() != oldVehicle.getProcState()
        && (newVehicle.hasProcState(Vehicle.ProcState.IDLE)
            || newVehicle.hasProcState(Vehicle.ProcState.AWAITING_ORDER));
  }

  private boolean orderSequenceNulled(Vehicle oldVehicle, Vehicle newVehicle) {
    // If the vehicle's order sequence reference has become null, the vehicle has just been released
    // from an order sequence, so we may look for new assignments.
    return newVehicle.getOrderSequence() == null
        && oldVehicle.getOrderSequence() != null;
  }
}
