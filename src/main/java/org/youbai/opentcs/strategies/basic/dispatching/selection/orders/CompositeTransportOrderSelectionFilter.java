/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.strategies.basic.dispatching.selection.orders;

import java.util.Collection;
import static java.util.Objects.requireNonNull;
import java.util.Set;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.youbai.opentcs.data.order.TransportOrder;
import org.youbai.opentcs.strategies.basic.dispatching.selection.TransportOrderSelectionFilter;

/**
 * A collection of {@link TransportOrderSelectionFilter}s.
 * 
 * @author Martin Grzenia (Fraunhofer IML)
 */
@ApplicationScoped
public class CompositeTransportOrderSelectionFilter
    implements TransportOrderSelectionFilter {

  /**
   * The {@link TransportOrderSelectionFilter}s.
   */
  private final Instance<TransportOrderSelectionFilter> filters;
  

  public CompositeTransportOrderSelectionFilter(Instance<TransportOrderSelectionFilter> filters) {
    this.filters = requireNonNull(filters, "filters");
  }

  @Override
  public Collection<String> apply(TransportOrder order) {
    return filters.stream()
        .flatMap(filter -> filter.apply(order).stream())
        .collect(Collectors.toList());
  }
}
