package org.youbai.opentcs.kernel.extensions.servicewebapi.imp;

import org.youbai.opentcs.access.rmi.services.RemotePlantModelService;
import org.youbai.opentcs.access.to.model.PlantModelCreationTO;
import org.youbai.opentcs.data.ObjectUnknownException;
import org.youbai.opentcs.data.model.Location;
import org.youbai.opentcs.kernel.annotations.StandardTCSObjectAnnotations;
import org.youbai.opentcs.kernel.extensions.servicewebapi.data.AppResult;
import org.youbai.opentcs.kernel.extensions.servicewebapi.data.AppResultBuilder;
import org.youbai.opentcs.kernel.extensions.servicewebapi.data.ResultCode;
import org.youbai.opentcs.kernel.extensions.servicewebapi.rest.RestPlantModelService;
import org.youbai.opentcs.kernel.services.StandardTCSObjectService;

import javax.inject.Inject;
import java.util.Map;

public class PlantModelService implements RestPlantModelService {

    @Inject
    RemotePlantModelService remotePlantModelService;

    @Inject@StandardTCSObjectAnnotations
    StandardTCSObjectService standardTCSObjectService;

    @Override
    public AppResult<String> createPlantModel(PlantModelCreationTO to) {
        remotePlantModelService.createPlantModel(to);
        return AppResultBuilder.success(ResultCode.SUCCESS);
    }

    @Override
    public AppResult<String> getModelName() {
        String modeName = remotePlantModelService.getModelName();
        return AppResultBuilder.success(modeName,ResultCode.SUCCESS);
    }

    @Override
    public AppResult<Map<String, String>> getModelProperties() {
        Map<String,String> Properties = remotePlantModelService.getModelProperties();
        return AppResultBuilder.success(Properties,ResultCode.SUCCESS);
    }

    @Override
    public AppResult<String> updateLocationLock(String ref, boolean locked) {
        try {
            Location location = standardTCSObjectService.fetchObjectNotNull(Location.class,ref);
            remotePlantModelService.updateLocationLock(location.getReference(),locked);
            return AppResultBuilder.success(ResultCode.SUCCESS);
        }catch (ObjectUnknownException exception){
            return AppResultBuilder.faile(ResultCode.DATA_IS_WRONG);
        }
    }
}
