package org.youbai.opentcs.strategies.basic.routing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.youbai.opentcs.data.model.Vehicle;
import org.youbai.opentcs.strategies.basic.routing.jgrapht.BellmanFordPointRouterFactory;
import org.youbai.opentcs.strategies.basic.routing.jgrapht.DijkstraPointRouterFactory;
import org.youbai.opentcs.strategies.basic.routing.jgrapht.FloydWarshallPointRouterFactory;
import org.youbai.opentcs.strategies.basic.routing.jgrapht.ShortestPathConfiguration;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

@ApplicationScoped
public class PointRouterFactoryImp {


    private static final Logger LOG = LoggerFactory.getLogger(PointRouterFactoryImp.class);

    @Inject
    DijkstraPointRouterFactory dijkstraPointRouterFactory;
    @Inject
    BellmanFordPointRouterFactory bellmanFordPointRouterFactory;
    @Inject
    FloydWarshallPointRouterFactory floydWarshallPointRouterFactory;
    @Inject
    ShortestPathConfiguration shortestPathConfiguration;

    @Produces
    @Named("pointRouterFactory")
    PointRouterFactory pointRouterFactory(){
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
