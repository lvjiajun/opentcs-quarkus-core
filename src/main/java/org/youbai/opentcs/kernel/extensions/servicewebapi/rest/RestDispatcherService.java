package org.youbai.opentcs.kernel.extensions.servicewebapi.rest;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.youbai.opentcs.kernel.extensions.servicewebapi.data.AppResult;
@Path("DispatcherService/")
public interface RestDispatcherService {

    @PUT
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/withdrawByVehicle")
    AppResult<String> withdrawByVehicle(@QueryParam("ref") String ref,
                                @QueryParam("immediateAbort") boolean immediateAbort);

    @PUT
    @Produces(MediaType.TEXT_PLAIN)
    @Path("withdrawByTransportOrder")
    AppResult<String> withdrawByTransportOrder(@QueryParam("ref") String ref,
                                       @QueryParam("immediateAbort")boolean immediateAbort);

}
