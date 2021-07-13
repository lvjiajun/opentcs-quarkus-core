/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.kernel.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.youbai.opentcs.access.KernelRuntimeException;
import org.youbai.opentcs.components.kernel.services.InternalVehicleService;
import org.youbai.opentcs.components.kernel.services.TCSObjectService;
import org.youbai.opentcs.components.kernel.services.VehicleService;
import org.youbai.opentcs.data.ObjectUnknownException;
import org.youbai.opentcs.data.TCSObjectReference;
import org.youbai.opentcs.data.model.Point;
import org.youbai.opentcs.data.model.Triple;
import org.youbai.opentcs.data.model.Vehicle;
import org.youbai.opentcs.data.order.OrderSequence;
import org.youbai.opentcs.data.order.TransportOrder;
import org.youbai.opentcs.drivers.vehicle.AdapterCommand;
import org.youbai.opentcs.drivers.vehicle.LoadHandlingDevice;
import org.youbai.opentcs.drivers.vehicle.VehicleCommAdapterDescription;
import org.youbai.opentcs.drivers.vehicle.management.AttachmentInformation;
import org.youbai.opentcs.drivers.vehicle.management.VehicleProcessModelTO;
import org.youbai.opentcs.kernel.GlobalSyncObject;
import org.youbai.opentcs.kernel.annotations.StandardTCSObjectAnnotations;
import org.youbai.opentcs.kernel.extensions.controlcenter.vehicles.AttachmentManager;
import org.youbai.opentcs.kernel.extensions.controlcenter.vehicles.VehicleEntry;
import org.youbai.opentcs.kernel.extensions.controlcenter.vehicles.VehicleEntryPool;
import org.youbai.opentcs.kernel.vehicles.LocalVehicleControllerPool;
import org.youbai.opentcs.kernel.vehicles.VehicleCommAdapterRegistry;
import org.youbai.opentcs.kernel.workingset.Model;
import org.youbai.opentcs.kernel.workingset.TCSObjectPool;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Set;

import static java.util.Objects.requireNonNull;

