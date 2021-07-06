/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.components.kernel.routing;

import javax.annotation.Nonnull;
import org.youbai.opentcs.data.model.Vehicle;

/**
 * Computes the weight of edges in the routing graph.
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
public interface EdgeEvaluator {

  /**
   * Computes the weight of an edge in the routing graph.
   *
   * @param edge The edge.
   * @param vehicle The vehicle for which to compute the edge's weight.
   * @return The computed weight of the given edge.
   * A value of {@code Double.POSITIVE_INFINITY} indicates that the edge is to be excluded from
   * routing.
   * Note that negative weights might not be handled well by the respective routing algorithm used.
   */
  double computeWeight(@Nonnull Edge edge, @Nonnull Vehicle vehicle);
}
