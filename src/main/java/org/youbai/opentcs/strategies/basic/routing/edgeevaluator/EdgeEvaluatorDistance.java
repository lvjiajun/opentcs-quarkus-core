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

/**
 * Uses an edge's length as its weight.
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
public class EdgeEvaluatorDistance
    implements EdgeEvaluator {

  /**
   * A key used for selecting this evaluator in a configuration setting.
   * Should be unique among all keys.
   */
  public static final String CONFIGURATION_KEY = "DISTANCE";

  public EdgeEvaluatorDistance() {
  }

  @Override
  public double computeWeight(Edge edge, Vehicle vehicle) {
    return edge.getPath().getLength();
  }
}
