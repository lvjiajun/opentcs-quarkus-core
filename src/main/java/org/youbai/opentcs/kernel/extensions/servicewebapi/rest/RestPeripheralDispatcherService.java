package org.youbai.opentcs.kernel.extensions.servicewebapi.rest;

import org.youbai.opentcs.kernel.extensions.servicewebapi.data.AppResult;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("PeripheralDispatcherService")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface RestPeripheralDispatcherService {
    @DELETE
    @Path("withdrawByLocation/{ref}")
    AppResult<String> withdrawByLocation(@PathParam("ref") String ref);
}
