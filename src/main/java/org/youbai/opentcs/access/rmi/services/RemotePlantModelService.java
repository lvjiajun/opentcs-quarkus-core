/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.access.rmi.services;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

import org.youbai.opentcs.access.to.model.PlantModelCreationTO;
import org.youbai.opentcs.components.kernel.services.PlantModelService;
import org.youbai.opentcs.data.TCSObjectReference;
import org.youbai.opentcs.data.model.Location;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Declares the methods provided by the {@link PlantModelService} via RMI.
 *
 * <p>
 * The majority of the methods declared here have signatures analogous to their counterparts in
 * {@link PlantModelService}, with an additional {@link ClientID} parameter which serves the purpose
 * of identifying the calling client and determining its permissions.
 * </p>
 * <p>
 * To avoid redundancy, the semantics of methods that only pass through their arguments are not
 * explicitly documented here again. See the corresponding API documentation in
 * {@link PlantModelService} for these, instead.
 * </p>
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */

public interface RemotePlantModelService
    extends RemoteTCSObjectService {

  void createPlantModel(PlantModelCreationTO to);

  String getModelName();

  Map<String, String> getModelProperties();

  void updateLocationLock(TCSObjectReference<Location> ref, boolean locked);

  void updateLocationLock(String ref, boolean locked);
}
