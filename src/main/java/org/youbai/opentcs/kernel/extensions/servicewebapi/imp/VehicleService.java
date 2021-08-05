package org.youbai.opentcs.kernel.extensions.servicewebapi.imp;

import org.youbai.opentcs.access.KernelRuntimeException;
import org.youbai.opentcs.access.rmi.services.RemoteVehicleService;
import org.youbai.opentcs.data.TCSObjectReference;
import org.youbai.opentcs.data.model.Vehicle;
import org.youbai.opentcs.drivers.vehicle.AdapterCommand;
import org.youbai.opentcs.drivers.vehicle.VehicleCommAdapterDescription;
import org.youbai.opentcs.drivers.vehicle.management.AttachmentInformation;
import org.youbai.opentcs.drivers.vehicle.management.VehicleProcessModelTO;
import org.youbai.opentcs.kernel.annotations.StandardVehicleServiceAnnotations;
import org.youbai.opentcs.kernel.extensions.servicewebapi.data.AppResult;
import org.youbai.opentcs.kernel.extensions.servicewebapi.data.AppResultBuilder;
import org.youbai.opentcs.kernel.extensions.servicewebapi.data.ResultCode;
import org.youbai.opentcs.kernel.extensions.servicewebapi.rest.RestVehicleService;
import org.youbai.opentcs.kernel.services.StandardVehicleService;

import javax.inject.Inject;
import java.util.Set;

public class VehicleService implements RestVehicleService {


    @Inject@StandardVehicleServiceAnnotations
    StandardVehicleService standardVehicleService;

    @Inject
    RemoteVehicleService remoteVehicleService;


    @Override
    public AppResult<String> attachCommAdapter(String ref, VehicleCommAdapterDescription description) {
        try {
            Vehicle vehicle = standardVehicleService.fetchObjectNotNull(Vehicle.class,ref);
            remoteVehicleService.attachCommAdapter(vehicle.getReference(),description);
            return AppResultBuilder.success(ResultCode.SUCCESS);
        }catch (KernelRuntimeException exception){
            return AppResultBuilder.faile(ResultCode.DATA_IS_WRONG);
        }
    }

    @Override
    public AppResult<String> disableCommAdapter(String ref) {
        try {
            Vehicle vehicle = standardVehicleService.fetchObjectNotNull(Vehicle.class,ref);
            remoteVehicleService.disableCommAdapter(vehicle.getReference());
            return AppResultBuilder.success(ResultCode.SUCCESS);
        }catch (KernelRuntimeException exception){
            return AppResultBuilder.faile(ResultCode.DATA_IS_WRONG);
        }
    }

    @Override
    public AppResult<String> enableCommAdapter(String ref) {
        try {
            Vehicle vehicle = standardVehicleService.fetchObjectNotNull(Vehicle.class,ref);
            remoteVehicleService.enableCommAdapter(vehicle.getReference());
            return AppResultBuilder.success(ResultCode.SUCCESS);
        }catch (KernelRuntimeException exception){
            return AppResultBuilder.faile(ResultCode.DATA_IS_WRONG);
        }
    }

    @Override
    public AppResult<AttachmentInformation> fetchAttachmentInformation(String ref) {
        Vehicle vehicle = standardVehicleService.fetchObject(Vehicle.class,ref);
        remoteVehicleService.fetchAttachmentInformation(vehicle.getReference());
        return AppResultBuilder.success(ResultCode.SUCCESS);
    }

    @Override
    public AppResult<VehicleProcessModelTO> fetchProcessModel(String ref) {
        Vehicle vehicle = standardVehicleService.fetchObject(Vehicle.class,ref);
        remoteVehicleService.fetchProcessModel(vehicle.getReference());
        return AppResultBuilder.success(ResultCode.SUCCESS);
    }

    @Override
    public AppResult<String> sendCommAdapterCommand(String ref, AdapterCommand command) {
        try {
            Vehicle vehicle = standardVehicleService.fetchObjectNotNull(Vehicle.class,ref);
            remoteVehicleService.sendCommAdapterCommand(vehicle.getReference(),command);
            return AppResultBuilder.success(ResultCode.SUCCESS);
        }catch (KernelRuntimeException exception){
            return AppResultBuilder.faile(ResultCode.DATA_IS_WRONG);
        }
    }

    @Override
    public AppResult<String> sendCommAdapterMessage(String ref, Object message) {
        try {
            Vehicle vehicle = standardVehicleService.fetchObjectNotNull(Vehicle.class,ref);
            remoteVehicleService.sendCommAdapterMessage(vehicle.getReference(),message);
            return AppResultBuilder.success(ResultCode.SUCCESS);
        }catch (KernelRuntimeException exception){
            return AppResultBuilder.faile(ResultCode.DATA_IS_WRONG);
        }
    }

    @Override
    public AppResult<String> updateVehicleIntegrationLevel(String ref, Vehicle.IntegrationLevel integrationLevel) {
        try {
            Vehicle vehicle = standardVehicleService.fetchObjectNotNull(Vehicle.class,ref);
            remoteVehicleService.updateVehicleIntegrationLevel(vehicle.getReference(),integrationLevel);
            return AppResultBuilder.success(ResultCode.SUCCESS);
        }catch (KernelRuntimeException exception){
            return AppResultBuilder.faile(ResultCode.DATA_IS_WRONG);
        }
    }

    @Override
    public AppResult<String> updateVehicleAllowedOrderTypes(String ref, Set<String> allowedOrderTypes) {
        try {
            Vehicle vehicle = standardVehicleService.fetchObjectNotNull(Vehicle.class,ref);
            remoteVehicleService.updateVehicleAllowedOrderTypes(vehicle.getReference(),allowedOrderTypes);
            return AppResultBuilder.success(ResultCode.SUCCESS);
        }catch (KernelRuntimeException exception){
            return AppResultBuilder.faile(ResultCode.DATA_IS_WRONG);
        }
    }
}
