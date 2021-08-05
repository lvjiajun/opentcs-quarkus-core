package org.youbai.opentcs.kernel.extensions.servicewebapi.imp;

import org.youbai.opentcs.kernel.extensions.servicewebapi.data.AppResult;
import org.youbai.opentcs.kernel.extensions.servicewebapi.rest.RestRouterService;

public class RouterService implements RestRouterService {
    @Override
    public AppResult<String> updatePathLock(String ref, boolean locked) {
        return null;
    }

    @Override
    public AppResult<String> updateRoutingTopology() {
        return null;
    }
}
