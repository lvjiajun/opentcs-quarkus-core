/**
 * Copyright (c) Fraunhofer IML
 */
package org.youbai.opentcs.drivers.adapter.exchange.commands;

import org.youbai.opentcs.drivers.adapter.CommAdapter;
import org.youbai.opentcs.drivers.vehicle.AdapterCommand;
import org.youbai.opentcs.drivers.vehicle.VehicleCommAdapter;

/**
 * A command to set the adapters's idle timeout.
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
public class SetIdleTimeoutCommand
    implements AdapterCommand {

  /**
   * The idle timeout to set.
   */
  private final int timeout;

  /**
   * Creates a new instance.
   *
   * @param timeout The idle timeout to set.
   */
  public SetIdleTimeoutCommand(int timeout) {
    this.timeout = timeout;
  }

  @Override
  public void execute(VehicleCommAdapter adapter) {
    if (!(adapter instanceof CommAdapter)) {
      return;
    }

    CommAdapter exampleAdapter = (CommAdapter) adapter;
    exampleAdapter.getProcessModel().setVehicleIdleTimeout(timeout);
  }
}
