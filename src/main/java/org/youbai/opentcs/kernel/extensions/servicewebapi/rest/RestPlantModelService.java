package org.youbai.opentcs.kernel.extensions.servicewebapi.rest;

import org.youbai.opentcs.access.to.model.PlantModelCreationTO;
import org.youbai.opentcs.kernel.extensions.servicewebapi.data.AppResult;

import javax.ws.rs.*;
import java.util.Map;

@Path("PlantModelService")
public interface RestPlantModelService {
    @POST
    @Path("createPlantModel")
    AppResult<String> createPlantModel(PlantModelCreationTO to);
    @GET
    @Path("getModelName")
    AppResult<String> getModelName();
    @GET
    @Path("getModelProperties")
    AppResult<Map<String, String>> getModelProperties();
    @PUT
    @Path("updateLocationLock")
    AppResult<String> updateLocationLock(@QueryParam("ref") String ref,
                            @QueryParam("ref") boolean locked);
}
