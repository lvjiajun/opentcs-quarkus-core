package org.youbai.opentcs.kernel.extensions.servicewebapi.imp;

import org.youbai.opentcs.access.KernelException;
import org.youbai.opentcs.access.rmi.services.RemoteRouterService;
import org.youbai.opentcs.kernel.extensions.servicewebapi.data.AppResult;
import org.youbai.opentcs.kernel.extensions.servicewebapi.data.AppResultBuilder;
import org.youbai.opentcs.kernel.extensions.servicewebapi.data.ResultCode;
import org.youbai.opentcs.kernel.extensions.servicewebapi.rest.RestRouterService;

import javax.inject.Inject;

public class RouterService implements RestRouterService {

    @Inject
    RemoteRouterService remoteRouterService;

    @Override
    public AppResult<String> updatePathLock(String ref, boolean locked) {
        try {
            remoteRouterService.updatePathLock(ref,locked);
            return AppResultBuilder.success(ResultCode.SUCCESS);
        }catch (RuntimeException exception){
            return AppResultBuilder.faile(ResultCode.RESULE_DATA_NONE);
        }
    }

    @Override
    public AppResult<String> updateRoutingTopology() {
        try {
            remoteRouterService.updateRoutingTopology();
            return AppResultBuilder.success(ResultCode.SUCCESS);
        }catch (RuntimeException exception){
            return AppResultBuilder.faile(ResultCode.RESULE_DATA_NONE);
        }
    }
}
