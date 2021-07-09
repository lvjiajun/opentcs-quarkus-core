/**
 * Copyright (c) Fraunhofer IML
 */
package org.youbai.opentcs.drivers.adapter.exchange.commands;

import org.youbai.opentcs.drivers.adapter.CommAdapter;
import org.youbai.opentcs.drivers.vehicle.AdapterCommand;
import org.youbai.opentcs.drivers.vehicle.VehicleCommAdapter;

/**
 * A command to set the adapter's reconnect on connection loss flag.
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
public class SetReconnectingOnConnectionLossCommand
    implements AdapterCommand {

  /**
   * The flag state to set.
   */
  private final boolean reconnect;

  /**
   * Creates a new instance.
   *
   * @param reconnect The flag state to set
   */
  public SetReconnectingOnConnectionLossCommand(boolean reconnect) {
    this.reconnect = reconnect;
  }

  @Override
  public void execute(VehicleCommAdapter adapter) {
    if (!(adapter instanceof CommAdapter)) {
      return;
    }

    CommAdapter exampleAdapter = (CommAdapter) adapter;
    exampleAdapter.getProcessModel().setReconnectingOnConnectionLoss(reconnect);
  }
}
