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
            remotePeripheralService.attachCommAdapter(ref,description);
            return AppResultBuilder.success(ResultCode.SUCCESS);
        }catch (ObjectUnknownException exception){
            return AppResultBuilder.faile(ResultCode.DATA_IS_WRONG);
        }
    }

    @Override
    public AppResult<String> disableCommAdapter(String ref) {
        try {
            remotePeripheralService.disableCommAdapter(ref);
            return AppResultBuilder.success(ResultCode.SUCCESS);
        }catch (ObjectUnknownException exception){
            return AppResultBuilder.faile(ResultCode.DATA_IS_WRONG);
        }
    }

    @Override
    public AppResult<String> enableCommAdapter(String ref) {
        try {
            remotePeripheralService.enableCommAdapter(ref);
            return AppResultBuilder.success(ResultCode.SUCCESS);
        }catch (ObjectUnknownException exception){
            return AppResultBuilder.faile(ResultCode.DATA_IS_WRONG);
        }
    }

    @Override
    public AppResult<PeripheralAttachmentInformation> fetchAttachmentInformation(String ref) {
        try {
            PeripheralAttachmentInformation peripheralAttachmentInformation
                    = remotePeripheralService.fetchAttachmentInformation(ref);
            return AppResultBuilder.success(peripheralAttachmentInformation,ResultCode.SUCCESS);
        }catch (ObjectUnknownException exception){
            return AppResultBuilder.faile(ResultCode.DATA_IS_WRONG);
        }
    }

    @Override
    public AppResult<PeripheralProcessModel> fetchProcessModel(String ref) {
        try {
            PeripheralProcessModel peripheralProcessModel
                    = remotePeripheralService.fetchProcessModel(ref);
            return AppResultBuilder.success(peripheralProcessModel,ResultCode.SUCCESS);
        }catch (ObjectUnknownException exception){
            return AppResultBuilder.faile(ResultCode.DATA_IS_WRONG);
        }
    }

    @Override
    public AppResult<String> sendCommAdapterCommand(String ref, PeripheralAdapterCommand command) {
        try {
            remotePeripheralService.sendCommAdapterCommand(ref,command);
            return AppResultBuilder.success(ResultCode.SUCCESS);
        }catch (ObjectUnknownException exception){
            return AppResultBuilder.faile(ResultCode.DATA_IS_WRONG);
        }
    }
}
