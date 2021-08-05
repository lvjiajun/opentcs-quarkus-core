package org.youbai.opentcs.kernel.extensions.servicewebapi.rest;

import org.youbai.opentcs.data.TCSObjectReference;
import org.youbai.opentcs.data.model.Path;
import org.youbai.opentcs.kernel.extensions.servicewebapi.data.AppResult;

import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;

@javax.ws.rs.Path("RouterService ")
public interface RestRouterService {
    @PUT
    @javax.ws.rs.Path("updatePathLock")
    AppResult<String> updatePathLock(@QueryParam("ref") String ref, @QueryParam("locked") boolean locked);
    @PUT
    @javax.ws.rs.Path("updateRoutingTopology")
    AppResult<String> updateRoutingTopology();
}
