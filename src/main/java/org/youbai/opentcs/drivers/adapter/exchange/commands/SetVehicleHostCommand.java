/**
 * Copyright (c) Fraunhofer IML
 */
package org.youbai.opentcs.drivers.adapter.exchange.commands;

import org.youbai.opentcs.drivers.adapter.CommAdapter;
import static java.util.Objects.requireNonNull;
import org.youbai.opentcs.drivers.vehicle.AdapterCommand;
import org.youbai.opentcs.drivers.vehicle.VehicleCommAdapter;

/**
 * A command to set the vehicle's host.
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
public class SetVehicleHostCommand
    implements AdapterCommand {

  /**
   * The host to set.
   */
  private final String host;

  /**
   * Creates a new instnace.
   *
   * @param host The host to set.
   */
  public SetVehicleHostCommand(String host) {
    this.host = requireNonNull(host, "host");
  }

  @Override
  public void execute(VehicleCommAdapter adapter) {
    if (!(adapter instanceof CommAdapter)) {
      return;
    }

    CommAdapter exampleAdapter = (CommAdapter) adapter;
    exampleAdapter.getProcessModel().setVehicleHost(host);
  }
}
