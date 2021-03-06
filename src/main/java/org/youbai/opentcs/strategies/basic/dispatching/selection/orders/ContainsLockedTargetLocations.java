/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.strategies.basic.dispatching.selection.orders;

import com.google.common.base.Objects;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import static java.util.Objects.requireNonNull;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.youbai.opentcs.components.kernel.services.TCSObjectService;
import org.youbai.opentcs.data.TCSObjectReference;
import org.youbai.opentcs.data.model.Location;
import org.youbai.opentcs.data.order.TransportOrder;
import org.youbai.opentcs.kernel.annotations.StandardTCSObjectAnnotations;
import org.youbai.opentcs.strategies.basic.dispatching.selection.TransportOrderSelectionFilter;

/**
 * Filters transport orders that contain locked target locations.
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
@Singleton
public class ContainsLockedTargetLocations
    implements TransportOrderSelectionFilter {

  /**
   * The object service.
   */
  private final TCSObjectService objectService;


  public ContainsLockedTargetLocations(@StandardTCSObjectAnnotations TCSObjectService objectService) {
    this.objectService = requireNonNull(objectService, "objectService");
  }

  @Override
  public Collection<String> apply(TransportOrder order) {
    return !lockedLocations(order) ? new ArrayList<>() : Arrays.asList(getClass().getName());
  }

  @SuppressWarnings("unchecked")
  private boolean lockedLocations(TransportOrder order) {
    return order.getAllDriveOrders().stream()
        .map(driveOrder -> driveOrder.getDestination().getDestination())
        .filter(destination -> Objects.equal(destination.getReferentClass(), Location.class))
        .map(destination -> (TCSObjectReference<Location>) destination)
        .map(locationReference -> objectService.fetchObject(Location.class, locationReference))
        .anyMatch(location -> location.isLocked());
  }
}