/**
 * This class is the standard implementation of the {@link VehicleService} interface.
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
@Singleton
public class StandardVehicleService
    extends AbstractTCSObjectService
    implements InternalVehicleService {

  /**
   * This class' logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(StandardVehicleService.class);
  /**
   * A global object to be used for synchronization within the kernel.
   */
  private final Object globalSyncObject;
  /**
   * The container of all course model and transport order objects.
   */
  private final TCSObjectPool globalObjectPool;
  /**
   * A pool of vehicle controllers.
   */
  private final LocalVehicleControllerPool vehicleControllerPool;
  /**
   * A pool of vehicle entries.
   */
  private final VehicleEntryPool vehicleEntryPool;
  /**
   * The attachment manager.
   */
  private final AttachmentManager attachmentManager;
  /**
   * The registry for all communication adapters.
   */
  private final VehicleCommAdapterRegistry commAdapterRegistry;
  /**
   * The model facade to the object pool.
   */
  private final Model model;

  /**
   * Creates a new instance.
   *
   * @param objectService The tcs object service.
   * @param globalSyncObject The kernel threads' global synchronization object.
   * @param globalObjectPool The object pool to be used.
   * @param vehicleControllerPool The controller pool to be used.
   * @param vehicleEntryPool The pool of vehicle entries to be used.
   * @param attachmentManager The attachment manager.
   * @param commAdapterRegistry The registry for all communication adapters.
   * @param model The model to be used.
   */

  public StandardVehicleService(@StandardTCSObjectAnnotations TCSObjectService objectService,
                                GlobalSyncObject globalSyncObject,
                                TCSObjectPool globalObjectPool,
                                LocalVehicleControllerPool vehicleControllerPool,
                                VehicleEntryPool vehicleEntryPool,
                                AttachmentManager attachmentManager,
                                VehicleCommAdapterRegistry commAdapterRegistry,
                                Model model) {
    super(objectService);
    this.globalSyncObject = requireNonNull(globalSyncObject, "globalSyncObject");
    this.globalObjectPool = requireNonNull(globalObjectPool, "globalObjectPool");
    this.vehicleControllerPool = requireNonNull(vehicleControllerPool, "vehicleControllerPool");
    this.vehicleEntryPool = requireNonNull(vehicleEntryPool, "vehicleEntryPool");
    this.attachmentManager = requireNonNull(attachmentManager, "attachmentManager");
    this.commAdapterRegistry = requireNonNull(commAdapterRegistry, "commAdapterRegistry");
    this.model = requireNonNull(model, "model");
  }

  @Override
  public void updateVehicleEnergyLevel(TCSObjectReference<Vehicle> ref, int energyLevel)
      throws ObjectUnknownException {
    synchronized (globalSyncObject) {
      model.setVehicleEnergyLevel(ref, energyLevel);
    }
  }

  @Override
  public void updateVehicleLoadHandlingDevices(TCSObjectReference<Vehicle> ref,
                                               List<LoadHandlingDevice> devices)
      throws ObjectUnknownException {
    synchronized (globalSyncObject) {
      model.setVehicleLoadHandlingDevices(ref, devices);
    }
  }

  @Override
  public void updateVehicleNextPosition(TCSObjectReference<Vehicle> vehicleRef,
                                        TCSObjectReference<Point> pointRef)
      throws ObjectUnknownException {
    synchronized (globalSyncObject) {
      model.setVehicleNextPosition(vehicleRef, pointRef);
    }
  }

  @Override
  public void updateVehicleOrderSequence(TCSObjectReference<Vehicle> vehicleRef,
                                         TCSObjectReference<OrderSequence> sequenceRef)
      throws ObjectUnknownException {
    synchronized (globalSyncObject) {
      model.setVehicleOrderSequence(vehicleRef, sequenceRef);
    }
  }

  @Override
  public void updateVehicleOrientationAngle(TCSObjectReference<Vehicle> ref, double angle)
      throws ObjectUnknownException {
    synchronized (globalSyncObject) {
      model.setVehicleOrientationAngle(ref, angle);
    }
  }

  @Override
  public void updateVehiclePosition(TCSObjectReference<Vehicle> vehicleRef,
                                    TCSObjectReference<Point> pointRef)
      throws ObjectUnknownException {
    synchronized (globalSyncObject) {
      LOG.debug("Vehicle {} has reached point {}.", vehicleRef, pointRef);
      model.setVehiclePosition(vehicleRef, pointRef);
    }
  }

  @Override
  public void updateVehiclePrecisePosition(TCSObjectReference<Vehicle> ref, Triple position)
      throws ObjectUnknownException {
    synchronized (globalSyncObject) {
      model.setVehiclePrecisePosition(ref, position);
    }
  }

  @Override
  public void updateVehicleProcState(TCSObjectReference<Vehicle> ref, Vehicle.ProcState state)
      throws ObjectUnknownException {
    synchronized (globalSyncObject) {
      LOG.debug("Updating procState of vehicle {} to {}...", ref.getName(), state);
      model.setVehicleProcState(ref, state);
    }
  }

  @Override
  public void updateVehicleRechargeOperation(TCSObjectReference<Vehicle> ref,
                                             String rechargeOperation)
      throws ObjectUnknownException {
    synchronized (globalSyncObject) {
      model.setVehicleRechargeOperation(ref, rechargeOperation);
    }
  }

  @Override
  public void updateVehicleRouteProgressIndex(TCSObjectReference<Vehicle> ref, int index)
      throws ObjectUnknownException {
    synchronized (globalSyncObject) {
      model.setVehicleRouteProgressIndex(ref, index);
    }
  }

  @Override
  public void updateVehicleState(TCSObjectReference<Vehicle> ref, Vehicle.State state)
      throws ObjectUnknownException {
    synchronized (globalSyncObject) {
      model.setVehicleState(ref, state);
    }
  }

  @Override
  public void updateVehicleTransportOrder(TCSObjectReference<Vehicle> vehicleRef,
                                          TCSObjectReference<TransportOrder> orderRef)
      throws ObjectUnknownException {
    synchronized (globalSyncObject) {
      model.setVehicleTransportOrder(vehicleRef, orderRef);
    }
  }

  @Override
  public void attachCommAdapter(TCSObjectReference<Vehicle> ref,
                                VehicleCommAdapterDescription description)
      throws ObjectUnknownException {
    synchronized (globalSyncObject) {
      attachmentManager.attachAdapterToVehicle(ref.getName(),
                                               commAdapterRegistry.findFactoryFor(description));
    }
  }

  @Override
  public void disableCommAdapter(TCSObjectReference<Vehicle> ref)
      throws ObjectUnknownException {
    synchronized (globalSyncObject) {
      VehicleEntry entry = vehicleEntryPool.getEntryFor(ref.getName());
      if (entry == null) {
        throw new IllegalArgumentException("No vehicle entry found for" + ref.getName());
      }

      entry.getCommAdapter().disable();
    }
  }

  @Override
  public void enableCommAdapter(TCSObjectReference<Vehicle> ref)
      throws ObjectUnknownException {
    synchronized (globalSyncObject) {
      VehicleEntry entry = vehicleEntryPool.getEntryFor(ref.getName());
      if (entry == null) {
        throw new IllegalArgumentException("No vehicle entry found for " + ref.getName());
      }

      entry.getCommAdapter().enable();
    }
  }

  @Override
  public AttachmentInformation fetchAttachmentInformation(TCSObjectReference<Vehicle> ref)
      throws ObjectUnknownException {
    synchronized (globalSyncObject) {
      return attachmentManager.getAttachmentInformation(ref.getName());
    }
  }

  @Override
  public VehicleProcessModelTO fetchProcessModel(TCSObjectReference<Vehicle> ref)
      throws ObjectUnknownException {
    synchronized (globalSyncObject) {
      VehicleEntry entry = vehicleEntryPool.getEntryFor(ref.getName());
      if (entry == null) {
        throw new IllegalArgumentException("No vehicle entry found for " + ref.getName());
      }

      return entry.getCommAdapter().createTransferableProcessModel();
    }
  }

  @Override
  public void sendCommAdapterCommand(TCSObjectReference<Vehicle> ref, AdapterCommand command)
      throws ObjectUnknownException {
    synchronized (globalSyncObject) {
      vehicleControllerPool
          .getVehicleController(ref.getName())
          .sendCommAdapterCommand(command);
    }
  }

  @Override
  public void sendCommAdapterMessage(TCSObjectReference<Vehicle> ref, Object message)
      throws ObjectUnknownException {
    synchronized (globalSyncObject) {
      vehicleControllerPool
          .getVehicleController(ref.getName())
          .sendCommAdapterMessage(message);
    }
  }

  @Override
  public void updateVehicleIntegrationLevel(TCSObjectReference<Vehicle> ref,
                                            Vehicle.IntegrationLevel integrationLevel)
      throws ObjectUnknownException, KernelRuntimeException {
    synchronized (globalSyncObject) {
      Vehicle vehicle = fetchObject(Vehicle.class, ref);

      if (vehicle.isProcessingOrder()
          && (integrationLevel == Vehicle.IntegrationLevel.TO_BE_IGNORED
              || integrationLevel == Vehicle.IntegrationLevel.TO_BE_NOTICED)) {
        throw new IllegalArgumentException(
            String.format("%s: Cannot change integration level to %s while processing orders.",
                          vehicle.getName(),
                          integrationLevel.name())
        );
      }

      model.setVehicleIntegrationLevel(ref, integrationLevel);
    }
  }

  @Override
  public void updateVehicleAllowedOrderTypes(TCSObjectReference<Vehicle> ref,
                                             Set<String> allowedOrderTypes)
      throws ObjectUnknownException {
    synchronized (globalSyncObject) {
      model.setVehicleAllowedOrderTypes(ref, allowedOrderTypes);
    }
  }
}
