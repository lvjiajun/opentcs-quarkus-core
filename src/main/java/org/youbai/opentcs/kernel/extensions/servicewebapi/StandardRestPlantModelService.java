package org.youbai.opentcs.kernel.extensions.servicewebapi;

import io.quarkus.vertx.web.Body;
import org.youbai.opentcs.access.rmi.ClientID;
import org.youbai.opentcs.access.to.model.PlantModelCreationTO;
import org.youbai.opentcs.components.kernel.services.PlantModelService;
import org.youbai.opentcs.data.TCSObjectReference;
import org.youbai.opentcs.data.model.Location;
import org.youbai.opentcs.kernel.annotations.StandardPlantModelServiceAnnotations;
import org.youbai.opentcs.kernel.extensions.rmi.UserPermission;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.*;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

@Path("StandardRestPlantModelService")
public class StandardRestPlantModelService extends StandardRestService{

    @Inject
    @StandardPlantModelServiceAnnotations
    PlantModelService plantModelService;

    @Inject
    @Named("ExecutorService")
    ExecutorService kernelExecutor;

    @POST
    @Path("createPlantModel")
    @Consumes("application/json")
    @Produces("application/json")
    public void createPlantModel(PlantModelCreationTO to) {
        try {
            kernelExecutor.submit(() -> plantModelService.createPlantModel(to)).get();
        }
        catch (InterruptedException | ExecutionException exc) {
            throw findSuitableExceptionFor(exc);
        }
    }
    @GET
    @Path("getModelName")
    @Produces("application/json")
    public String getModelName() {
        return plantModelService.getModelName();
    }
    @GET
    @Path("getModelProperties")
    @Produces("application/json")
    public Map<String, String> getModelProperties() {
        return plantModelService.getModelProperties();
    }

    @POST
    @Path("updateLocationLock")
    @Consumes("application/json")
    @Produces("application/json")
    public void updateLocationLock(TCSObjectReference<Location> ref, boolean locked)
            throws RemoteException {
        try {
            kernelExecutor.submit(() -> plantModelService.updateLocationLock(ref, locked)).get();
        }
        catch (InterruptedException | ExecutionException exc) {
            throw findSuitableExceptionFor(exc);
        }
    }
}
