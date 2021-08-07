/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.components.kernel.services;

import org.apache.kafka.common.protocol.types.Field;
import org.youbai.opentcs.access.KernelRuntimeException;
import org.youbai.opentcs.data.ObjectUnknownException;
import org.youbai.opentcs.data.model.Location;
import org.youbai.opentcs.data.model.TCSResourceReference;
import org.youbai.opentcs.drivers.peripherals.PeripheralAdapterCommand;
import org.youbai.opentcs.drivers.peripherals.PeripheralCommAdapter;
import org.youbai.opentcs.drivers.peripherals.PeripheralCommAdapterDescription;
import org.youbai.opentcs.drivers.peripherals.PeripheralProcessModel;
import org.youbai.opentcs.drivers.peripherals.management.PeripheralAttachmentInformation;

/**
 * Provides methods concerning peripheral devices represented by {@link Location}s.
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
public interface PeripheralService
    extends TCSObjectService {

  /**
   * Attaches the described comm adapter to the referenced location.
   *
   * @param ref A reference to the location.
   * @param description The description for the comm adapter to be attached.
   * @throws ObjectUnknownException If the referenced location does not exist.
   * @throws KernelRuntimeException In case there is an exception executing this method.
   */
  void attachCommAdapter(TCSResourceReference<Location> ref,
                         PeripheralCommAdapterDescription description)
      throws ObjectUnknownException, KernelRuntimeException;
  void attachCommAdapter(String ref,
                         PeripheralCommAdapterDescription description)
          throws ObjectUnknownException, KernelRuntimeException;
  /**
   * Disables the comm adapter attached to the referenced location.
   *
   * @param ref A reference to the location the comm adapter is attached to.
   * @throws ObjectUnknownException If the referenced location does not exist.
   * @throws KernelRuntimeException In case there is an exception executing this method.
   */
  void disableCommAdapter(TCSResourceReference<Location> ref)
      throws ObjectUnknownException, KernelRuntimeException;
  void disableCommAdapter(String ref)
          throws ObjectUnknownException, KernelRuntimeException;
  /**
   * Enables the comm adapter attached to the referenced location.
   *
   * @param ref A reference to the location the comm adapter is attached to.
   * @throws ObjectUnknownException If the referenced location does not exist.
   * @throws KernelRuntimeException In case there is an exception executing this method.
   */
  void enableCommAdapter(TCSResourceReference<Location> ref)
      throws ObjectUnknownException, KernelRuntimeException;
  void enableCommAdapter(String ref)
          throws ObjectUnknownException, KernelRuntimeException;
  /**
   * Returns attachment information for the referenced location.
   *
   * @param ref A reference to the location.
   * @return The attachment information.
   * @throws ObjectUnknownException If the referenced location does not exist.
   * @throws KernelRuntimeException In case there is an exception executing this method.
   */
  PeripheralAttachmentInformation fetchAttachmentInformation(TCSResourceReference<Location> ref)
      throws ObjectUnknownException, KernelRuntimeException;
  PeripheralAttachmentInformation fetchAttachmentInformation(String ref)
          throws ObjectUnknownException, KernelRuntimeException;
  /**
   * Returns the process model for the referenced location.
   *
   * @param ref A reference to the location.
   * @return The process model.
   * @throws ObjectUnknownException If the referenced location does not exist.
   * @throws KernelRuntimeException In case there is an exception executing this method.
   */
  PeripheralProcessModel fetchProcessModel(TCSResourceReference<Location> ref)
      throws ObjectUnknownException, KernelRuntimeException;
  PeripheralProcessModel fetchProcessModel(String ref)
          throws ObjectUnknownException, KernelRuntimeException;
  /**
   * Sends a {@link PeripheralAdapterCommand} to the comm adapter attached to the referenced
   * location.
   *
   * @see PeripheralAdapterCommand#execute(PeripheralCommAdapter)
   * @param ref A reference to the location.
   * @param command The adapter command to send.
   * @throws ObjectUnknownException If the referenced location does not exist.
   * @throws KernelRuntimeException In case there is an exception executing this method.
   */
  void sendCommAdapterCommand(TCSResourceReference<Location> ref, PeripheralAdapterCommand command)
      throws ObjectUnknownException, KernelRuntimeException;
  void sendCommAdapterCommand(String ref, PeripheralAdapterCommand command)
          throws ObjectUnknownException, KernelRuntimeException;
}
