/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.strategies.basic.routing.edgeevaluator;

import org.youbai.opentcs.components.kernel.routing.Edge;
import org.youbai.opentcs.components.kernel.routing.EdgeEvaluator;
import org.youbai.opentcs.data.model.Vehicle;
import org.youbai.opentcs.strategies.basic.routing.PointRouter;

import javax.enterprise.context.ApplicationScoped;

/**
 * Uses the estimated travel time (length/maximum velocity) for an edge as its weight.
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
@ApplicationScoped
public class EdgeEvaluatorTravelTime
    implements EdgeEvaluator {

  /**
   * A key used for selecting this evaluator in a configuration setting.
   * Should be unique among all keys.
   */
  public static final String CONFIGURATION_KEY = "TRAVELTIME";

  public EdgeEvaluatorTravelTime() {
  }

  @Override
  public double computeWeight(Edge edge, Vehicle vehicle) {
    int maxVelocity;
    if (edge.isTravellingReverse()) {
      maxVelocity = Math.min(vehicle.getMaxReverseVelocity(),
                             edge.getPath().getMaxReverseVelocity());
    }
    else {
      maxVelocity = Math.min(vehicle.getMaxVelocity(), edge.getPath().getMaxVelocity());
    }
    return (maxVelocity == 0) ? PointRouter.INFINITE_COSTS : edge.getPath().getLength() / maxVelocity;
  }
}
