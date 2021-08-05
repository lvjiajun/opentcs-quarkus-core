package org.youbai.opentcs.kernel.extensions.servicewebapi.rest;

import org.youbai.opentcs.kernel.extensions.servicewebapi.data.AppResult;

import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("PeripheralDispatcherService")
public interface RestPeripheralDispatcherService {
    @DELETE
    @Path("withdrawByLocation/{ref}")
    AppResult<String> withdrawByLocation(@PathParam("ref") String ref);
}
