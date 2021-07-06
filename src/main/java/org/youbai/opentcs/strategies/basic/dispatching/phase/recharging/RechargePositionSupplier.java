/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.strategies.basic.dispatching.phase.recharging;

import java.util.List;
import javax.annotation.Nonnull;
import org.youbai.opentcs.components.Lifecycle;
import org.youbai.opentcs.data.model.Vehicle;
import org.youbai.opentcs.data.order.DriveOrder;

/**
 * A strategy for finding locations suitable for recharging vehicles.
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
public interface RechargePositionSupplier
    extends Lifecycle {

  /**
   * Returns a sequence of destinations for recharging the given vehicle.
   * In most cases, the sequence will probably consist of only one destination. Some vehicles may
   * require to visit more than one destination for recharging, though, e.g. to drop off a battery
   * pack at one location and get a fresh one at another location.
   *
   * @param vehicle The vehicle to be recharged.
   * @return A sequence of destinations including operations for recharging the given vehicle. If
   * no suitable sequence was found, the returned sequence will be empty.
   */
  @Nonnull
  List<DriveOrder.Destination> findRechargeSequence(@Nonnull Vehicle vehicle);
}
