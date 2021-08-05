/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.kernel.extensions.rmi;

import java.rmi.registry.Registry;

import static java.util.Objects.requireNonNull;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.inject.Singleton;

import org.youbai.opentcs.access.rmi.factories.SocketFactoryProvider;
import org.youbai.opentcs.access.rmi.services.RegistrationName;
import org.youbai.opentcs.access.rmi.services.RemotePeripheralService;
import org.youbai.opentcs.components.kernel.services.PeripheralService;
import org.youbai.opentcs.data.model.Location;
import org.youbai.opentcs.data.model.TCSResourceReference;
import org.youbai.opentcs.drivers.peripherals.PeripheralAdapterCommand;
import org.youbai.opentcs.drivers.peripherals.PeripheralCommAdapterDescription;
import org.youbai.opentcs.drivers.peripherals.PeripheralProcessModel;
import org.youbai.opentcs.drivers.peripherals.management.PeripheralAttachmentInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.youbai.opentcs.kernel.annotations.ExecutorServiceAnnotations;
import org.youbai.opentcs.kernel.annotations.StandardPeripheralServiceAnnotations;

/**
 * This class is the standard implementation of the {@link RemotePeripheralService} interface.
 * <p>
 * Upon creation, an instance of this class registers itself with the RMI registry by the name
 * declared by {@link RegistrationName#REMOTE_PERIPHERAL_SERVICE}.
 * </p>
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
@Singleton
public class StandardRemotePeripheralService
    extends StandardRemoteTCSObjectService
    implements RemotePeripheralService {

  /**
   * This class's logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(StandardRemotePeripheralService.class);
  /**
   * The peripheral service to invoke methods on.
   */
  private final PeripheralService peripheralService;
  /**
   * Executes tasks modifying kernel data.
   */
  private final ExecutorService kernelExecutor;

  /**
   * Creates a new instance.
   *
   * @param peripheralService The peripheral service.
   * @param kernelExecutor Executes tasks modifying kernel data.
   */

  public StandardRemotePeripheralService(@StandardPeripheralServiceAnnotations PeripheralService peripheralService,
                                         @ExecutorServiceAnnotations ExecutorService kernelExecutor) {
    super(peripheralService, kernelExecutor);
    this.peripheralService = requireNonNull(peripheralService, "peripheralService");
    this.kernelExecutor = requireNonNull(kernelExecutor, "kernelExecutor");
  }


  @Override
  public void attachCommAdapter(TCSResourceReference<Location> ref,
                                PeripheralCommAdapterDescription description) {

    try {
      kernelExecutor.submit(() -> peripheralService.attachCommAdapter(ref, description)).get();
    }
    catch (InterruptedException | ExecutionException exc) {
      throw findSuitableExceptionFor(exc);
    }
  }

  @Override
  public void disableCommAdapter(TCSResourceReference<Location> ref) {
    try {
      kernelExecutor.submit(() -> peripheralService.disableCommAdapter(ref)).get();
    }
    catch (InterruptedException | ExecutionException exc) {
      throw findSuitableExceptionFor(exc);
    }
  }

  @Override
  public void enableCommAdapter(TCSResourceReference<Location> ref) {
    try {
      kernelExecutor.submit(() -> peripheralService.enableCommAdapter(ref)).get();
    }
    catch (InterruptedException | ExecutionException exc) {
      throw findSuitableExceptionFor(exc);
    }
  }

  @Override
  public PeripheralAttachmentInformation fetchAttachmentInformation(
      TCSResourceReference<Location> ref) {

    return peripheralService.fetchAttachmentInformation(ref);
  }

  @Override
  public PeripheralProcessModel fetchProcessModel(TCSResourceReference<Location> ref) {
    return peripheralService.fetchProcessModel(ref);
  }

  @Override
  public void sendCommAdapterCommand(TCSResourceReference<Location> ref,
                                     PeripheralAdapterCommand command) {
    try {
      kernelExecutor.submit(() -> peripheralService.sendCommAdapterCommand(ref, command)).get();
    }
    catch (InterruptedException | ExecutionException exc) {
      throw findSuitableExceptionFor(exc);
    }
  }
}
