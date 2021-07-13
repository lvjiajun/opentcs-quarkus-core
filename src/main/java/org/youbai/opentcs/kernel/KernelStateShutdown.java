/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.kernel;

import org.youbai.opentcs.access.Kernel;
import org.youbai.opentcs.kernel.annotations.XMLFileModelAnnotations;
import org.youbai.opentcs.kernel.persistence.ModelPersister;
import org.youbai.opentcs.kernel.workingset.Model;
import org.youbai.opentcs.kernel.workingset.TCSObjectPool;

import javax.inject.Singleton;

/**
 * This class implements the standard openTCS kernel when it's shut down.
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
@Singleton
public class KernelStateShutdown
    extends KernelState {

  /**
   * Indicates whether this component is enabled.
   */
  private boolean initialized;

  /**
   * Creates a new StandardKernelShutdownState.
   *
   * @param objectPool The object pool to be used.
   * @param modelPersister The model persister to be used.
   */

  public KernelStateShutdown(GlobalSyncObject globalSyncObject,
                             TCSObjectPool objectPool,
                             Model model,
                             @XMLFileModelAnnotations ModelPersister modelPersister) {
    super(globalSyncObject,
          objectPool,
          model,
          modelPersister);
  }

  // Methods that HAVE to be implemented/overridden start here.
  @Override
  public void initialize() {
    initialized = true;
  }

  @Override
  public boolean isInitialized() {
    return initialized;
  }

  @Override
  public void terminate() {
    initialized = false;
  }

  @Override
  public Kernel.State getState() {
    return Kernel.State.SHUTDOWN;
  }
}
