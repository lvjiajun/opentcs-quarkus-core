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
import org.youbai.opentcs.components.kernel.services.InternalPeripheralService;
import org.youbai.opentcs.components.kernel.services.PeripheralService;
import org.youbai.opentcs.components.kernel.services.TCSObjectService;
import org.youbai.opentcs.data.ObjectUnknownException;
import org.youbai.opentcs.data.TCSObjectReference;
import org.youbai.opentcs.data.model.Location;
import org.youbai.opentcs.data.model.PeripheralInformation;
import org.youbai.opentcs.data.model.TCSResourceReference;
import org.youbai.opentcs.data.model.Vehicle;
import org.youbai.opentcs.data.peripherals.PeripheralJob;
import org.youbai.opentcs.drivers.peripherals.PeripheralAdapterCommand;
import org.youbai.opentcs.drivers.peripherals.PeripheralCommAdapterDescription;
import org.youbai.opentcs.drivers.peripherals.PeripheralProcessModel;
import org.youbai.opentcs.drivers.peripherals.management.PeripheralAttachmentInformation;
import org.youbai.opentcs.kernel.GlobalSyncObject;
import org.youbai.opentcs.kernel.annotations.StandardPeripheralServiceAnnotations;
import org.youbai.opentcs.kernel.annotations.StandardTCSObjectAnnotations;
import org.youbai.opentcs.kernel.peripherals.PeripheralAttachmentManager;
import org.youbai.opentcs.kernel.peripherals.PeripheralEntry;
import org.youbai.opentcs.kernel.peripherals.PeripheralEntryPool;
import org.youbai.opentcs.kernel.workingset.Model;

/**
 * This class is the standard implementation of the {@link PeripheralService} interface.
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
@Singleton
@StandardPeripheralServiceAnnotations
public class StandardPeripheralService
    extends AbstractTCSObjectService
    implements InternalPeripheralService {

  /**
   * A global object to be used for synchronization within the kernel.
   */
  private final Object globalSyncObject;
  /**
   * The attachment manager.
   */
  @Inject
  public PeripheralAttachmentManager attachmentManager;
  /**
   * The pool of peripheral entries.
   */
  @Inject
  public PeripheralEntryPool peripheralEntryPool;
  /**
   * The model facade to the object pool.
   */
  private final Model model;

  /**
   * Creates a new instance.
   *
   * @param objectService The tcs object service.
   * @param globalSyncObject The kernel threads' global synchronization object.
   * @param model The model to be used.
   */

  public StandardPeripheralService(@StandardTCSObjectAnnotations TCSObjectService objectService,
                                   GlobalSyncObject globalSyncObject,
                                   Model model) {
    super(objectService);
    this.globalSyncObject = requireNonNull(globalSyncObject, "globalSyncObject");
    this.model = requireNonNull(model, "model");
  }

  @Override
  public void attachCommAdapter(TCSResourceReference<Location> ref,
                                PeripheralCommAdapterDescription description)
      throws ObjectUnknownException {
    synchronized (globalSyncObject) {
      attachmentManager.attachAdapterToLocation(ref, description);
    }
  }

  @Override
  public void attachCommAdapter(String ref, PeripheralCommAdapterDescription description) throws ObjectUnknownException, KernelRuntimeException {
    Location location = fetchObject(Location.class,ref);
    attachmentManager.attachAdapterToLocation(location.getReference(), description);
  }

  @Override
  public void disableCommAdapter(TCSResourceReference<Location> ref)
      throws ObjectUnknownException {
    synchronized (globalSyncObject) {
      peripheralEntryPool.getEntryFor(ref).getCommAdapter().disable();
    }
  }

  @Override
  public void disableCommAdapter(String ref) throws ObjectUnknownException, KernelRuntimeException {
    Location location = fetchObject(Location.class,ref);
    peripheralEntryPool.getEntryFor(location.getReference()).getCommAdapter().disable();
  }

  @Override
  public void enableCommAdapter(TCSResourceReference<Location> ref)
      throws ObjectUnknownException {
    synchronized (globalSyncObject) {
      peripheralEntryPool.getEntryFor(ref).getCommAdapter().enable();
    }
  }

  @Override
  public void enableCommAdapter(String ref) throws ObjectUnknownException, KernelRuntimeException {
    Location location = fetchObject(Location.class,ref);
    peripheralEntryPool.getEntryFor(location.getReference()).getCommAdapter().enable();
  }

  @Override
  public PeripheralAttachmentInformation fetchAttachmentInformation(
      TCSResourceReference<Location> ref)
      throws ObjectUnknownException {
    synchronized (globalSyncObject) {
      return attachmentManager.getAttachmentInformation(ref);
    }
  }

  @Override
  public PeripheralAttachmentInformation fetchAttachmentInformation(String ref) throws ObjectUnknownException, KernelRuntimeException {
    Location location = fetchObject(Location.class,ref);
    return attachmentManager.getAttachmentInformation(location.getReference());
  }

  @Override
  public PeripheralProcessModel fetchProcessModel(TCSResourceReference<Location> ref)
      throws ObjectUnknownException {
    synchronized (globalSyncObject) {
      return peripheralEntryPool.getEntryFor(ref).getCommAdapter().getProcessModel();
    }
  }

  @Override
  public PeripheralProcessModel fetchProcessModel(String ref) throws ObjectUnknownException, KernelRuntimeException {
    Location location = fetchObject(Location.class,ref);
    return peripheralEntryPool.getEntryFor(location.getReference()).getCommAdapter().getProcessModel();
  }

  @Override
  public void sendCommAdapterCommand(TCSResourceReference<Location> ref,
                                     PeripheralAdapterCommand command)
      throws ObjectUnknownException {
    synchronized (globalSyncObject) {
      PeripheralEntry entry = peripheralEntryPool.getEntryFor(ref);
      synchronized (entry.getCommAdapter()) {
        entry.getCommAdapter().execute(command);
      }
    }
  }

  @Override
  public void sendCommAdapterCommand(String ref, PeripheralAdapterCommand command) throws ObjectUnknownException, KernelRuntimeException {
    synchronized (globalSyncObject) {
      Location location = fetchObject(Location.class,ref);
      PeripheralEntry entry = peripheralEntryPool.getEntryFor(location.getReference());
      synchronized (entry.getCommAdapter()) {
        entry.getCommAdapter().execute(command);
      }
    }
  }

  @Override
  public void updatePeripheralProcState(TCSResourceReference<Location> ref,
                                        PeripheralInformation.ProcState state)
      throws ObjectUnknownException {
    synchronized (globalSyncObject) {
      model.setLocationProcState(ref, state);
    }
  }

  @Override
  public void updatePeripheralReservationToken(TCSResourceReference<Location> ref,
                                               String reservationToken)
      throws ObjectUnknownException {
    synchronized (globalSyncObject) {
      model.setLocationReservationToken(ref, reservationToken);
    }
  }

  @Override
  public void updatePeripheralState(TCSResourceReference<Location> ref,
                                    PeripheralInformation.State state)
      throws ObjectUnknownException {
    synchronized (globalSyncObject) {
      model.setLocationState(ref, state);
    }
  }

  @Override
  public void updatePeripheralJob(TCSResourceReference<Location> ref,
                                  TCSObjectReference<PeripheralJob> peripheralJob)
      throws ObjectUnknownException {
    synchronized (globalSyncObject) {
      model.setLocationPeripheralJob(ref, peripheralJob);
    }
  }
}
