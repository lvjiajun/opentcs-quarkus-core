/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.kernel.extensions.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;

import static java.util.Objects.requireNonNull;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.youbai.opentcs.access.rmi.factories.SocketFactoryProvider;
import org.youbai.opentcs.access.rmi.services.RegistrationName;
import org.youbai.opentcs.access.rmi.services.RemotePeripheralJobService;
import org.youbai.opentcs.access.to.peripherals.PeripheralJobCreationTO;
import org.youbai.opentcs.components.kernel.services.PeripheralJobService;
import org.youbai.opentcs.data.peripherals.PeripheralJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.youbai.opentcs.kernel.annotations.ExecutorServiceAnnotations;
import org.youbai.opentcs.kernel.annotations.StandardPeripheralJobServiceAnnotations;

/**
 * This class is the standard implementation of the {@link RemotePeripheralJobService} interface.
 * <p>
 * Upon creation, an instance of this class registers itself with the RMI registry by the name
 * declared by {@link RegistrationName#REMOTE_PERIPHERAL_JOB_SERVICE}.
 * </p>
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
@Singleton
public class StandardRemotePeripheralJobService
    extends StandardRemoteTCSObjectService
    implements RemotePeripheralJobService {

  /**
   * This class's logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(StandardRemotePeripheralJobService.class);
  /**
   * The peripheral job service to invoke methods on.
   */
  private final PeripheralJobService peripheralJobService;
  /**
   * Executes tasks modifying kernel data.
   */
  private final ExecutorService kernelExecutor;

  /**
   * Creates a new instance.
   *
   * @param peripheralJobService The peripheral job service.
   * @param kernelExecutor Executes tasks modifying kernel data.
   */
  @Inject
  public StandardRemotePeripheralJobService(@StandardPeripheralJobServiceAnnotations PeripheralJobService peripheralJobService,
                                            @ExecutorServiceAnnotations ExecutorService kernelExecutor) {
    super(peripheralJobService,kernelExecutor);
    this.peripheralJobService = requireNonNull(peripheralJobService, "transportOrderService");
    this.kernelExecutor = requireNonNull(kernelExecutor, "kernelExecutor");
  }
  @Override
  public PeripheralJob createPeripheralJob(PeripheralJobCreationTO to) {
    try {
      return kernelExecutor.submit(() -> peripheralJobService.createPeripheralJob(to)).get();
    }
    catch (InterruptedException | ExecutionException exc) {
      throw findSuitableExceptionFor(exc);
    }
  }
}
