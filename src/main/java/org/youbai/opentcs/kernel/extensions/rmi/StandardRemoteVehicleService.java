/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.kernel.extensions.rmi;

import java.rmi.RemoteException;

import static java.util.Objects.requireNonNull;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.youbai.opentcs.access.rmi.factories.SocketFactoryProvider;
import org.youbai.opentcs.access.rmi.services.RemoteVehicleService;
import org.youbai.opentcs.components.kernel.services.VehicleService;
import org.youbai.opentcs.data.TCSObjectReference;
import org.youbai.opentcs.data.model.Vehicle;
import org.youbai.opentcs.drivers.vehicle.AdapterCommand;
import org.youbai.opentcs.drivers.vehicle.VehicleCommAdapterDescription;
import org.youbai.opentcs.drivers.vehicle.management.AttachmentInformation;
import org.youbai.opentcs.drivers.vehicle.management.VehicleProcessModelTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.youbai.opentcs.kernel.annotations.ExecutorServiceAnnotations;
import org.youbai.opentcs.kernel.annotations.StandardVehicleServiceAnnotations;

/**
 * This class is the standard implementation of the {@link RemoteVehicleService} interface.
 * <p>
 * Upon creation, an instance of this class registers itself with the RMI registry by the name
 * </p>
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
@Singleton
public class StandardRemoteVehicleService
    extends StandardRemoteTCSObjectService
    implements RemoteVehicleService {

  /**
   * This class's logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(StandardRemoteVehicleService.class);
  /**
   * The vehicle service to invoke methods on.
   */
  private final VehicleService vehicleService;
  /**
   * Executes tasks modifying kernel data.
   */
  private final ExecutorService kernelExecutor;

  /**
   * Creates a new instance.
   *
   * @param vehicleService The vehicle service.
   * @param kernelExecutor Executes tasks modifying kernel data.
   */
  @Inject
  public StandardRemoteVehicleService(@StandardVehicleServiceAnnotations VehicleService vehicleService,
                                      @ExecutorServiceAnnotations ExecutorService kernelExecutor) {
    super(vehicleService,kernelExecutor);
    this.vehicleService = requireNonNull(vehicleService, "vehicleService");
    this.kernelExecutor = requireNonNull(kernelExecutor, "kernelExecutor");
  }
  @Override
  public void attachCommAdapter(TCSObjectReference<Vehicle> ref,
                                VehicleCommAdapterDescription description) {
    try {
      kernelExecutor.submit(() -> vehicleService.attachCommAdapter(ref, description)).get();
    }
    catch (InterruptedException | ExecutionException exc) {
      throw findSuitableExceptionFor(exc);
    }
  }

  @Override
  public void disableCommAdapter(TCSObjectReference<Vehicle> ref) {
    try {
      kernelExecutor.submit(() -> vehicleService.disableCommAdapter(ref)).get();
    }
    catch (InterruptedException | ExecutionException exc) {
      throw findSuitableExceptionFor(exc);
    }
  }

  @Override
  public void enableCommAdapter(TCSObjectReference<Vehicle> ref) {
    try {
      kernelExecutor.submit(() -> vehicleService.enableCommAdapter(ref)).get();
    }
    catch (InterruptedException | ExecutionException exc) {
      throw findSuitableExceptionFor(exc);
    }
  }

  @Override
  public AttachmentInformation fetchAttachmentInformation(TCSObjectReference<Vehicle> ref) {


    return vehicleService.fetchAttachmentInformation(ref);
  }

  @Override
  public VehicleProcessModelTO fetchProcessModel(TCSObjectReference<Vehicle> ref) {

    return vehicleService.fetchProcessModel(ref);
  }

  @Override
  public void sendCommAdapterCommand(TCSObjectReference<Vehicle> ref,
                                     AdapterCommand command) {

    try {
      kernelExecutor.submit(() -> vehicleService.sendCommAdapterCommand(ref, command)).get();
    }
    catch (InterruptedException | ExecutionException exc) {
      throw findSuitableExceptionFor(exc);
    }
  }

  @Override
  public void sendCommAdapterMessage(TCSObjectReference<Vehicle> vehicleRef,
                                     Object message) {

    try {
      kernelExecutor.submit(() -> vehicleService.sendCommAdapterMessage(vehicleRef, message)).get();
    }
    catch (InterruptedException | ExecutionException exc) {
      throw findSuitableExceptionFor(exc);
    }
  }

  @Override
  public void updateVehicleIntegrationLevel(TCSObjectReference<Vehicle> ref,
                                            Vehicle.IntegrationLevel integrationLevel) {

    try {
      kernelExecutor.submit(
          () -> vehicleService.updateVehicleIntegrationLevel(ref, integrationLevel)
      ).get();
    }
    catch (InterruptedException | ExecutionException exc) {
      throw findSuitableExceptionFor(exc);
    }
  }

  @Override
  public void updateVehicleAllowedOrderTypes(TCSObjectReference<Vehicle> ref,
                                             Set<String> allowedOrderTypes) {

    try {
      kernelExecutor.submit(
          () -> vehicleService.updateVehicleAllowedOrderTypes(ref, allowedOrderTypes))
          .get();
    }
    catch (InterruptedException | ExecutionException exc) {
      throw findSuitableExceptionFor(exc);
    }
  }
}
