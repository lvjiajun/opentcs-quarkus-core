package org.youbai.opentcs.kernel.extensions.servicewebapi.rest;
import org.youbai.opentcs.access.Kernel;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.youbai.opentcs.kernel.extensions.servicewebapi.data.AppResult;
@Path("KernelService")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface RestKernelServicePortal {
    @GET
    @Path("getState")
    AppResult<Kernel.State> getState();

    @PUT
    @Path("publishEvent")
    AppResult<String> publishEvent(Object event);
}
