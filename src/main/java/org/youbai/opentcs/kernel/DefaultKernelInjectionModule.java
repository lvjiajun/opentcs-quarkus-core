package org.youbai.opentcs.kernel;

import org.youbai.opentcs.access.Kernel;
import org.youbai.opentcs.access.KernelException;
import org.youbai.opentcs.components.kernel.KernelExtension;
import org.youbai.opentcs.util.logging.UncaughtExceptionLogger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;

@ApplicationScoped
public class DefaultKernelInjectionModule {

    @Inject
    KernelStateShutdown kernelStateShutdown;
    @Inject
    KernelStateModelling kernelStateModelling;
    @Inject
    KernelStateOperating kernelStateOperating;



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


    public Map<Kernel.State, KernelState> stateProviders(){
        Map<Kernel.State,KernelState> stateProviders = new ConcurrentHashMap();
        stateProviders.put(Kernel.State.SHUTDOWN,this.kernelStateShutdown);
        stateProviders.put(Kernel.State.MODELLING,this.kernelStateModelling);
        stateProviders.put(Kernel.State.OPERATING,this.kernelStateOperating);
        return stateProviders;
    }
}
