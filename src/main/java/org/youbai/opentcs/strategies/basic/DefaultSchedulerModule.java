package org.youbai.opentcs.strategies.basic;

import org.youbai.opentcs.components.kernel.Scheduler;
import org.youbai.opentcs.strategies.basic.scheduling.DefaultScheduler;
import org.youbai.opentcs.strategies.basic.scheduling.DummyScheduler;
import org.youbai.opentcs.strategies.basic.scheduling.SchedulerAnnotation;

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

    @Produces
    @SchedulerAnnotation
    @ApplicationScoped
    Scheduler SchedulerServiceProduces(){
        return defaultScheduler;
    }
}
