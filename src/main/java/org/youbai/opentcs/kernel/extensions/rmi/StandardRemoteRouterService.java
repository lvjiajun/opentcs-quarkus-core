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
import org.youbai.opentcs.access.rmi.services.RemoteRouterService;
import org.youbai.opentcs.components.kernel.services.RouterService;
import org.youbai.opentcs.data.TCSObjectReference;
import org.youbai.opentcs.data.model.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.youbai.opentcs.kernel.annotations.ExecutorServiceAnnotations;
import org.youbai.opentcs.kernel.annotations.StandardRouterServiceAnnotations;
import org.youbai.opentcs.kernel.annotations.StandardTCSObjectAnnotations;
import org.youbai.opentcs.kernel.services.StandardTCSObjectService;

/**
 * This class is the standard implementation of the {@link RemoteRouterService} interface.
 * <p>
 * Upon creation, an instance of this class registers itself with the RMI registry by the name
 * </p>
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
@ApplicationScoped
public class StandardRemoteRouterService
    extends KernelRemoteService
    implements RemoteRouterService {

  /**
   * This class's logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(StandardRemoteRouterService.class);
  /**
   * The scheduler service to invoke methods on.
   */
  @StandardRouterServiceAnnotations
  RouterService routerService;
  /**
   * Executes tasks modifying kernel data.
   */
  @ExecutorServiceAnnotations
  ExecutorService kernelExecutor;
  /**
   * Creates a new instance.
   *
   */
  @StandardTCSObjectAnnotations
  StandardTCSObjectService standardTCSObjectService;

  public StandardRemoteRouterService() {
  }


  @Override
  public void updatePathLock(TCSObjectReference<Path> ref, boolean locked) {
    try {
      kernelExecutor.submit(() -> routerService.updatePathLock(ref, locked)).get();
    }
    catch (InterruptedException | ExecutionException exc) {
      throw findSuitableExceptionFor(exc);
    }
  }

  @Override
  public void updatePathLock(String ref,boolean locked) {
    try {
      Path path = standardTCSObjectService.fetchObject(Path.class,ref);
      kernelExecutor.submit(() -> routerService.updatePathLock(path.getReference(), locked)).get();
    }
    catch (InterruptedException | ExecutionException exc) {
      throw findSuitableExceptionFor(exc);
    }
  }

  @Override
  public void updateRoutingTopology() {
    try {
      kernelExecutor.submit(() -> routerService.updateRoutingTopology()).get();
    }
    catch (InterruptedException | ExecutionException exc) {
      throw findSuitableExceptionFor(exc);
    }
  }
}
