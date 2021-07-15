package org.youbai.opentcs.strategies.basic;

import org.youbai.opentcs.components.kernel.Scheduler;
import org.youbai.opentcs.strategies.basic.scheduling.DefaultScheduler;
import org.youbai.opentcs.strategies.basic.scheduling.DummyScheduler;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

@ApplicationScoped
public class DefaultSchedulerModule {

    @Inject
    DefaultScheduler defaultScheduler;
    @Inject
    DummyScheduler dummyScheduler;

    @Produces@Named("bindScheduler")@ApplicationScoped
    Scheduler bindScheduler(){
        return defaultScheduler;
    }
}
