package org.youbai.opentcs.kernel;

import org.youbai.opentcs.access.Kernel;
import org.youbai.opentcs.util.logging.UncaughtExceptionLogger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;

@ApplicationScoped
public class DefaultKernelInjectionModule {

    @Inject
    KernelStateModelling kernelStateModelling;
    @Inject
    KernelStateOperating kernelStateOperating;
    @Inject
    KernelStateShutdown kernelStateShutdown;


    @Produces
    public ScheduledExecutorService kernelExecutor() {
        return new org.youbai.opentcs.common.LoggingScheduledThreadPoolExecutor(
                1,
                (runnable) -> {
                    Thread thread = new Thread(runnable, "kernelExecutor");
                    thread.setUncaughtExceptionHandler(new UncaughtExceptionLogger(false));
                    return thread;
                }
        );
    }
    @Produces
    public File directory(){
        return new File(System.getProperty("opentcs.home", "."));
    }


    @Produces
    public Map<Kernel.State, KernelState> stateMapBinder(){
        ConcurrentHashMap<Kernel.State, KernelState> comparatorConcurrentHashMap = new ConcurrentHashMap<>();
        comparatorConcurrentHashMap.put(Kernel.State.SHUTDOWN,kernelStateShutdown);
        comparatorConcurrentHashMap.put(Kernel.State.MODELLING,kernelStateModelling);
        comparatorConcurrentHashMap.put(Kernel.State.OPERATING,kernelStateOperating);
        return comparatorConcurrentHashMap;
    }
}
