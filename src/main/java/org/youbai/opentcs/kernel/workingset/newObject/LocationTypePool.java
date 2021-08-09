package org.youbai.opentcs.kernel.workingset.newObject;

import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.youbai.opentcs.data.TCSObject;
import org.youbai.opentcs.util.event.EventHandler;

public class LocationTypePool extends TCSResourcePool{

    /**
     * Creates a new instance that uses the given event handler.
     *
     * @param eventHandler
     * @param objectsByName
     */
    public LocationTypePool(EventHandler eventHandler, RMap<String, TCSObject<?>> objectsByName) {
        super(eventHandler, objectsByName);
    }
}
