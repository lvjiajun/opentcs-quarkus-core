package org.youbai.opentcs.kernel.extensions.servicewebapi.rest;

import org.youbai.opentcs.data.model.Vehicle;
import org.youbai.opentcs.drivers.vehicle.AdapterCommand;
import org.youbai.opentcs.drivers.vehicle.VehicleCommAdapterDescription;
import org.youbai.opentcs.drivers.vehicle.management.AttachmentInformation;
import org.youbai.opentcs.drivers.vehicle.management.VehicleProcessModelTO;
import org.youbai.opentcs.kernel.extensions.servicewebapi.data.AppResult;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import java.util.Set;

@Path("VehicleService")
public interface RestVehicleService {
    @PUT
    @Path("attachCommAdapter")
    AppResult<String> attachCommAdapter(String ref,
                                        VehicleCommAdapterDescription description);
    @PUT
    @Path("disableCommAdapter")
    AppResult<String> disableCommAdapter(String ref);

    @PUT
    @Path("enableCommAdapter")
    AppResult<String> enableCommAdapter(String ref);

    @GET
    @Path("fetchAttachmentInformation")
    AppResult<AttachmentInformation> fetchAttachmentInformation(String ref);

    @GET
    @Path("fetchProcessModel")
    AppResult<VehicleProcessModelTO> fetchProcessModel(String ref);

    @POST
    @Path("sendCommAdapterCommand")
    AppResult<String> sendCommAdapterCommand(String ref,
                                AdapterCommand command);

    @POST
    @Path("sendCommAdapterMessage")
    AppResult<String> sendCommAdapterMessage(String vehicleRef,
                                Object message);

    @PUT
    @Path("updateVehicleIntegrationLevel")
    AppResult<String> updateVehicleIntegrationLevel(String ref,
                                       Vehicle.IntegrationLevel integrationLevel);

    @PUT
    @Path("updateVehicleAllowedOrderTypes")
    AppResult<String> updateVehicleAllowedOrderTypes(String ref,
                                        Set<String> allowedOrderTypes);
}
