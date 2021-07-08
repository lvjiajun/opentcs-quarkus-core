/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.strategies.basic.routing;

import io.quarkus.arc.config.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * Provides methods to configure the {@link DefaultRouter}.
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
@ConfigProperties(prefix =DefaultRouterConfiguration.PREFIX)
public interface DefaultRouterConfiguration {

  /**
   * This configuration's prefix.
   */
  String PREFIX = "defaultrouter";


      //"Whether to compute a route even if the vehicle is already at the destination."
  @ConfigProperty(name = "routeToCurrentPosition")
  boolean routeToCurrentPosition();

}
