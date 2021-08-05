package org.youbai.opentcs.kernel.extensions.servicewebapi.rest;
import org.youbai.opentcs.access.Kernel;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import org.youbai.opentcs.kernel.extensions.servicewebapi.data.AppResult;
@Path("KernelService")
public interface RestKernelServicePortal {
    @GET
    @Path("getState")
    AppResult<Kernel.State> getState();

    @PUT
    @Path("publishEvent")
    AppResult<String> publishEvent(Object event);
}
