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

import io.quarkus.runtime.Startup;
import org.youbai.opentcs.access.KernelRuntimeException;
import org.youbai.opentcs.components.kernel.PeripheralJobDispatcher;
import org.youbai.opentcs.components.kernel.services.PeripheralDispatcherService;
import org.youbai.opentcs.data.ObjectUnknownException;
import org.youbai.opentcs.data.model.Location;
import org.youbai.opentcs.data.model.TCSResourceReference;
import org.youbai.opentcs.kernel.GlobalSyncObject;
import org.youbai.opentcs.kernel.annotations.StandardPeripheralDispatcherServiceAnnotations;
import org.youbai.opentcs.kernel.workingset.TCSObjectPool;

/**
 * This class is the standard implementation of the {@link PeripheralDispatcherService} interface.
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
@Singleton
@StandardPeripheralDispatcherServiceAnnotations
public class StandardPeripheralDispatcherService
    implements PeripheralDispatcherService {

  /**
   * A global object to be used for synchronization within the kernel.
   */
  private final Object globalSyncObject;
  /**
   * The container of all course model and transport order objects.
   */
  private final TCSObjectPool globalObjectPool;
  /**
   * The peripheral job dispatcher.
   */
  @Inject
  public PeripheralJobDispatcher dispatcher;

  /**
   * Creates a new instance.
   *
   * @param globalSyncObject The kernel threads' global synchronization object.
   * @param globalObjectPool The object pool to be used.
   */

  public StandardPeripheralDispatcherService(GlobalSyncObject globalSyncObject,
                                             TCSObjectPool globalObjectPool) {
    this.globalSyncObject = requireNonNull(globalSyncObject, "globalSyncObject");
    this.globalObjectPool = requireNonNull(globalObjectPool, "globalObjectPool");
  }

  @Override
  public void dispatch() {
    synchronized (globalSyncObject) {
      dispatcher.dispatch();
    }
  }

  @Override
  public void withdrawByLocation(TCSResourceReference<Location> ref)
      throws ObjectUnknownException {
    synchronized (globalSyncObject) {
      dispatcher.withdrawJob(globalObjectPool.getObject(Location.class, ref));
    }
  }

  @Override
  public void withdrawByLocation(String ref)
          throws ObjectUnknownException{
    dispatcher.withdrawJob(globalObjectPool.getObject(Location.class, ref));
  }
}
