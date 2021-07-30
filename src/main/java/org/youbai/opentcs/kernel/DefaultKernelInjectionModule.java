package org.youbai.opentcs.kernel;

import org.youbai.opentcs.access.SslParameterSet;
import org.youbai.opentcs.common.LoggingScheduledThreadPoolExecutor;
import org.youbai.opentcs.common.LoggingCachedThreadPoolExecutor;
import org.youbai.opentcs.kernel.annotations.ExecutorServiceAnnotations;
import org.youbai.opentcs.kernel.annotations.ScheduledExecutorServiceAnnotations;
import org.youbai.opentcs.util.logging.UncaughtExceptionLogger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class DefaultKernelInjectionModule {

    @Inject
    SslConfiguration sslconfiguration;

    @Inject
    KernelApplicationConfiguration kernelApplicationConfiguration;

    @Produces
    @ScheduledExecutorServiceAnnotations
    public ScheduledExecutorService ScheduledExecutorServiceProduces() {
        return new LoggingScheduledThreadPoolExecutor(
                kernelApplicationConfiguration.corePoolSize(),
                (runnable) -> {
                    Thread thread = new Thread(runnable, "kernelScheduledExecutor");
                    thread.setUncaughtExceptionHandler(new UncaughtExceptionLogger(false));
                    return thread;
                }
        );
    }

    @Produces
    @ExecutorServiceAnnotations
    public ExecutorService ExecutorServiceProduces() {
        return new LoggingCachedThreadPoolExecutor(
                kernelApplicationConfiguration.corePoolSize(),
                kernelApplicationConfiguration.maximumPoolSize(),
                kernelApplicationConfiguration.keepAliveTime(),
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(),
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
