/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.strategies.basic.dispatching.priorization.vehicle;

import java.util.Comparator;
import org.youbai.opentcs.data.model.Vehicle;

import javax.enterprise.context.ApplicationScoped;

/**
 * Compares {@link Vehicle}s by their names.
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
@ApplicationScoped
public class VehicleComparatorByName
    implements Comparator<Vehicle> {

  /**
   * A key used for selecting this comparator in a configuration setting.
   * Should be unique among all keys.
   */
  public static final String CONFIGURATION_KEY = "BY_NAME";

  @Override
  public int compare(Vehicle vehicle1, Vehicle vehicle2) {
    return vehicle1.getName().compareTo(vehicle2.getName());
  }

}
