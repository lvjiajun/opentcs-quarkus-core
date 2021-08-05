package org.youbai.opentcs.kernel.extensions.servicewebapi.imp;

import org.youbai.opentcs.access.rmi.services.RemotePeripheralDispatcherService;
import org.youbai.opentcs.data.TCSObjectReference;
import org.youbai.opentcs.data.model.Location;
import org.youbai.opentcs.kernel.annotations.StandardTCSObjectAnnotations;
import org.youbai.opentcs.kernel.extensions.servicewebapi.data.AppResult;
import org.youbai.opentcs.kernel.extensions.servicewebapi.data.AppResultBuilder;
import org.youbai.opentcs.kernel.extensions.servicewebapi.data.ResultCode;
import org.youbai.opentcs.kernel.extensions.servicewebapi.rest.RestPeripheralDispatcherService;
import org.youbai.opentcs.kernel.services.StandardPeripheralDispatcherService;
import org.youbai.opentcs.kernel.services.StandardTCSObjectService;

import javax.inject.Inject;

public class PeripheralDispatcherService implements RestPeripheralDispatcherService {

    @Inject
    RemotePeripheralDispatcherService remotePeripheralDispatcherService;

    @Inject@StandardTCSObjectAnnotations
    StandardTCSObjectService standardTCSObjectService;

    @Override
    public AppResult<String> withdrawByLocation(String ref) {
        Location location = standardTCSObjectService.fetchObjectNotNull(Location.class,ref);
        remotePeripheralDispatcherService.withdrawByLocation(location.getReference());
        return AppResultBuilder.success(ResultCode.SUCCESS);
    }
}
