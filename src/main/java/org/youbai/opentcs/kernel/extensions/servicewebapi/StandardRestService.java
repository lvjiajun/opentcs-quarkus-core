package org.youbai.opentcs.kernel.extensions.servicewebapi;

import org.youbai.opentcs.access.KernelRuntimeException;

import java.util.concurrent.ExecutionException;

public abstract class StandardRestService {

    /**
     * The message to log when a service method execution failed.
     */
    private static final String EXECUTION_FAILED_MESSAGE = "Failed to execute service method";

    /**
     * Wraps the given exception into a suitable {@link RuntimeException}.
     *
     * @param exc The exception to find a runtime exception for.
     * @return The runtime exception.
     */
    protected RuntimeException findSuitableExceptionFor(Exception exc) {
        if (exc instanceof InterruptedException) {
            return new IllegalStateException("Unexpectedly interrupted");
        }
        if (exc instanceof ExecutionException
                && exc.getCause() instanceof RuntimeException) {
            return (RuntimeException) exc.getCause();
        }
        return new KernelRuntimeException(EXECUTION_FAILED_MESSAGE, exc.getCause());
    }
}
