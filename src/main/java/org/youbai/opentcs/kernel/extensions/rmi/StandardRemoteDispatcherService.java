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
import javax.inject.Named;

import org.youbai.opentcs.access.rmi.factories.SocketFactoryProvider;
import org.youbai.opentcs.access.rmi.services.RemoteDispatcherService;
import org.youbai.opentcs.components.kernel.services.DispatcherService;
import org.youbai.opentcs.data.TCSObjectReference;
import org.youbai.opentcs.data.model.Vehicle;
import org.youbai.opentcs.data.order.TransportOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.youbai.opentcs.kernel.annotations.ExecutorServiceAnnotations;
import org.youbai.opentcs.kernel.annotations.StandardDispatcherServiceAnnotations;

/**
 * This class is the standard implementation of the {@link RemoteDispatcherService} interface.
 * <p>
 * Upon creation, an instance of this class registers itself with the RMI registry by the name
 * </p>
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
@ApplicationScoped
public class StandardRemoteDispatcherService
    extends KernelRemoteService
    implements RemoteDispatcherService {

  /**
   * This class's logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(StandardRemoteDispatcherService.class);
  /**
   * The dispatcher service to invoke methods on.
   */
  private final DispatcherService dispatcherService;
  /**
   * Executes tasks modifying kernel data.
   */
  private final ExecutorService kernelExecutor;

  /**
   * Creates a new instance.
   *
   * @param dispatcherService The dispatcher service.
   * @param kernelExecutor Executes tasks modifying kernel data.
   */
  public StandardRemoteDispatcherService(@StandardDispatcherServiceAnnotations DispatcherService dispatcherService,
                                         @ExecutorServiceAnnotations ExecutorService kernelExecutor) {
    this.dispatcherService = requireNonNull(dispatcherService, "dispatcherService");
    this.kernelExecutor = requireNonNull(kernelExecutor, "kernelExecutor");
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
  public void withdrawByVehicle(TCSObjectReference<Vehicle> ref,
                                boolean immediateAbort) {

    try {
      kernelExecutor.submit(() -> dispatcherService.withdrawByVehicle(ref, immediateAbort))
          .get();
    }
    catch (InterruptedException | ExecutionException exc) {
      throw findSuitableExceptionFor(exc);
    }
  }

  @Override
  public void withdrawByTransportOrder(TCSObjectReference<TransportOrder> ref,
                                       boolean immediateAbort) {

    try {
      kernelExecutor.submit(() -> dispatcherService.withdrawByTransportOrder(ref, immediateAbort))
          .get();
    }
    catch (InterruptedException | ExecutionException exc) {
      throw findSuitableExceptionFor(exc);
    }
  }

  @Override
  public void withdrawByVehicle(String ref, boolean immediateAbort) {
    try {
      kernelExecutor.submit(() -> dispatcherService.withdrawByVehicle(ref, immediateAbort))
              .get();
    }
    catch (InterruptedException | ExecutionException exc) {
      throw findSuitableExceptionFor(exc);
    }
  }

  @Override
  public void withdrawByTransportOrder(String ref, boolean immediateAbort) {

    try {
      kernelExecutor.submit(() -> dispatcherService.withdrawByTransportOrder(ref, immediateAbort))
              .get();
    }
    catch (InterruptedException | ExecutionException exc) {
      throw findSuitableExceptionFor(exc);
    }
  }
}
