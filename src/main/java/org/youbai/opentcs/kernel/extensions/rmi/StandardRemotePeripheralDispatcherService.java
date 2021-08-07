/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.kernel.extensions.rmi;

import java.rmi.registry.Registry;

import static java.util.Objects.requireNonNull;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.youbai.opentcs.access.rmi.factories.SocketFactoryProvider;
import org.youbai.opentcs.access.rmi.services.RegistrationName;
import org.youbai.opentcs.access.rmi.services.RemotePeripheralDispatcherService;
import org.youbai.opentcs.components.kernel.services.PeripheralDispatcherService;
import org.youbai.opentcs.data.model.Location;
import org.youbai.opentcs.data.model.TCSResourceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.youbai.opentcs.kernel.annotations.ExecutorServiceAnnotations;
import org.youbai.opentcs.kernel.annotations.StandardPeripheralDispatcherServiceAnnotations;

/**
 * This class is the standard implementation of the {@link RemotePeripheralDispatcherService}
 * interface.
 * <p>
 * Upon creation, an instance of this class registers itself with the RMI registry by the name
 * declared by {@link RegistrationName#REMOTE_PERIPHERAL_DISPATCHER_SERVICE}.
 * </p>
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
@ApplicationScoped
public class StandardRemotePeripheralDispatcherService
    extends KernelRemoteService
    implements RemotePeripheralDispatcherService {

  /**
   * This class's logger.
   */
  private static final Logger LOG
      = LoggerFactory.getLogger(StandardRemotePeripheralDispatcherService.class);
  /**
   * The peripheral dispatcher service to invoke methods on.
   */
  @Inject
  @StandardPeripheralDispatcherServiceAnnotations
  PeripheralDispatcherService dispatcherService;
  /**
   * Executes tasks modifying kernel data.
   */
  @Inject
  @ExecutorServiceAnnotations
  ExecutorService kernelExecutor;


  public StandardRemotePeripheralDispatcherService() {
  }

  @Override
  public void dispatch() {
    try {
      kernelExecutor.submit(() -> dispatcherService.dispatch()).get();
    }
    catch (InterruptedException | ExecutionException exc) {
      throw findSuitableExceptionFor(exc);
    }
  }

  @Override
  public void withdrawByLocation(TCSResourceReference<Location> ref) {
    try {
      kernelExecutor.submit(() -> dispatcherService.withdrawByLocation(ref)).get();
    }
    catch (InterruptedException | ExecutionException exc) {
      throw findSuitableExceptionFor(exc);
    }
  }

  @Override
  public void withdrawByLocation(String ref) {
    try {
      kernelExecutor.submit(() -> dispatcherService.withdrawByLocation(ref)).get();
    }
    catch (InterruptedException | ExecutionException exc) {
      throw findSuitableExceptionFor(exc);
    }
  }
}
