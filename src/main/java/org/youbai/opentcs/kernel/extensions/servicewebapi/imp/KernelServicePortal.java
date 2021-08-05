package org.youbai.opentcs.kernel.extensions.servicewebapi.imp;

import org.youbai.opentcs.access.Kernel;
import org.youbai.opentcs.access.rmi.services.RemoteKernelServicePortal;
import org.youbai.opentcs.kernel.extensions.servicewebapi.data.AppResult;
import org.youbai.opentcs.kernel.extensions.servicewebapi.data.AppResultBuilder;
import org.youbai.opentcs.kernel.extensions.servicewebapi.data.ResultCode;
import org.youbai.opentcs.kernel.extensions.servicewebapi.rest.RestKernelServicePortal;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
@ApplicationScoped
public class KernelServicePortal implements RestKernelServicePortal {


    @Inject
    RemoteKernelServicePortal remoteKernelServicePortal;

    @Override
    public AppResult<Kernel.State> getState() {
        return AppResultBuilder
                .success(remoteKernelServicePortal
                        .getState(), ResultCode.SUCCESS);
    }

    @Override
    public AppResult<String> publishEvent(Object event) {
        remoteKernelServicePortal.publishEvent(event);
        return AppResultBuilder.success(ResultCode.SUCCESS);
    }
}
