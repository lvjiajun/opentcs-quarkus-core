/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.kernel.vehicles;

import org.youbai.opentcs.data.model.Vehicle;
import org.youbai.opentcs.drivers.vehicle.VehicleCommAdapter;

import javax.inject.Singleton;

/**
 * A factory for <code>VehicleManager</code> instances.
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
public interface VehicleControllerFactory {

  /**
   * Creates a new vehicle controller for the given vehicle and communication adapter.
   *
   * @param vehicle The vehicle.
   * @param commAdapter The communication adapter.
   * @return A new vehicle controller.
   */
  DefaultVehicleController createVehicleController(Vehicle vehicle, VehicleCommAdapter commAdapter);
}
