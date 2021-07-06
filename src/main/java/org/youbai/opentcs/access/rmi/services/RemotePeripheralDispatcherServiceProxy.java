/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.access.rmi.services;

import java.rmi.RemoteException;
import org.youbai.opentcs.access.KernelRuntimeException;
import org.youbai.opentcs.components.kernel.services.PeripheralDispatcherService;
import org.youbai.opentcs.data.ObjectUnknownException;
import org.youbai.opentcs.data.model.Location;
import org.youbai.opentcs.data.model.TCSResourceReference;

/**
 * The default implementation of the peripheral dispatcher service.
 * Delegates method invocations to the corresponding remote service.
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
class RemotePeripheralDispatcherServiceProxy
    extends AbstractRemoteServiceProxy<RemotePeripheralDispatcherService>
    implements PeripheralDispatcherService {

  @Override
  public void dispatch()
      throws KernelRuntimeException {
    checkServiceAvailability();

    try {
      getRemoteService().dispatch(getClientId());
    }
    catch (RemoteException ex) {
      throw findSuitableExceptionFor(ex);
    }
  }

  @Override
  public void withdrawByLocation(TCSResourceReference<Location> locationRef)
      throws ObjectUnknownException, KernelRuntimeException {
    checkServiceAvailability();

    try {
      getRemoteService().withdrawByLocation(getClientId(), locationRef);
    }
    catch (RemoteException ex) {
      throw findSuitableExceptionFor(ex);
    }
  }
}
