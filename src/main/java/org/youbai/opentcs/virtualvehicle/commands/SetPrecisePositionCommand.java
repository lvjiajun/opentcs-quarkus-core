/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.virtualvehicle.commands;

import javax.annotation.Nullable;
import org.youbai.opentcs.data.model.Triple;
import org.youbai.opentcs.drivers.vehicle.AdapterCommand;
import org.youbai.opentcs.drivers.vehicle.VehicleCommAdapter;

/**
 * A command to set a vehicle's precise position.
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
public class SetPrecisePositionCommand
    implements AdapterCommand {

  /**
   * The percise position to set.
   */
  private final Triple position;

  /**
   * Creates a new instance.
   *
   * @param position The precise position to set.
   */
  public SetPrecisePositionCommand(@Nullable Triple position) {
    this.position = position;
  }

  @Override
  public void execute(VehicleCommAdapter adapter) {
    adapter.getProcessModel().setVehiclePrecisePosition(position);
  }
}
