/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.kernel.extensions.controlcenter.vehicles;

import org.youbai.opentcs.data.model.Vehicle;
import org.youbai.opentcs.drivers.vehicle.VehicleCommAdapter;
import org.youbai.opentcs.drivers.vehicle.VehicleCommAdapterDescription;
import org.youbai.opentcs.drivers.vehicle.VehicleCommAdapterFactory;

/**
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
public class NullVehicleCommAdapterFactory
    implements VehicleCommAdapterFactory {

  /**
   * Creates a new instance.
   */
  public NullVehicleCommAdapterFactory() {
  }

  @Override
  public VehicleCommAdapterDescription getDescription() {
    return new NullVehicleCommAdapterDescription();
  }

  @Override
  public boolean providesAdapterFor(Vehicle vehicle) {
    return false;
  }

  @Override
  public VehicleCommAdapter getAdapterFor(Vehicle vehicle) {
    return null;
  }

  @Override
  public void initialize() {
  }

  @Override
  public boolean isInitialized() {
    return true;
  }

  @Override
  public void terminate() {
  }
}
