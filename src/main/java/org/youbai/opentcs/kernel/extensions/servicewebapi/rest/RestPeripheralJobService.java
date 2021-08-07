package org.youbai.opentcs.kernel.extensions.servicewebapi.rest;

import org.youbai.opentcs.access.to.peripherals.PeripheralJobCreationTO;
import org.youbai.opentcs.data.peripherals.PeripheralJob;
import org.youbai.opentcs.kernel.extensions.servicewebapi.data.AppResult;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("PeripheralJobService")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface RestPeripheralJobService {
    @POST
    @Path("createPeripheralJob")
    AppResult<PeripheralJob> createPeripheralJob(PeripheralJobCreationTO to);
}
