/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.kernel.peripherals;

import org.youbai.opentcs.data.model.Location;
import org.youbai.opentcs.data.model.TCSResourceReference;
import org.youbai.opentcs.drivers.peripherals.PeripheralCommAdapter;
import org.youbai.opentcs.drivers.peripherals.PeripheralController;

/**
 * A factory for {@link PeripheralController} instances.
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
public interface PeripheralControllerFactory {

  /**
   * Creates a new peripheral controller for the given location and communication adapter.
   *
   * @param location The location.
   * @param commAdapter The communication adapter.
   * @return A new peripheral controller.
   */
  DefaultPeripheralController createVehicleController(TCSResourceReference<Location> location,
                                                      PeripheralCommAdapter commAdapter);
}
