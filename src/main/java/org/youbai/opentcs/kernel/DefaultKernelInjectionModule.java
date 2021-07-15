package org.youbai.opentcs.kernel;

import io.quarkus.arc.impl.BeanManagerImpl;
import io.quarkus.runtime.Startup;
import org.youbai.opentcs.access.Kernel;
import org.youbai.opentcs.util.logging.UncaughtExceptionLogger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;

@ApplicationScoped
public class DefaultKernelInjectionModule {

    @Produces
    @Named("ExecutorService")
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
