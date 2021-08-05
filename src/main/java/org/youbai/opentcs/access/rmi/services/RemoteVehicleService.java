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
import java.util.Set;

import org.youbai.opentcs.components.kernel.services.VehicleService;
import org.youbai.opentcs.data.TCSObjectReference;
import org.youbai.opentcs.data.model.Vehicle;
import org.youbai.opentcs.drivers.vehicle.AdapterCommand;
import org.youbai.opentcs.drivers.vehicle.VehicleCommAdapterDescription;
import org.youbai.opentcs.drivers.vehicle.management.AttachmentInformation;
import org.youbai.opentcs.drivers.vehicle.management.VehicleProcessModelTO;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Declares the methods provided by the {@link VehicleService} via RMI.
 *
 * <p>
 * The majority of the methods declared here have signatures analogous to their counterparts in
 * {@link VehicleService}, with an additional {@link ClientID} parameter which serves the purpose
 * of identifying the calling client and determining its permissions.
 * </p>
 * <p>
 * To avoid redundancy, the semantics of methods that only pass through their arguments are not
 * explicitly documented here again. See the corresponding API documentation in
 * {@link VehicleService} for these, instead.
 * </p>
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
public interface RemoteVehicleService
    extends RemoteTCSObjectService{

  void attachCommAdapter(TCSObjectReference<Vehicle> ref,
                         VehicleCommAdapterDescription description);

  void disableCommAdapter(TCSObjectReference<Vehicle> ref);

  void enableCommAdapter(TCSObjectReference<Vehicle> ref);

  AttachmentInformation fetchAttachmentInformation(TCSObjectReference<Vehicle> ref);

  VehicleProcessModelTO fetchProcessModel(TCSObjectReference<Vehicle> ref);

  void sendCommAdapterCommand(TCSObjectReference<Vehicle> ref,
                              AdapterCommand command);

  void sendCommAdapterMessage(TCSObjectReference<Vehicle> vehicleRef,
                              Object message);

  void updateVehicleIntegrationLevel(TCSObjectReference<Vehicle> ref,
                                     Vehicle.IntegrationLevel integrationLevel);

  void updateVehicleAllowedOrderTypes(TCSObjectReference<Vehicle> ref,
                                      Set<String> allowedOrderTypes);
}
