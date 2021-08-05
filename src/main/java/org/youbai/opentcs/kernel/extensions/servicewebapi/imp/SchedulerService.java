package org.youbai.opentcs.kernel.extensions.servicewebapi.imp;

import org.youbai.opentcs.access.SchedulerAllocationState;
import org.youbai.opentcs.access.rmi.services.RemoteSchedulerService;
import org.youbai.opentcs.kernel.extensions.servicewebapi.data.AppResult;
import org.youbai.opentcs.kernel.extensions.servicewebapi.data.AppResultBuilder;
import org.youbai.opentcs.kernel.extensions.servicewebapi.data.ResultCode;
import org.youbai.opentcs.kernel.extensions.servicewebapi.rest.RestSchedulerService;

import javax.inject.Inject;

public class SchedulerService implements RestSchedulerService {

    @Inject
    RemoteSchedulerService remoteSchedulerService;

    @Override
    public AppResult<SchedulerAllocationState> fetchSchedulerAllocations() {
        SchedulerAllocationState schedulerAllocationState
                =  remoteSchedulerService.fetchSchedulerAllocations();
        return AppResultBuilder
                .success(schedulerAllocationState, ResultCode.SUCCESS);
    }
}
