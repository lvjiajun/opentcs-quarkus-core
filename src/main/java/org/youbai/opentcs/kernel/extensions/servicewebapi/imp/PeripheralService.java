package org.youbai.opentcs.kernel.extensions.servicewebapi.imp;

import org.youbai.opentcs.access.rmi.services.RemotePeripheralService;
import org.youbai.opentcs.data.ObjectUnknownException;
import org.youbai.opentcs.data.model.Location;
import org.youbai.opentcs.drivers.peripherals.PeripheralAdapterCommand;
import org.youbai.opentcs.drivers.peripherals.PeripheralCommAdapterDescription;
import org.youbai.opentcs.drivers.peripherals.PeripheralProcessModel;
import org.youbai.opentcs.drivers.peripherals.management.PeripheralAttachmentInformation;
import org.youbai.opentcs.kernel.annotations.StandardTCSObjectAnnotations;
import org.youbai.opentcs.kernel.extensions.servicewebapi.data.AppResult;
import org.youbai.opentcs.kernel.extensions.servicewebapi.data.AppResultBuilder;
import org.youbai.opentcs.kernel.extensions.servicewebapi.data.ResultCode;
import org.youbai.opentcs.kernel.extensions.servicewebapi.rest.RestPeripheralService;
import org.youbai.opentcs.kernel.services.StandardPeripheralService;
import org.youbai.opentcs.kernel.services.StandardTCSObjectService;

import javax.inject.Inject;

public class PeripheralService implements RestPeripheralService {

    @Inject
    RemotePeripheralService remotePeripheralService;
    @Inject@StandardTCSObjectAnnotations
    StandardTCSObjectService standardTCSObjectService;
    @Override
    public AppResult<String> attachCommAdapter(String ref, PeripheralCommAdapterDescription description) {
        try {
            Location location = standardTCSObjectService.fetchObjectNotNull(Location.class,ref);
            remotePeripheralService.attachCommAdapter(location.getReference(),description);
            return AppResultBuilder.success(ResultCode.SUCCESS);
        }catch (ObjectUnknownException exception){
            return AppResultBuilder.faile(ResultCode.DATA_IS_WRONG);
        }
    }

    @Override
    public AppResult<String> disableCommAdapter(String ref) {
        try {
            Location location = standardTCSObjectService.fetchObjectNotNull(Location.class,ref);
            remotePeripheralService.disableCommAdapter(location.getReference());
            return AppResultBuilder.success(ResultCode.SUCCESS);
        }catch (ObjectUnknownException exception){
            return AppResultBuilder.faile(ResultCode.DATA_IS_WRONG);
        }
    }

    @Override
    public AppResult<String> enableCommAdapter(String ref) {
        try {
            Location location = standardTCSObjectService.fetchObjectNotNull(Location.class,ref);
            remotePeripheralService.enableCommAdapter(location.getReference());
            return AppResultBuilder.success(ResultCode.SUCCESS);
        }catch (ObjectUnknownException exception){
            return AppResultBuilder.faile(ResultCode.DATA_IS_WRONG);
        }
    }

    @Override
    public AppResult<PeripheralAttachmentInformation> fetchAttachmentInformation(String ref) {
        try {
            Location location = standardTCSObjectService.fetchObjectNotNull(Location.class,ref);
            remotePeripheralService.fetchAttachmentInformation(location.getReference());
            return AppResultBuilder.success(ResultCode.SUCCESS);
        }catch (ObjectUnknownException exception){
            return AppResultBuilder.faile(ResultCode.DATA_IS_WRONG);
        }
    }

    @Override
    public AppResult<PeripheralProcessModel> fetchProcessModel(String ref) {
        try {
            Location location = standardTCSObjectService.fetchObjectNotNull(Location.class,ref);
            remotePeripheralService.fetchProcessModel(location.getReference());
            return AppResultBuilder.success(ResultCode.SUCCESS);
        }catch (ObjectUnknownException exception){
            return AppResultBuilder.faile(ResultCode.DATA_IS_WRONG);
        }
    }

    @Override
    public AppResult<String> sendCommAdapterCommand(String ref, PeripheralAdapterCommand command) {
        try {
            Location location = standardTCSObjectService.fetchObjectNotNull(Location.class,ref);
            remotePeripheralService.sendCommAdapterCommand(location.getReference(),command);
            return AppResultBuilder.success(ResultCode.SUCCESS);
        }catch (ObjectUnknownException exception){
            return AppResultBuilder.faile(ResultCode.DATA_IS_WRONG);
        }
    }
}
