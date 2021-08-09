/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.kernel.extensions.rmi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.youbai.opentcs.access.Kernel;
import org.youbai.opentcs.access.KernelRuntimeException;
import org.youbai.opentcs.access.LocalKernel;
import org.youbai.opentcs.access.rmi.factories.SocketFactoryProvider;
import org.youbai.opentcs.access.rmi.services.RegistrationName;
import org.youbai.opentcs.access.rmi.services.RemoteKernelServicePortal;
import org.youbai.opentcs.components.kernel.KernelExtension;
import org.youbai.opentcs.kernel.annotations.SimpleEventBusAnnotation;
import org.youbai.opentcs.kernel.annotations.StandardKernelAnnotations;
import org.youbai.opentcs.util.event.EventHandler;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Named;
import javax.inject.Singleton;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import static java.util.Objects.requireNonNull;

/**
 * This class is the standard implementation of the {@link RemoteKernelServicePortal} interface.
 * <p>
 * Upon creation, an instance of this class registers itself with the RMI registry by the name
 * </p>
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
@ApplicationScoped
public class StandardRemoteKernelClientPortal
    implements RemoteKernelServicePortal{

  /**
   * This class' logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(StandardRemoteKernelClientPortal.class);
  /**
   * The kernel.
   */
  private final Kernel kernel;
  /**
   * The event handler to publish events to.
   */
  private final EventHandler eventHandler;

  /**
   * Creates a new instance.
   *
   * @param kernel The kernel.
   * @param eventHandler The event handler to publish events to.
   */

  public StandardRemoteKernelClientPortal(@StandardKernelAnnotations LocalKernel kernel,
                                          @SimpleEventBusAnnotation EventHandler eventHandler) {
    this.kernel = requireNonNull(kernel, "kernel");
    this.eventHandler = requireNonNull(eventHandler, "eventHandler");
  }

  @Override
  public Kernel.State getState() {

    return kernel.getState();
  }
  @Override
  public void publishEvent(Object event)
      throws KernelRuntimeException {
    eventHandler.onEvent(event);
  }
}
