package org.youbai.opentcs.kernel;

import org.youbai.opentcs.util.logging.UncaughtExceptionLogger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

import java.io.File;
import java.util.concurrent.ScheduledExecutorService;

@ApplicationScoped
public class DefaultKernelInjectionModule {

    @Produces
    @Singleton
    public Object getGlobalSyncObject()
    {
        return new Object();
    }


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
}
