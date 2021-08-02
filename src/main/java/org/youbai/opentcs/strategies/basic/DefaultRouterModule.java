package org.youbai.opentcs.strategies.basic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.youbai.opentcs.components.kernel.routing.EdgeEvaluator;
import org.youbai.opentcs.strategies.basic.routing.PointRouterFactory;
import org.youbai.opentcs.strategies.basic.routing.PointRouterFactoryAnnotation;
import org.youbai.opentcs.strategies.basic.routing.edgeevaluator.EdgeEvaluatorDistance;
import org.youbai.opentcs.strategies.basic.routing.edgeevaluator.EdgeEvaluatorExplicitProperties;
import org.youbai.opentcs.strategies.basic.routing.edgeevaluator.EdgeEvaluatorHops;
import org.youbai.opentcs.strategies.basic.routing.edgeevaluator.EdgeEvaluatorTravelTime;
import org.youbai.opentcs.strategies.basic.routing.jgrapht.BellmanFordPointRouterFactory;
import org.youbai.opentcs.strategies.basic.routing.jgrapht.DijkstraPointRouterFactory;
import org.youbai.opentcs.strategies.basic.routing.jgrapht.FloydWarshallPointRouterFactory;
import org.youbai.opentcs.strategies.basic.routing.jgrapht.ShortestPathConfiguration;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class DefaultRouterModule {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultRouterModule.class);

    @Inject
    EdgeEvaluatorDistance edgeEvaluatorDistance;
    @Inject
    EdgeEvaluatorExplicitProperties edgeEvaluatorExplicitProperties;
    @Inject
    EdgeEvaluatorHops edgeEvaluatorHops;
    @Inject
    EdgeEvaluatorTravelTime edgeEvaluatorTravelTime;

    @Produces
    @ApplicationScoped
    Map<String, EdgeEvaluator> edgeEvaluatorBinder(){
        ConcurrentHashMap<String, EdgeEvaluator> concurrentHashMap = new ConcurrentHashMap<>();
        concurrentHashMap.put(EdgeEvaluatorDistance.CONFIGURATION_KEY,edgeEvaluatorDistance);
        concurrentHashMap.put(EdgeEvaluatorExplicitProperties.CONFIGURATION_KEY,edgeEvaluatorExplicitProperties);
        concurrentHashMap.put(EdgeEvaluatorHops.CONFIGURATION_KEY,edgeEvaluatorHops);
        concurrentHashMap.put(EdgeEvaluatorTravelTime.CONFIGURATION_KEY,edgeEvaluatorTravelTime);
        return concurrentHashMap;
    }

    @Inject
    DijkstraPointRouterFactory dijkstraPointRouterFactory;
    @Inject
    BellmanFordPointRouterFactory bellmanFordPointRouterFactory;
    @Inject
    FloydWarshallPointRouterFactory floydWarshallPointRouterFactory;
    @Inject
    ShortestPathConfiguration shortestPathConfiguration;

    @Produces@PointRouterFactoryAnnotation
    public PointRouterFactory pointRouterFactory(){
        switch (shortestPathConfiguration.algorithm()) {
            case DIJKSTRA:
                return dijkstraPointRouterFactory;
            case BELLMAN_FORD:
                return bellmanFordPointRouterFactory;
            case FLOYD_WARSHALL:
                return floydWarshallPointRouterFactory;
            default:
                LOG.warn("Unhandled algorithm selected ({}), falling back to Dijkstra's algorithm.",
                        shortestPathConfiguration.algorithm());
                return dijkstraPointRouterFactory;
        }
    }
}
