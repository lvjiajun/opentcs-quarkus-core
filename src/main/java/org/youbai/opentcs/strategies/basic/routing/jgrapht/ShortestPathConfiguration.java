/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.strategies.basic.routing.jgrapht;

import io.quarkus.arc.config.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.List;


/**
 * Provides methods to configure the shortest path algorithm.
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
@ConfigProperties(prefix =ShortestPathConfiguration.PREFIX)
public interface ShortestPathConfiguration {

  /**
   * This configuration's prefix.
   */
  String PREFIX = "defaultrouter.shortestpath";

  @ConfigProperty(name = "algorithm")
  Algorithm algorithm();

  @ConfigProperty(name = "edgeEvaluators")
  List<String> edgeEvaluators();

  enum Algorithm {
    DIJKSTRA(false),
    BELLMAN_FORD(true),
    FLOYD_WARSHALL(false);

    private final boolean handlingNegativeCosts;

    private Algorithm(boolean handlingNegativeCosts) {
      this.handlingNegativeCosts = handlingNegativeCosts;
    }

    public boolean isHandlingNegativeCosts() {
      return handlingNegativeCosts;
    }
  }
}
