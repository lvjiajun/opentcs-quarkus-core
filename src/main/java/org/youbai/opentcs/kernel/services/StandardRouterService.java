/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.kernel.services;

import static java.util.Objects.requireNonNull;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.youbai.opentcs.access.Kernel;
import org.youbai.opentcs.access.LocalKernel;
import org.youbai.opentcs.components.kernel.Dispatcher;
import org.youbai.opentcs.components.kernel.Router;
import org.youbai.opentcs.components.kernel.services.RouterService;
import org.youbai.opentcs.customizations.kernel.GlobalSyncObject;
import org.youbai.opentcs.data.ObjectUnknownException;
import org.youbai.opentcs.data.TCSObjectReference;
import org.youbai.opentcs.data.model.Path;
import org.youbai.opentcs.kernel.KernelApplicationConfiguration;
import org.youbai.opentcs.kernel.annotations.StandardKernelAnnotations;
import org.youbai.opentcs.kernel.workingset.Model;

/**
 * This class is the standard implementation of the {@link RouterService} interface.
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
@Singleton
public class StandardRouterService
    implements RouterService {

  /**
   * A global object to be used for synchronization within the kernel.
   */
  private final Object globalSyncObject;
  /**
   * The kernel.
   */
  private final LocalKernel kernel;
  /**
   * The router.
   */
  private final Router router;
  /**
   * The dispatcher.
   */
  private final Dispatcher dispatcher;
  /**
   * The model facade to the object pool.
   */
  private final Model model;
  /**
   * The kernel application's configuration.
   */
  private final KernelApplicationConfiguration configuration;

  /**
   * Creates a new instance.
   *
   * @param globalSyncObject The kernel threads' global synchronization object.
   * @param kernel The kernel.
   * @param router The scheduler.
   * @param dispatcher The dispatcher.
   * @param model The model to be used.
   * @param configuration The kernel application's configuration.
   */

  public StandardRouterService(Object globalSyncObject,
                               @StandardKernelAnnotations LocalKernel kernel,
                               Router router,
                               Dispatcher dispatcher,
                               Model model,
                               KernelApplicationConfiguration configuration) {
    this.globalSyncObject = requireNonNull(globalSyncObject, "globalSyncObject");
    this.kernel = requireNonNull(kernel, "kernel");
    this.router = requireNonNull(router, "router");
    this.dispatcher = requireNonNull(dispatcher, "dispatcher");
    this.model = requireNonNull(model, "model");
    this.configuration = requireNonNull(configuration, "configuration");
  }

  @Override
  public void updatePathLock(TCSObjectReference<Path> ref, boolean locked)
      throws ObjectUnknownException {
    synchronized (globalSyncObject) {
      model.setPathLocked(ref, locked);
      if (kernel.getState() == Kernel.State.OPERATING
          && configuration.updateRoutingTopologyOnPathLockChange()) {
        updateRoutingTopology();
      }
    }
  }

  @Override
  public void updateRoutingTopology() {
    synchronized (globalSyncObject) {
      router.topologyChanged();
      dispatcher.topologyChanged();
    }
  }
}
