/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.strategies.basic.peripherals.dispatching;

import io.smallrye.config.ConfigMapping;
import org.youbai.opentcs.configuration.ConfigurationEntry;

/**
 * Provides methods to configure the {@link DefaultPeripheralJobDispatcher}
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
@ConfigMapping(prefix =DefaultPeripheralJobDispatcherConfiguration.PREFIX)
public interface DefaultPeripheralJobDispatcherConfiguration {

  /**
   * This configuration's prefix.
   */
  String PREFIX = "defaultperipheraljobdispatcher";

  @ConfigurationEntry(
      type = "Integer",
      description = "The interval between redispatching of peripheral devices.",
      orderKey = "9_misc")
  long idlePeripheralRedispatchingInterval();
}
