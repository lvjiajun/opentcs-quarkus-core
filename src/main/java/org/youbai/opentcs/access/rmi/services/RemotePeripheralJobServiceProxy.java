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
import org.youbai.opentcs.access.to.peripherals.PeripheralJobCreationTO;
import org.youbai.opentcs.components.kernel.services.PeripheralJobService;
import org.youbai.opentcs.data.ObjectExistsException;
import org.youbai.opentcs.data.ObjectUnknownException;
import org.youbai.opentcs.data.peripherals.PeripheralJob;

/**
 * The default implementation of the peripheral job service.
 * Delegates method invocations to the corresponding remote service.
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
class RemotePeripheralJobServiceProxy
    extends RemoteTCSObjectServiceProxy<RemotePeripheralJobService>
    implements PeripheralJobService {

  @Override
  public PeripheralJob createPeripheralJob(PeripheralJobCreationTO to)
      throws ObjectUnknownException, ObjectExistsException, KernelRuntimeException {
    checkServiceAvailability();

    try {
      return getRemoteService().createPeripheralJob(getClientId(), to);
    }
    catch (RemoteException ex) {
      throw findSuitableExceptionFor(ex);
    }
  }
}
