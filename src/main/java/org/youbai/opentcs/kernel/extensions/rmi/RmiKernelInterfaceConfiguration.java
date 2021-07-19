/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.kernel.extensions.rmi;

import io.quarkus.arc.config.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.youbai.opentcs.configuration.ConfigurationEntry;
import org.youbai.opentcs.configuration.ConfigurationPrefix;

/**
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
@ConfigProperties(prefix =RmiKernelInterfaceConfiguration.PREFIX)
public interface RmiKernelInterfaceConfiguration {

  /**
   * This configuration's prefix.
   */
  String PREFIX = "rmikernelinterface";

  @ConfigProperty(name = "enable")
  Boolean enable();

  @ConfigProperty(name = "registryPort")
  int registryPort();

  @ConfigProperty(name = "remoteKernelServicePortalPort")
  int remoteKernelServicePortalPort();

  @ConfigProperty(name = "remotePlantModelServicePort")
  int remotePlantModelServicePort();

  @ConfigProperty(name = "remoteTransportOrderServicePort")
  int remoteTransportOrderServicePort();

  @ConfigProperty(name = "remoteVehicleServicePort")
  int remoteVehicleServicePort();

  @ConfigProperty(name = "remoteNotificationServicePort")
  int remoteNotificationServicePort();

  @ConfigProperty(name = "remoteSchedulerServicePort")
  int remoteSchedulerServicePort();

  @ConfigProperty(name = "remoteRouterServicePort")
  int remoteRouterServicePort();

  @ConfigProperty(name = "remoteDispatcherServicePort")
  int remoteDispatcherServicePort();

  @ConfigProperty(name = "remoteQueryServicePort")
  int remoteQueryServicePort();

  @ConfigProperty(name = "remotePeripheralServicePort")
  int remotePeripheralServicePort();

  @ConfigProperty(name = "remotePeripheralJobServicePort")
  int remotePeripheralJobServicePort();

  @ConfigProperty(name = "clientSweepInterval")
  long clientSweepInterval();

  @ConfigProperty(name = "useSsl")
  boolean useSsl();
}
