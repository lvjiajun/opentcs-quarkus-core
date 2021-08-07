package org.youbai.opentcs.kernel.extensions.servicewebapi.rest;

import org.youbai.opentcs.drivers.peripherals.PeripheralAdapterCommand;
import org.youbai.opentcs.drivers.peripherals.PeripheralCommAdapterDescription;
import org.youbai.opentcs.drivers.peripherals.PeripheralProcessModel;
import org.youbai.opentcs.drivers.peripherals.management.PeripheralAttachmentInformation;
import org.youbai.opentcs.kernel.extensions.servicewebapi.data.AppResult;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("PeripheralService")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface RestPeripheralService {

    AppResult<String> attachCommAdapter(String ref,
                                        PeripheralCommAdapterDescription description);
    @PUT
    @Path("disableCommAdapter/{ref}")
    AppResult<String> disableCommAdapter(@PathParam("ref") String ref);
    @PUT
    @Path("enableCommAdapter/{ref}")
    AppResult<String> enableCommAdapter(@PathParam("ref") String ref);
    @GET
    @Path("fetchAttachmentInformation/{ref}")
    AppResult<PeripheralAttachmentInformation> fetchAttachmentInformation(@PathParam("ref") String ref);
    @GET
    @Path("fetchProcessModel/{ref}")
    AppResult<PeripheralProcessModel> fetchProcessModel(@PathParam("ref") String ref);

    AppResult<String> sendCommAdapterCommand(String ref,
                                PeripheralAdapterCommand command);
}
