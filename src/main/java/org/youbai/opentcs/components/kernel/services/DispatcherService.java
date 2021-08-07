/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.components.kernel.services;

import org.youbai.opentcs.access.KernelRuntimeException;
import org.youbai.opentcs.components.kernel.Dispatcher;
import org.youbai.opentcs.data.ObjectUnknownException;
import org.youbai.opentcs.data.TCSObjectReference;
import org.youbai.opentcs.data.model.Vehicle;
import org.youbai.opentcs.data.order.TransportOrder;

/**
 * Provides methods concerning the {@link Dispatcher}.
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
public interface DispatcherService {

  /**
   * Explicitly trigger the dispatching process.
   *
   * @throws KernelRuntimeException In case there is an exception executing this method.
   */
  void dispatch()
      throws KernelRuntimeException;

  /**
   * Withdraw any order that a vehicle might be processing.
   *
   * @param ref A reference to the vehicle to be modified.
   * @param immediateAbort If {@code false}, this method once will initiate the withdrawal, leaving
   * the transport order assigned to the vehicle until it has finished the movements that it has
   * already been ordered to execute. The transport order's state will change to
   * {@link TransportOrder.State#WITHDRAWN}. If {@code true}, the dispatcher will withdraw the order from the
   * vehicle without further waiting.
   * @throws ObjectUnknownException If the referenced vehicle does not exist.
   * @throws KernelRuntimeException In case there is an exception executing this method.
   */
  void withdrawByVehicle(TCSObjectReference<Vehicle> ref, boolean immediateAbort)
      throws ObjectUnknownException, KernelRuntimeException;
  void withdrawByVehicle(String ref, boolean immediateAbort)
          throws ObjectUnknownException, KernelRuntimeException;
  /**
   * Withdraw the referenced order.
   *
   * @param ref A reference to the transport order to be withdrawn.
   * @param immediateAbort If {@code false}, this method once will initiate the withdrawal, leaving
   * the transport order assigned to the vehicle until it has finished the movements that it has
   * already been ordered to execute. The transport order's state will change to
   * {@link TransportOrder.State#WITHDRAWN}. If {@code true}, the dispatcher will withdraw the order from the
   * vehicle without further waiting.
   * @throws ObjectUnknownException If the referenced transport order does not exist.
   * @throws KernelRuntimeException In case there is an exception executing this method.
   */
  void withdrawByTransportOrder(TCSObjectReference<TransportOrder> ref, boolean immediateAbort)
      throws ObjectUnknownException, KernelRuntimeException;
  void withdrawByTransportOrder(String ref, boolean immediateAbort)
          throws ObjectUnknownException, KernelRuntimeException;
}
