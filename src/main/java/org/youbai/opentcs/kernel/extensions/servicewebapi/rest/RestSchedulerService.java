package org.youbai.opentcs.kernel.extensions.servicewebapi.rest;

import org.youbai.opentcs.access.SchedulerAllocationState;
import org.youbai.opentcs.kernel.extensions.servicewebapi.data.AppResult;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("SchedulerService")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface RestSchedulerService {
    @GET
    @Path("fetchSchedulerAllocations")
    AppResult<SchedulerAllocationState> fetchSchedulerAllocations();
}
