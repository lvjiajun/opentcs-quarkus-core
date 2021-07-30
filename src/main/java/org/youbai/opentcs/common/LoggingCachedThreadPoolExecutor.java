package org.youbai.opentcs.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class LoggingCachedThreadPoolExecutor extends ThreadPoolExecutor {

    private static final Logger LOG
            = LoggerFactory.getLogger(LoggingCachedThreadPoolExecutor.class);

    public LoggingCachedThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        if (t == null && r instanceof Future<?>) {
            try {
                Future<?> future = (Future<?>) r;
                if (future.isDone()) {
                    future.get();
                }
                else if (isPeriodic(future)) {
                    // Periodic futures will never be done
                    return;
                }
                else {
                    LOG.debug("Future was not done: {}", future);
                }
            }
            catch (ExecutionException ee) {
                LOG.warn("Unhandled exception in executed task", ee.getCause());
            }
            catch (CancellationException ce) {
                LOG.debug("Task was cancelled", ce);
            }
            catch (InterruptedException ie) {
                LOG.debug("Interrupted during Future.get()", ie);
                // Ignore/Reset
                Thread.currentThread().interrupt();
            }
        }
        if (t != null) {
            LOG.error("Abrupt termination", t);
        }
    }

    private boolean isPeriodic(Future<?> future) {
        if (future instanceof RunnableScheduledFuture<?>) {
            RunnableScheduledFuture<?> runnableFuture = (RunnableScheduledFuture<?>) future;
            if (runnableFuture.isPeriodic()) {
                return true;
            }
        }
        return false;
    }
}
