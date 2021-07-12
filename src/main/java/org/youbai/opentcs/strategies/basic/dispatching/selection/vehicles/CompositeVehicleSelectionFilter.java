/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.strategies.basic.dispatching.selection.vehicles;

import java.util.Collection;
import static java.util.Objects.requireNonNull;
import java.util.Set;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.youbai.opentcs.data.model.Vehicle;
import org.youbai.opentcs.strategies.basic.dispatching.selection.VehicleSelectionFilter;

/**
 * A collection of {@link VehicleSelectionFilter}s.
 * 
 * @author Martin Grzenia (Fraunhofer IML)
 */
@Singleton
public class CompositeVehicleSelectionFilter
    implements VehicleSelectionFilter {

  /**
   * The {@link VehicleSelectionFilter}s.
   */
  private final Instance<VehicleSelectionFilter> filters;
  

  public CompositeVehicleSelectionFilter(Instance<VehicleSelectionFilter> filters) {
    this.filters = requireNonNull(filters, "filters");
  }

  @Override
  public Collection<String> apply(Vehicle vehicle) {
    return filters.stream()
        .flatMap(filter -> filter.apply(vehicle).stream())
        .collect(Collectors.toList());
  }
}
