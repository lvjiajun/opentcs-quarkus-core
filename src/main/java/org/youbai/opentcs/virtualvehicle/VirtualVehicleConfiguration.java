/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.virtualvehicle;

import io.quarkus.arc.config.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * Provides methods to configure to {@link LoopbackCommunicationAdapter}.
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
@ConfigProperties(prefix = VirtualVehicleConfiguration.PREFIX)
public interface VirtualVehicleConfiguration {

  /**
   * This configuration's prefix.
   */
  String PREFIX = "virtualvehicle";

  @ConfigProperty(name = "enable")
  boolean enable();

  @ConfigProperty(name = "commandQueueCapacity")
  int commandQueueCapacity();

  @ConfigProperty(name = "rechargeOperation")
  String rechargeOperation();

  @ConfigProperty(name = "simulationTimeFactor")
  double simulationTimeFactor();
}
