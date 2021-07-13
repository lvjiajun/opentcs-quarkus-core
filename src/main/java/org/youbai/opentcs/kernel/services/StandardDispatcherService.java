/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.kernel.services;

import org.youbai.opentcs.components.kernel.Dispatcher;
import org.youbai.opentcs.components.kernel.services.DispatcherService;
import org.youbai.opentcs.data.ObjectUnknownException;
import org.youbai.opentcs.data.TCSObjectReference;
import org.youbai.opentcs.data.model.Vehicle;
import org.youbai.opentcs.data.order.TransportOrder;
import org.youbai.opentcs.kernel.GlobalSyncObject;
import org.youbai.opentcs.kernel.annotations.StandardDispatcherServiceAnnotations;
import org.youbai.opentcs.kernel.workingset.TCSObjectPool;

import javax.inject.Inject;
import javax.inject.Singleton;

import static java.util.Objects.requireNonNull;

/**
 * This class is the standard implementation of the {@link DispatcherService} interface.
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
@Singleton
@StandardDispatcherServiceAnnotations
public class StandardDispatcherService
    implements DispatcherService {

  /**
   * A global object to be used for synchronization within the kernel.
   */
  private final Object globalSyncObject;
  /**
   * The container of all course model and transport order objects.
   */
  private final TCSObjectPool globalObjectPool;
  /**
   * The dispatcher.
   */
  private final Dispatcher dispatcher;

  /**
   * Creates a new instance.
   *
   * @param globalSyncObject The kernel threads' global synchronization object.
   * @param globalObjectPool The object pool to be used.
   * @param dispatcher The dispatcher.
   */

  public StandardDispatcherService(GlobalSyncObject globalSyncObject,
                                   TCSObjectPool globalObjectPool,
                                   Dispatcher dispatcher) {
    this.globalSyncObject = requireNonNull(globalSyncObject, "globalSyncObject");
    this.globalObjectPool = requireNonNull(globalObjectPool, "globalObjectPool");
    this.dispatcher = requireNonNull(dispatcher, "dispatcher");
  }

  @Override
  public void dispatch() {
    synchronized (globalSyncObject) {
      dispatcher.dispatch();
    }
  }

  @Override
  public void withdrawByVehicle(TCSObjectReference<Vehicle> ref, boolean immediateAbort)
      throws ObjectUnknownException {
    synchronized (globalSyncObject) {
      dispatcher.withdrawOrder(globalObjectPool.getObject(Vehicle.class, ref), immediateAbort);
    }
  }

  @Override
  public void withdrawByTransportOrder(TCSObjectReference<TransportOrder> ref,
                                       boolean immediateAbort)
      throws ObjectUnknownException {
    synchronized (globalSyncObject) {
      dispatcher.withdrawOrder(globalObjectPool.getObject(TransportOrder.class, ref),
                               immediateAbort);
    }
  }
}
