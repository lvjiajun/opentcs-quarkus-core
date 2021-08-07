package org.youbai.opentcs.kernel.extensions.servicewebapi.imp;

import org.youbai.opentcs.access.rmi.services.RemotePeripheralJobService;
import org.youbai.opentcs.access.to.peripherals.PeripheralJobCreationTO;
import org.youbai.opentcs.data.peripherals.PeripheralJob;
import org.youbai.opentcs.kernel.extensions.servicewebapi.data.AppResult;
import org.youbai.opentcs.kernel.extensions.servicewebapi.data.AppResultBuilder;
import org.youbai.opentcs.kernel.extensions.servicewebapi.data.ResultCode;
import org.youbai.opentcs.kernel.extensions.servicewebapi.rest.RestPeripheralJobService;

import javax.inject.Inject;

public class PeripheralJobService implements RestPeripheralJobService {

    @Inject
    RemotePeripheralJobService remotePeripheralJobService;

    @Override
    public AppResult<PeripheralJob> createPeripheralJob(PeripheralJobCreationTO to) {
        try {
            PeripheralJob peripheralJob = remotePeripheralJobService.createPeripheralJob(to);
            return AppResultBuilder.success(peripheralJob, ResultCode.SUCCESS);
        }catch (RuntimeException exception){
            return AppResultBuilder.faile(ResultCode.RESULE_DATA_NONE);
        }
    }
}
