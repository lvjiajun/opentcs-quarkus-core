/**
 * Copyright (c) Fraunhofer IML
 */
package org.youbai.opentcs.drivers.adapter.exchange.commands;

import org.youbai.opentcs.drivers.adapter.CommAdapter;
import org.youbai.opentcs.drivers.vehicle.AdapterCommand;
import org.youbai.opentcs.drivers.vehicle.VehicleCommAdapter;

/**
 * A command to enable/disable periodic state requests.
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
public class SetPeriodicStateRequestEnabledCommand
    implements AdapterCommand {

  /**
   * The new state request state.
   */
  private final boolean enabled;

  /**
   * Creates a new instance.
   *
   * @param enabled The new state request state.
   */
  public SetPeriodicStateRequestEnabledCommand(boolean enabled) {
    this.enabled = enabled;
  }

  @Override
  public void execute(VehicleCommAdapter adapter) {
    if (!(adapter instanceof CommAdapter)) {
      return;
    }

    CommAdapter exampleAdapter = (CommAdapter) adapter;
    exampleAdapter.getProcessModel().setPeriodicStateRequestEnabled(enabled);
  }
}
