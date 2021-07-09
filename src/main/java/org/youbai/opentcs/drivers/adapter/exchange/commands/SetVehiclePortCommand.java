/**
 * Copyright (c) Fraunhofer IML
 */
package org.youbai.opentcs.drivers.adapter.exchange.commands;

import org.youbai.opentcs.drivers.adapter.CommAdapter;
import org.youbai.opentcs.drivers.vehicle.AdapterCommand;
import org.youbai.opentcs.drivers.vehicle.VehicleCommAdapter;

/**
 * A command to set the vehicle's port.
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
public class SetVehiclePortCommand
    implements AdapterCommand {

  /**
   * The port to set.
   */
  private final int port;

  /**
   * Creates a new instnace.
   *
   * @param port The host to set.
   */
  public SetVehiclePortCommand(int port) {
    this.port = port;
  }

  @Override
  public void execute(VehicleCommAdapter adapter) {
    if (!(adapter instanceof CommAdapter)) {
      return;
    }

    CommAdapter exampleAdapter = (CommAdapter) adapter;
    exampleAdapter.getProcessModel().setVehiclePort(port);
  }
}
