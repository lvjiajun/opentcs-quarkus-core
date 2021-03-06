/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.strategies.basic.dispatching.selection.vehicles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import static java.util.Objects.requireNonNull;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.youbai.opentcs.components.kernel.services.TCSObjectService;
import org.youbai.opentcs.data.TCSObjectReference;
import org.youbai.opentcs.data.model.Point;
import org.youbai.opentcs.data.model.Vehicle;
import org.youbai.opentcs.kernel.annotations.StandardTCSObjectAnnotations;
import org.youbai.opentcs.strategies.basic.dispatching.selection.ReparkVehicleSelectionFilter;

/**
 * Filters vehicles that are reparkable.
 *
 * @author Stefan Walter (Fraunhofer IML)
 */

public class IsReparkable
    implements ReparkVehicleSelectionFilter {

  /**
   * The object service.
   */
  private final TCSObjectService objectService;

  /**
   * Creates a new instance.
   *
   * @param objectService The object service.
   */

  public IsReparkable(@StandardTCSObjectAnnotations TCSObjectService objectService) {
    this.objectService = requireNonNull(objectService, "objectService");
  }

  @Override
  public Collection<String> apply(Vehicle vehicle) {
    return reparkable(vehicle) ? new ArrayList<>() : Arrays.asList(getClass().getName());
  }

  private boolean reparkable(Vehicle vehicle) {
    return vehicle.getIntegrationLevel() == Vehicle.IntegrationLevel.TO_BE_UTILIZED
        && vehicle.hasProcState(Vehicle.ProcState.IDLE)
        && vehicle.hasState(Vehicle.State.IDLE)
        && isParkingPosition(vehicle.getCurrentPosition())
        && vehicle.getOrderSequence() == null;
  }

  private boolean isParkingPosition(TCSObjectReference<Point> positionRef) {
    if (positionRef == null) {
      return false;
    }

    Point position = objectService.fetchObject(Point.class, positionRef);
    return position.isParkingPosition();
  }
}
