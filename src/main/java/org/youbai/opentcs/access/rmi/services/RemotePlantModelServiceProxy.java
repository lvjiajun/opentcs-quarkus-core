/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.access.rmi.services;

import java.rmi.RemoteException;
import java.util.Map;
import org.youbai.opentcs.access.KernelRuntimeException;
import org.youbai.opentcs.access.to.model.PlantModelCreationTO;
import org.youbai.opentcs.components.kernel.services.PlantModelService;
import org.youbai.opentcs.data.ObjectExistsException;
import org.youbai.opentcs.data.ObjectUnknownException;
import org.youbai.opentcs.data.TCSObjectReference;
import org.youbai.opentcs.data.model.Location;

/**
 * The default implementation of the plant model service.
 * Delegates method invocations to the corresponding remote service.
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
class RemotePlantModelServiceProxy
    extends RemoteTCSObjectServiceProxy<RemotePlantModelService>
    implements PlantModelService {

  @Override
  public void createPlantModel(PlantModelCreationTO to)
      throws ObjectUnknownException, ObjectExistsException, KernelRuntimeException,
             IllegalStateException {
    checkServiceAvailability();

    try {
      getRemoteService().createPlantModel(getClientId(), to);
    }
    catch (RemoteException ex) {
      throw findSuitableExceptionFor(ex);
    }
  }

  @Override
  public String getModelName()
      throws KernelRuntimeException {
    checkServiceAvailability();

    try {
      return getRemoteService().getModelName(getClientId());
    }
    catch (RemoteException ex) {
      throw findSuitableExceptionFor(ex);
    }
  }

  @Override
  public Map<String, String> getModelProperties()
      throws KernelRuntimeException {
    checkServiceAvailability();

    try {
      return getRemoteService().getModelProperties(getClientId());
    }
    catch (RemoteException ex) {
      throw findSuitableExceptionFor(ex);
    }
  }

  @Override
  public void updateLocationLock(TCSObjectReference<Location> ref, boolean locked)
      throws ObjectUnknownException, KernelRuntimeException {
    checkServiceAvailability();

    try {
      getRemoteService().updateLocationLock(getClientId(), ref, locked);
    }
    catch (RemoteException ex) {
      throw findSuitableExceptionFor(ex);
    }
  }
}
