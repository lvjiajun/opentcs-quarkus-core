/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.commadapter.peripheral.loopback;

import io.quarkus.arc.config.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * Provides methods to configure to {@link LoopbackPeripheralCommAdapter}.
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
@ConfigProperties(prefix = VirtualPeripheralConfiguration.PREFIX)
public interface VirtualPeripheralConfiguration {

  /**
   * This configuration's prefix.
   */
  String PREFIX = "virtualperipheral";

  @ConfigProperty(name = "enable")
  boolean enable();
}
