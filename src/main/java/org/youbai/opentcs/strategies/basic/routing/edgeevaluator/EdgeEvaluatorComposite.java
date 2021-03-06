/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.strategies.basic.routing.edgeevaluator;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.youbai.opentcs.components.kernel.routing.Edge;
import org.youbai.opentcs.components.kernel.routing.EdgeEvaluator;
import org.youbai.opentcs.data.model.Vehicle;
import org.youbai.opentcs.strategies.basic.routing.jgrapht.ShortestPathConfiguration;
import static org.youbai.opentcs.util.Assertions.checkArgument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link EdgeEvaluator} computing costs as the sum of the costs computed by all configured
 * evaluators.
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
@Singleton
public class EdgeEvaluatorComposite
    implements EdgeEvaluator {

  /**
   * This class's logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(EdgeEvaluatorComposite.class);
  /**
   * The evaluators.
   */
  private final Set<EdgeEvaluator> evaluators = new HashSet<>();

  /**
   * Creates a new instance.
   *
   * @param configuration The configuration to use.
   * @param availableEvaluators The configured evaluators to use.
   */

  public EdgeEvaluatorComposite(ShortestPathConfiguration configuration,
                                Map<String, EdgeEvaluator> availableEvaluators) {
    if (availableEvaluators.isEmpty()) {
      LOG.warn("No edge evaluator enabled, falling back to distance-based evaluation.");
      evaluators.add(new EdgeEvaluatorDistance());
    }
    else {
      for (String evaluatorKey : configuration.edgeEvaluators()) {
        checkArgument(availableEvaluators.containsKey(evaluatorKey),
                      "Unknown edge evaluator key: %s",
                      evaluatorKey);
        evaluators.add(availableEvaluators.get(evaluatorKey));
      }
    }
  }

  @Override
  public double computeWeight(Edge edge, Vehicle vehicle) {
    double result = 0.0;
    for (EdgeEvaluator component : evaluators) {
      result += component.computeWeight(edge, vehicle);
    }
    return result;
  }
}
