package org.youbai.opentcs.kernel;

import io.quarkus.arc.impl.BeanManagerImpl;
import io.quarkus.runtime.Startup;
import org.youbai.opentcs.access.Kernel;
import org.youbai.opentcs.access.SslParameterSet;
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

    @Inject
    SslConfiguration sslconfiguration;

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
        return new File(System.getProperty("opentcs.home", ""));
    }

    @Produces
    public SslParameterSet sslParamSet(){
        return new SslParameterSet(SslParameterSet.DEFAULT_KEYSTORE_TYPE,
                new File(sslconfiguration.keystoreFile()),
                sslconfiguration.keystorePassword(),
                new File(sslconfiguration.truststoreFile()),
                sslconfiguration.truststorePassword());
    }
}
