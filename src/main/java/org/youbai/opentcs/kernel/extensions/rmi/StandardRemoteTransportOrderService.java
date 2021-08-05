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
import javax.enterprise.context.Dependent;
import javax.inject.Named;
import javax.inject.Singleton;

import org.youbai.opentcs.access.rmi.factories.SocketFactoryProvider;
import org.youbai.opentcs.access.rmi.services.RemoteTransportOrderService;
import org.youbai.opentcs.access.to.order.OrderSequenceCreationTO;
import org.youbai.opentcs.access.to.order.TransportOrderCreationTO;
import org.youbai.opentcs.components.kernel.services.TransportOrderService;
import org.youbai.opentcs.data.TCSObjectReference;
import org.youbai.opentcs.data.order.OrderSequence;
import org.youbai.opentcs.data.order.TransportOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.youbai.opentcs.kernel.annotations.ExecutorServiceAnnotations;
import org.youbai.opentcs.kernel.annotations.StandardTransportOrderServiceAnnotations;

/**
 * This class is the standard implementation of the {@link RemoteTransportOrderService} interface.
 * <p>
 * Upon creation, an instance of this class registers itself with the RMI registry by the name
 * </p>
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
@Singleton
public class StandardRemoteTransportOrderService
    extends StandardRemoteTCSObjectService
    implements RemoteTransportOrderService {

  /**
   * This class's logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(StandardRemoteTransportOrderService.class);
  /**
   * The transport order service to invoke methods on.
   */
  private final TransportOrderService transportOrderService;
  /**
   * Executes tasks modifying kernel data.
   */
  private final ExecutorService kernelExecutor;

  /**
   * Creates a new instance.
   *
   * @param transportOrderService The transport order service.
   * @param kernelExecutor Executes tasks modifying kernel data.
   */

  public StandardRemoteTransportOrderService(@StandardTransportOrderServiceAnnotations TransportOrderService transportOrderService,
                                             @ExecutorServiceAnnotations ExecutorService kernelExecutor) {
    super(transportOrderService, kernelExecutor);
    this.transportOrderService = requireNonNull(transportOrderService, "transportOrderService");
    this.kernelExecutor = requireNonNull(kernelExecutor, "kernelExecutor");
  }
  @Override
  public OrderSequence createOrderSequence(OrderSequenceCreationTO to) {
    try {
      return kernelExecutor.submit(() -> transportOrderService.createOrderSequence(to)).get();
    }
    catch (InterruptedException | ExecutionException exc) {
      throw findSuitableExceptionFor(exc);
    }
  }

  @Override
  public TransportOrder createTransportOrder(TransportOrderCreationTO to) {
    try {
      return kernelExecutor.submit(() -> transportOrderService.createTransportOrder(to)).get();
    }
    catch (InterruptedException | ExecutionException exc) {
      throw findSuitableExceptionFor(exc);
    }
  }

  @Override
  public void markOrderSequenceComplete(TCSObjectReference<OrderSequence> ref) {
    try {
      kernelExecutor.submit(() -> transportOrderService.markOrderSequenceComplete(ref)).get();
    }
    catch (InterruptedException | ExecutionException exc) {
      throw findSuitableExceptionFor(exc);
    }
  }
}
