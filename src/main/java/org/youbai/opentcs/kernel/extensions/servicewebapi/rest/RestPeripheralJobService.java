package org.youbai.opentcs.kernel.extensions.servicewebapi.rest;

import org.youbai.opentcs.access.to.peripherals.PeripheralJobCreationTO;
import org.youbai.opentcs.data.peripherals.PeripheralJob;
import org.youbai.opentcs.kernel.extensions.servicewebapi.data.AppResult;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("PeripheralJobService")
public interface RestPeripheralJobService {
    @POST
    @Path("createPeripheralJob")
    AppResult<PeripheralJob> createPeripheralJob(PeripheralJobCreationTO to);
}
