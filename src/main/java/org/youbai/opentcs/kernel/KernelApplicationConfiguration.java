/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.kernel;

import io.quarkus.arc.config.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * Provides common kernel configuration entries.
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
@ConfigProperties(prefix = KernelApplicationConfiguration.PREFIX)
public interface KernelApplicationConfiguration {
  /**
   * This configuration's prefix.
   */
  String PREFIX = "kernelapp";

  @ConfigProperty(name = "autoEnableDriversOnStartup")
  boolean autoEnableDriversOnStartup();

  @ConfigProperty(name = "autoEnablePeripheralDriversOnStartup")
  boolean autoEnablePeripheralDriversOnStartup();

  @ConfigProperty(name = "saveModelOnTerminateModelling")
  boolean saveModelOnTerminateModelling();

  @ConfigProperty(name = "saveModelOnTerminateOperating")
  boolean saveModelOnTerminateOperating();

  @ConfigProperty(name = "updateRoutingTopologyOnPathLockChange")
  boolean updateRoutingTopologyOnPathLockChange();

  @ConfigProperty(name = "thread.corePoolSize")
  int corePoolSize();

  @ConfigProperty(name = "thread.maximumPoolSize")
  int maximumPoolSize();

  @ConfigProperty(name = "thread.keepAliveTime")
  int keepAliveTime();
}
