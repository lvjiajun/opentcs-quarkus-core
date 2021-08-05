/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.kernel.extensions.rmi;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import static java.util.Objects.requireNonNull;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.youbai.opentcs.access.rmi.factories.SocketFactoryProvider;
import org.youbai.opentcs.access.rmi.services.RegistrationName;
import org.youbai.opentcs.access.rmi.services.RemoteDispatcherService;
import org.youbai.opentcs.access.rmi.services.RemoteQueryService;
import org.youbai.opentcs.components.kernel.Query;
import org.youbai.opentcs.components.kernel.services.QueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.youbai.opentcs.kernel.annotations.ExecutorServiceAnnotations;
import org.youbai.opentcs.kernel.annotations.StandardQueryServiceAnnotations;

/**
 * This class is the standard implementation of the {@link RemoteQueryService} interface.
 * <p>
 * Upon creation, an instance of this class registers itself with the RMI registry by the name
 * </p>
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
@ApplicationScoped
public class StandardRemoteQueryService
    extends KernelRemoteService
    implements RemoteQueryService {

  /**
   * This class's logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(StandardRemoteQueryService.class);
  /**
   * The query service to invoke methods on.
   */
  @StandardQueryServiceAnnotations
  QueryService queryService;
  /**
   * Executes tasks modifying kernel data.
   */
  @ExecutorServiceAnnotations
  ExecutorService kernelExecutor;


  @Override
  public <T> T query(Query<T> query) {
    try {
      return kernelExecutor.submit(() -> queryService.query(query))
          .get();
    }
    catch (InterruptedException | ExecutionException exc) {
      throw findSuitableExceptionFor(exc);
    }
  }

}
