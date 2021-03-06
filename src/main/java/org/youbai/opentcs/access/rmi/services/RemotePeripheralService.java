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

import org.youbai.opentcs.components.kernel.services.PeripheralService;
import org.youbai.opentcs.data.model.Location;
import org.youbai.opentcs.data.model.TCSResourceReference;
import org.youbai.opentcs.drivers.peripherals.PeripheralAdapterCommand;
import org.youbai.opentcs.drivers.peripherals.PeripheralCommAdapterDescription;
import org.youbai.opentcs.drivers.peripherals.PeripheralProcessModel;
import org.youbai.opentcs.drivers.peripherals.management.PeripheralAttachmentInformation;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Declares the methods provided by the {@link PeripheralService} via RMI.
 *
 * <p>
 * The majority of the methods declared here have signatures analogous to their counterparts in
 * {@link PeripheralService}, with an additional {@link ClientID} parameter which serves the purpose
 * of identifying the calling client and determining its permissions.
 * </p>
 * <p>
 * To avoid redundancy, the semantics of methods that only pass through their arguments are not
 * explicitly documented here again. See the corresponding API documentation in
 * {@link PeripheralService} for these, instead.
 * </p>
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */

public interface RemotePeripheralService
    extends RemoteTCSObjectService{

  void attachCommAdapter(TCSResourceReference<Location> ref,
                         PeripheralCommAdapterDescription description);
  void attachCommAdapter(String ref,
                         PeripheralCommAdapterDescription description);
  void disableCommAdapter(TCSResourceReference<Location> ref);
  void disableCommAdapter(String ref);
  void enableCommAdapter(TCSResourceReference<Location> ref);
  void enableCommAdapter(String ref);
  PeripheralAttachmentInformation fetchAttachmentInformation(TCSResourceReference<Location> ref);
  PeripheralAttachmentInformation fetchAttachmentInformation(String ref);
  PeripheralProcessModel fetchProcessModel(TCSResourceReference<Location> ref);
  PeripheralProcessModel fetchProcessModel(String ref);
  void sendCommAdapterCommand(TCSResourceReference<Location> ref,
                              PeripheralAdapterCommand command);
  void sendCommAdapterCommand(String ref,
                              PeripheralAdapterCommand command);
}
