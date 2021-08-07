/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.components.kernel.services;

import java.util.Map;
import org.youbai.opentcs.access.KernelRuntimeException;
import org.youbai.opentcs.access.to.model.PlantModelCreationTO;
import org.youbai.opentcs.data.ObjectExistsException;
import org.youbai.opentcs.data.ObjectUnknownException;
import org.youbai.opentcs.data.TCSObjectReference;
import org.youbai.opentcs.data.model.Location;

/**
 * Provides methods concerning the plant model.
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
public interface PlantModelService
    extends TCSObjectService {

  /**
   * Creates a new plant model with the objects described in the given transfer object.
   * Implicitly saves/persists the new plant model.
   *
   * @param to The transfer object describing the plant model objects to be created.
   * @throws ObjectUnknownException If any referenced object does not exist.
   * @throws ObjectExistsException If an object with the same name already exists in the model.
   * @throws KernelRuntimeException In case there is an exception executing this method.
   * @throws IllegalStateException If there was a problem persisting the model.
   */
  void createPlantModel(PlantModelCreationTO to)
      throws ObjectUnknownException, ObjectExistsException, KernelRuntimeException,
             IllegalStateException;

  /**
   * Returns the name of the model that is currently loaded in the kernel.
   *
   * @return The name of the currently loaded model.
   * @throws KernelRuntimeException In case there is an exception executing this method.
   */
  String getModelName()
      throws KernelRuntimeException;

  /**
   * Returns the model's properties.
   *
   * @return The model's properties.
   * @throws KernelRuntimeException In case there is an exception executing this method.
   */
  Map<String, String> getModelProperties()
      throws KernelRuntimeException;

  /**
   * Updates a location's lock state.
   *
   * @param ref A reference to the location to be updated.
   * @param locked Indicates whether the location is to be locked ({@code true}) or unlocked
   * ({@code false}).
   * @throws ObjectUnknownException If the referenced location does not exist.
   * @throws KernelRuntimeException In case there is an exception executing this method.
   */
  void updateLocationLock(TCSObjectReference<Location> ref, boolean locked)
      throws ObjectUnknownException, KernelRuntimeException;
  void updateLocationLock(String ref, boolean locked)
          throws ObjectUnknownException, KernelRuntimeException;
}
