/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.strategies.basic.peripherals.dispatching;

import io.quarkus.arc.config.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * Provides methods to configure the {@link DefaultPeripheralJobDispatcher}
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
@ConfigProperties(prefix =DefaultPeripheralJobDispatcherConfiguration.PREFIX)
public interface DefaultPeripheralJobDispatcherConfiguration {

  /**
   * This configuration's prefix.
   */
  String PREFIX = "defaultperipheraljobdispatcher";


  @ConfigProperty(name = "idlePeripheralRedispatchingInterval")
  long idlePeripheralRedispatchingInterval();
}
