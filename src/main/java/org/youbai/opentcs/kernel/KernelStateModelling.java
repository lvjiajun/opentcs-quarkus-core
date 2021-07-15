/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.kernel;

import io.quarkus.runtime.Startup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.youbai.opentcs.access.Kernel;
import org.youbai.opentcs.components.kernel.KernelExtension;
import org.youbai.opentcs.kernel.annotations.XMLFileModelAnnotations;
import org.youbai.opentcs.kernel.persistence.ModelPersister;
import org.youbai.opentcs.kernel.workingset.Model;
import org.youbai.opentcs.kernel.workingset.TCSObjectPool;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Singleton;

import static java.util.Objects.requireNonNull;

/**
 * This class implements the standard openTCS kernel in modelling mode.
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
@Singleton
public class KernelStateModelling
    extends KernelStateOnline {

  /**
   * This class's Logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(KernelStateModelling.class);
  /**
   * This kernel state's local extensions.
   */
  private final Instance<KernelExtension> extensions;
  /**
   * This instance's <em>initialized</em> flag.
   */
  private boolean initialized;

  /**
   * Creates a new instance.
   *
   * @param objectPool The object pool to be used.
   * @param modelPersister The model persister to be used.
   * @param configuration This class's configuration.
   */

  KernelStateModelling(GlobalSyncObject globalSyncObject,
                       TCSObjectPool objectPool,
                       Model model,
                       @XMLFileModelAnnotations ModelPersister modelPersister,
                       KernelApplicationConfiguration configuration,
                       Instance<KernelExtension> extensions) {
    super(globalSyncObject,
          objectPool,
          model,
          modelPersister,
          configuration.saveModelOnTerminateModelling());
    this.extensions = requireNonNull(extensions, "extensions");
  }

  @Override
  public void initialize() {
    if (initialized) {
      throw new IllegalStateException("Already initialized");
    }
    LOG.debug("Initializing modelling state...");

    // Start kernel extensions.
    for (KernelExtension extension : extensions) {
      LOG.debug("Initializing kernel extension '{}'...", extension);
      extension.initialize();
    }
    LOG.debug("Finished initializing kernel extensions.");

    initialized = true;

    LOG.debug("Modelling state initialized.");
  }

  @Override
  public boolean isInitialized() {
    return initialized;
  }

  @Override
  public void terminate() {
    if (!initialized) {
      throw new IllegalStateException("Not initialized, cannot terminate");
    }
    LOG.debug("Terminating modelling state...");
    super.terminate();

    // Terminate everything that may still use resources.
    for (KernelExtension extension : extensions) {
      LOG.debug("Terminating kernel extension '{}'...", extension);
      extension.terminate();
    }
    LOG.debug("Terminated kernel extensions.");

    initialized = false;

    LOG.debug("Modelling state terminated.");
  }

  @Override
  public Kernel.State getState() {
    return Kernel.State.MODELLING;
  }
}
