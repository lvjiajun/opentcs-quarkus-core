package org.youbai.opentcs.kernel;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GlobalSyncObject {

    private final Object GlobalSynchronized = new Object();


    public GlobalSyncObject() {
    }

}
