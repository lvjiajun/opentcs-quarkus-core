/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.components.kernel.services;

import java.util.Set;
import javax.annotation.Nullable;
import org.youbai.opentcs.access.KernelRuntimeException;
import org.youbai.opentcs.data.ObjectUnknownException;
import org.youbai.opentcs.data.TCSObjectReference;
import org.youbai.opentcs.data.model.Location;
import org.youbai.opentcs.data.model.TCSResource;
import org.youbai.opentcs.data.model.TCSResourceReference;

/**
 * Declares the methods the plant model service must provide which are not accessible to remote
 * peers.
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
public interface InternalPlantModelService
    extends PlantModelService {

  /**
   * Expands a set of resources <em>A</em> to a set of resources <em>B</em>.
   * <em>B</em> contains the resources in <em>A</em> with blocks expanded to their actual members.
   * The given set is not modified.
   *
   * @param resources The set of resources to be expanded.
   * @return The given set with resources expanded.
   * @throws ObjectUnknownException If any of the referenced objects does not exist.
   */
  Set<TCSResource<?>> expandResources(Set<TCSResourceReference<?>> resources)
      throws ObjectUnknownException;

  /**
   * Loads the saved model into the kernel.
   * If there is no saved model, a new empty model will be loaded.
   *
   * @throws IllegalStateException If the model cannot be loaded.
   */
  void loadPlantModel()
      throws IllegalStateException;

  /**
   * Saves the current model under the given name.
   * If there is a saved model, it will be overwritten.
   *
   * @throws IllegalStateException If the model could not be persisted for some reason.
   */
  void savePlantModel()
      throws IllegalStateException;

  /**
   * Updates a location's reservation token.
   *
   * @param ref A reference to the location to be updated.
   * @param token The location's new reservation token.
   * @throws ObjectUnknownException If the referenced location does not exist.
   * @throws KernelRuntimeException In case there is an exception executing this method.
   */
  void updateLocationReservationToken(TCSObjectReference<Location> ref, @Nullable String token)
      throws ObjectUnknownException, KernelRuntimeException;
}
