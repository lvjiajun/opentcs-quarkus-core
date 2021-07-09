/**
 * Copyright (c) Fraunhofer IML
 */
package org.youbai.opentcs.drivers.adapter.exchange.commands;

import org.youbai.opentcs.drivers.adapter.CommAdapter;
import org.youbai.opentcs.drivers.vehicle.AdapterCommand;
import org.youbai.opentcs.drivers.vehicle.VehicleCommAdapter;

/**
 * A command to set the adapter's disconnecot on vehicle idle flag.
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
public class SetDisconnectingOnVehicleIdleCommand
    implements AdapterCommand {

  /**
   * The flag state to set.
   */
  private final boolean disconnect;

  /**
   * Creates a new instance.
   *
   * @param disconnect The flag state to set
   */
  public SetDisconnectingOnVehicleIdleCommand(boolean disconnect) {
    this.disconnect = disconnect;
  }

  @Override
  public void execute(VehicleCommAdapter adapter) {
    if (!(adapter instanceof CommAdapter)) {
      return;
    }

    CommAdapter exampleAdapter = (CommAdapter) adapter;
    exampleAdapter.getProcessModel().setDisconnectingOnVehicleIdle(disconnect);
  }
}
