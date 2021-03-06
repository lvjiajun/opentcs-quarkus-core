/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.strategies.basic.dispatching.phase;

import java.util.Collection;
import static java.util.Objects.requireNonNull;
import org.youbai.opentcs.data.model.Vehicle;

/**
 * The result of a vehicle filter operation.
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
public class VehicleFilterResult {

  private final Vehicle vehicle;

  private final Collection<String> filterReasons;

  public VehicleFilterResult(Vehicle vehicle, Collection<String> filterReasons) {
    this.vehicle = requireNonNull(vehicle, "vehicle");
    this.filterReasons = requireNonNull(filterReasons, "filterReasons");
  }

  public Vehicle getVehicle() {
    return vehicle;
  }

  public Collection<String> getFilterReasons() {
    return filterReasons;
  }

  public boolean isFiltered() {
    return !filterReasons.isEmpty();
  }
}
