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
 * Provides methods to configure the {@link OrderCleanerTask}.
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
@ConfigProperties(prefix = OrderPoolConfiguration.PREFIX)
public interface OrderPoolConfiguration {

  /**
   * This configuration's prefix.
   */
  String PREFIX = "orderpool";

  @ConfigProperty(name = "sweepInterval")
  long sweepInterval();

  @ConfigProperty(name = "sweepAge")
  int sweepAge();
}
