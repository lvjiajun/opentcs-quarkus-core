/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.strategies.basic.dispatching.selection.orders;

import static java.util.Objects.requireNonNull;
import java.util.function.Predicate;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.youbai.opentcs.components.kernel.services.TCSObjectService;
import org.youbai.opentcs.data.ObjectHistory;
import org.youbai.opentcs.data.order.OrderSequence;
import org.youbai.opentcs.data.order.TransportOrder;
import org.youbai.opentcs.kernel.annotations.StandardTCSObjectAnnotations;
import org.youbai.opentcs.strategies.basic.dispatching.OrderReservationPool;
import org.youbai.opentcs.strategies.basic.dispatching.selection.TransportOrderSelectionFilter;

/**
 * Filters transport orders that are dispatchable and available to <em>any</em> vehicle.
 *
 * <p>
 * Note: This filter is not a {@link TransportOrderSelectionFilter} by intention, since it is not
 * intended to be used in contexts where {@link ObjectHistory} entries are created.
 * </p>
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
@Singleton
public class IsFreelyDispatchableToAnyVehicle
    implements Predicate<TransportOrder> {

  /**
   * The order service.
   */
  private final TCSObjectService objectService;
  /**
   * Stores reservations of orders for vehicles.
   */
  private final OrderReservationPool orderReservationPool;

  /**
   * Creates a new isntance.
   *
   * @param objectService The order service.
   * @param orderReservationPool Stores reservations of orders for vehicles.
   */

  public IsFreelyDispatchableToAnyVehicle(@StandardTCSObjectAnnotations TCSObjectService objectService,
                                          OrderReservationPool orderReservationPool) {
    this.objectService = requireNonNull(objectService, "objectService");
    this.orderReservationPool = requireNonNull(orderReservationPool, "orderReservationPool");
  }

  @Override
  public boolean test(TransportOrder order) {
    // We only want to check dispatchable transport orders.
    // Filter out transport orders that are intended for other vehicles.
    // Also filter out all transport orders with reservations. We assume that a check for reserved
    // orders has been performed already, and if any had been found, we wouldn't have been called.
    return order.hasState(TransportOrder.State.DISPATCHABLE)
        && !partOfAnyVehiclesSequence(order)
        && !orderReservationPool.isReserved(order.getReference());
  }

  private boolean partOfAnyVehiclesSequence(TransportOrder order) {
    if (order.getWrappingSequence() == null) {
      return false;
    }
    OrderSequence seq = objectService.fetchObject(OrderSequence.class,
                                                  order.getWrappingSequence());
    return seq != null && seq.getProcessingVehicle() != null;
  }
}
