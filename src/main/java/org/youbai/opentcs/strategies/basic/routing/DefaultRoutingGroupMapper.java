/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.strategies.basic.routing;

import org.youbai.opentcs.components.kernel.Router;
import static org.youbai.opentcs.components.kernel.Router.PROPKEY_ROUTING_GROUP;
import org.youbai.opentcs.components.kernel.routing.GroupMapper;
import org.youbai.opentcs.data.model.Vehicle;

import javax.inject.Singleton;

/**
 * Determines a vehicle's routing group by reading it's {@link Router#PROPKEY_ROUTING_GROUP}
 * property. Returns {@link #DEFAULT_ROUTING_GROUP} if the property does not exist or is invalid.
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
@Singleton
public class DefaultRoutingGroupMapper
    implements GroupMapper {

  /**
   * The default value of a vehicle's routing group.
   */
  private static final String DEFAULT_ROUTING_GROUP = "";

  @Override
  public String apply(Vehicle vehicle) {
    String propVal = vehicle.getProperty(PROPKEY_ROUTING_GROUP);

    return propVal == null ? DEFAULT_ROUTING_GROUP : propVal;
  }
}
