/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.kernel;

import com.google.common.util.concurrent.Uninterruptibles;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import static java.util.Objects.requireNonNull;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;


import io.quarkus.runtime.Startup;
import org.youbai.opentcs.access.Kernel;
import org.youbai.opentcs.access.KernelStateTransitionEvent;
import org.youbai.opentcs.access.LocalKernel;
import org.youbai.opentcs.access.ModelTransitionEvent;
import org.youbai.opentcs.components.kernel.KernelExtension;
import org.youbai.opentcs.components.kernel.services.NotificationService;
import org.youbai.opentcs.data.notification.UserNotification;
import org.youbai.opentcs.kernel.annotations.*;
import org.youbai.opentcs.kernel.extensions.rmi.KernelRemoteService;
import org.youbai.opentcs.util.event.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements the standard openTCS kernel.
 * <hr>
 * <h4>Configuration entries</h4>
 * <dl>
 * <dt><b>messageBufferCapacity:</b></dt>
 * <dd>An integer defining the maximum number of messages to be kept in the
 * kernel's message buffer (default: 500).</dd>
 * </dl>
 * <hr>
 *
 * @author Stefan Walter (Fraunhofer IML)
 */

@StandardKernelAnnotations
@Startup
@Singleton
public class StandardKernel
    implements LocalKernel,
               Runnable {

  /**
   * This class's Logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(StandardKernel.class);
  /**
   * Message for UnsupportedKernelOpExceptions thrown in user management
   * methods.
   */
  private static final String MSG_NO_USER_MANAGEMENT = "No user management in local kernel";
  /**
   * The application's event bus.
   */
  @Inject@SimpleEventBusAnnotation
  EventBus eventBus;
  /**
   * Our executor.
   */
  @Inject@ScheduledExecutorServiceAnnotations
  ScheduledExecutorService kernelExecutor;
  /**
   * This kernel's order receivers.
   */
  private final Set<KernelExtension> kernelExtensions = new HashSet<>();
  /**
   * Functions as a barrier for the kernel's {@link #run() run()} method.
   */
  private final Semaphore terminationSemaphore = new Semaphore(0);
  /**
   * The notification service.
   */
  @Inject@StandardNotificationServiceAnnotation
  NotificationService notificationService;
  /**
   * This kernel's <em>initialized</em> flag.
   */
  private volatile boolean initialized;
  /**
   * The kernel implementing the actual functionality for the current mode.
   */
  private KernelState kernelState;

  @Inject
  KernelStateOperating kernelStateOperating;
  @Inject
  KernelStateModelling kernelStateModelling;
  @Inject
  KernelStateShutdown kernelStateShutdown;

  /**
   * A map to state providers used when switching kernel states.
   */
  /**
   * Creates a new kernel.
   */

  StandardKernel() {
  }

  @Override
  public void initialize() {
    if (isInitialized()) {
      return;
    }
    // First of all, start all kernel extensions that are already registered.
    for (KernelExtension extension : kernelExtensions) {
      LOG.debug("Initializing extension: {}", extension.getClass().getName());
      extension.initialize();
    }

    // Initial state is modelling.
    setState(State.MODELLING);

    initialized = true;
    LOG.debug("Starting kernel thread");
    Thread kernelThread = new Thread(this, "kernelThread");
    kernelThread.start();
  }

  @Override
  public boolean isInitialized() {
    return initialized;
  }

  @Override
  public void terminate() {
    if (!isInitialized()) {
      return;
    }
    // Note that the actual shutdown of extensions should happen when the kernel
    // thread (see run()) finishes, not here.
    // Set the terminated flag and wake up this kernel's thread for termination.
    initialized = false;
    terminationSemaphore.release();
  }

  @Override
  public void run() {
    // Wait until terminated.
    terminationSemaphore.acquireUninterruptibly();
    LOG.info("Terminating...");
    // Sleep a bit so clients have some time to receive an event for the
    // SHUTDOWN state change and shut down gracefully themselves.
    Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
    // Shut down all kernel extensions.
    LOG.debug("Shutting down kernel extensions...");
    for (KernelExtension extension : kernelExtensions) {
      extension.terminate();
    }
    kernelExecutor.shutdown();
    LOG.info("Kernel thread finished.");
  }

  @Override
  public State getState() {
    LOG.debug("method entry");
    return kernelState.getState();
  }

  @Override
  public void setState(State newState)
      throws IllegalArgumentException {
    Objects.requireNonNull(newState, "newState is null");
    final State oldState;
    if (kernelState != null) {
      oldState = kernelState.getState();
      // Don't do anything if the new state is the same as the current one.
      if (oldState == newState) {
        LOG.debug("Already in state '{}', doing nothing.", newState.name());
        return;
      }
      // Let listeners know we're in transition.
      emitStateEvent(oldState, newState, false);
      // Terminate previous state.
      kernelState.terminate();
    }
    else {
      oldState = null;
    }
    LOG.info("Switching kernel to state '{}'", newState.name());
    switch (newState) {
      case SHUTDOWN:
        kernelState = kernelStateShutdown;
        kernelState.initialize();
        terminate();
        break;
      case MODELLING:
        kernelState = kernelStateModelling;
        kernelState.initialize();
        break;
      case OPERATING:
        kernelState = kernelStateOperating;
        kernelState.initialize();
        break;
      default:
        throw new IllegalArgumentException("Unexpected state: " + newState);
    }
    emitStateEvent(oldState, newState, true);
    notificationService.publishUserNotification(new UserNotification("Kernel is now in state " + newState,
                                                                     UserNotification.Level.INFORMATIONAL));
  }

  @Override
  public void addKernelExtension(final KernelExtension newExtension) {
    LOG.debug("method entry");
    Objects.requireNonNull(newExtension, "newExtension is null");
    kernelExtensions.add(newExtension);
  }

  @Override
  public void removeKernelExtension(final KernelExtension rmExtension) {
    LOG.debug("method entry");
    Objects.requireNonNull(rmExtension, "rmExtension is null");
    kernelExtensions.remove(rmExtension);
  }

  // Methods not declared in any interface start here.
  /**
   * Generates an event for a state change.
   *
   * @param leftState The state left.
   * @param enteredState The state entered.
   * @param transitionFinished Whether the transition is finished or not.
   */
  private void emitStateEvent(State leftState,
                              State enteredState,
                              boolean transitionFinished) {
    assert enteredState != null;

    eventBus.onEvent(new KernelStateTransitionEvent(leftState, enteredState, transitionFinished));
  }

  /**
   * Generates an event for a Model change.
   *
   * @param oldModelName The state left.
   * @param enteredModelName The state entered.
   * @param modelContentChanged Whether the model's content actually changed.
   * @param transitionFinished Whether the transition is finished or not.
   */
  private void emitModelEvent(String oldModelName,
                              String enteredModelName,
                              boolean modelContentChanged,
                              boolean transitionFinished) {
    assert enteredModelName != null;

    eventBus.onEvent(new ModelTransitionEvent(oldModelName,
                                              enteredModelName,
                                              modelContentChanged,
                                              transitionFinished));
  }
}
