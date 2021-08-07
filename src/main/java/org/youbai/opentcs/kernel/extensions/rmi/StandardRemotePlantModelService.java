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
import java.util.Map;
import static java.util.Objects.requireNonNull;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.inject.Singleton;

import org.youbai.opentcs.access.rmi.factories.SocketFactoryProvider;
import org.youbai.opentcs.access.rmi.services.RemotePlantModelService;
import org.youbai.opentcs.access.to.model.PlantModelCreationTO;
import org.youbai.opentcs.components.kernel.services.PlantModelService;
import org.youbai.opentcs.data.TCSObjectReference;
import org.youbai.opentcs.data.model.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.youbai.opentcs.kernel.annotations.ExecutorServiceAnnotations;
import org.youbai.opentcs.kernel.annotations.StandardPlantModelServiceAnnotations;

/**
 * This class is the standard implementation of the {@link RemotePlantModelService} interface.
 * <p>
 * Upon creation, an instance of this class registers itself with the RMI registry by the name
 * </p>
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
@Singleton
public class StandardRemotePlantModelService
    extends StandardRemoteTCSObjectService
    implements RemotePlantModelService {

  /**
   * This class's logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(StandardRemotePlantModelService.class);
  /**
   * The plant model service to invoke methods on.
   */
  private final PlantModelService plantModelService;
  /**
   * Executes tasks modifying kernel data.
   */
  private final ExecutorService kernelExecutor;


  /**
   * Creates a new instance.
   *
   * @param plantModelService The plant model service.
   * @param kernelExecutor Executes tasks modifying kernel data.
   */

  public StandardRemotePlantModelService(@StandardPlantModelServiceAnnotations PlantModelService plantModelService,
                                         @ExecutorServiceAnnotations ExecutorService kernelExecutor) {
    super(plantModelService,kernelExecutor);
    this.plantModelService = requireNonNull(plantModelService, "plantModelService");
    this.kernelExecutor = requireNonNull(kernelExecutor, "kernelExecutor");
  }

  @Override
  public void createPlantModel(PlantModelCreationTO to) {
    try {
      kernelExecutor.submit(() -> plantModelService.createPlantModel(to)).get();
    }
    catch (InterruptedException | ExecutionException exc) {
      throw findSuitableExceptionFor(exc);
    }
  }

  @Override
  public String getModelName() {

    return plantModelService.getModelName();
  }

  @Override
  public Map<String, String> getModelProperties() {

    return plantModelService.getModelProperties();
  }

  @Override
  public void updateLocationLock(TCSObjectReference<Location> ref,
                                 boolean locked) {
    try {
      kernelExecutor.submit(() -> plantModelService.updateLocationLock(ref, locked)).get();
    }
    catch (InterruptedException | ExecutionException exc) {
      throw findSuitableExceptionFor(exc);
    }
  }

  @Override
  public void updateLocationLock(String ref, boolean locked) {
    try {
      kernelExecutor.submit(() -> plantModelService.updateLocationLock(ref, locked)).get();
    }
    catch (InterruptedException | ExecutionException exc) {
      throw findSuitableExceptionFor(exc);
    }
  }
}
