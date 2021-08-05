package org.youbai.opentcs.kernel.extensions.servicewebapi.rest;

import org.youbai.opentcs.access.SchedulerAllocationState;
import org.youbai.opentcs.kernel.extensions.servicewebapi.data.AppResult;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("SchedulerService")
public interface RestSchedulerService {
    @GET
    @Path("fetchSchedulerAllocations")
    AppResult<SchedulerAllocationState> fetchSchedulerAllocations();
}
