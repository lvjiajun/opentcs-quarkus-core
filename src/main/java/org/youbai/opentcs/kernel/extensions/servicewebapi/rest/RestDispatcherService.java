package org.youbai.opentcs.kernel.extensions.servicewebapi.rest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.youbai.opentcs.kernel.extensions.servicewebapi.data.AppResult;
@Path("DispatcherService/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface RestDispatcherService {

    @PUT
    @Path("/withdrawByVehicle")
    AppResult<String> withdrawByVehicle(@QueryParam("ref") String ref,
                                @QueryParam("immediateAbort") boolean immediateAbort);

    @PUT
    @Path("withdrawByTransportOrder")
    AppResult<String> withdrawByTransportOrder(@QueryParam("ref") String ref,
                                       @QueryParam("immediateAbort")boolean immediateAbort);

}
