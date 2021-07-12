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
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.youbai.opentcs.data.model.Vehicle;
import org.youbai.opentcs.strategies.basic.dispatching.selection.ReparkVehicleSelectionFilter;

/**
 * A collection of {@link ReparkVehicleSelectionFilter}s.
 * 
 * @author Stefan Walter (Fraunhofer IML)
 */
@Singleton
public class CompositeReparkVehicleSelectionFilter
    implements ReparkVehicleSelectionFilter {

  /**
   * The {@link ParkVehicleSelectionFilter}s.
   */
  private final Instance<ReparkVehicleSelectionFilter> filters;
  

  public CompositeReparkVehicleSelectionFilter(Instance<ReparkVehicleSelectionFilter> filters) {
    this.filters = requireNonNull(filters, "filters");
  }

  @Override
  public Collection<String> apply(Vehicle vehicle) {
    return filters.stream()
        .flatMap(filter -> filter.apply(vehicle).stream())
        .collect(Collectors.toList());
  }
}
