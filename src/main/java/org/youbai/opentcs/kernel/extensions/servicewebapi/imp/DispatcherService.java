package org.youbai.opentcs.kernel.extensions.servicewebapi.imp;

import org.youbai.opentcs.access.KernelRuntimeException;
import org.youbai.opentcs.access.rmi.services.RemoteDispatcherService;
import org.youbai.opentcs.data.TCSObjectReference;
import org.youbai.opentcs.data.model.TCSResource;
import org.youbai.opentcs.data.model.Vehicle;
import org.youbai.opentcs.data.order.TransportOrder;
import org.youbai.opentcs.kernel.annotations.StandardTransportOrderServiceAnnotations;
import org.youbai.opentcs.kernel.annotations.StandardVehicleServiceAnnotations;
import org.youbai.opentcs.kernel.extensions.servicewebapi.data.AppResultBuilder;
import org.youbai.opentcs.kernel.extensions.servicewebapi.data.ResultCode;
import org.youbai.opentcs.kernel.extensions.servicewebapi.rest.RestDispatcherService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.youbai.opentcs.kernel.extensions.servicewebapi.data.AppResult;
import org.youbai.opentcs.kernel.services.StandardTransportOrderService;
import org.youbai.opentcs.kernel.services.StandardVehicleService;

@ApplicationScoped
public class DispatcherService implements RestDispatcherService {

    @Inject
    RemoteDispatcherService remoteDispatcherService;



    @Override
    public AppResult<String> withdrawByVehicle(String ref, boolean immediateAbort) {
        try {
            remoteDispatcherService.withdrawByVehicle(new TCSObjectReference<>(Vehicle.class,ref),immediateAbort);
            return AppResultBuilder.success(ResultCode.SUCCESS);
        }catch (KernelRuntimeException exception){
            return AppResultBuilder.success(ResultCode.RESULE_DATA_NONE);
        }
    }
    @Override
    public AppResult<String> withdrawByTransportOrder(String ref, boolean immediateAbort) {
        try {
            remoteDispatcherService.withdrawByTransportOrder(new TCSObjectReference<>(TransportOrder.class,ref),immediateAbort);
            return AppResultBuilder.success(ResultCode.SUCCESS);
        }catch (KernelRuntimeException exception){
            return AppResultBuilder.success(ResultCode.RESULE_DATA_NONE);
        }
    }
}
