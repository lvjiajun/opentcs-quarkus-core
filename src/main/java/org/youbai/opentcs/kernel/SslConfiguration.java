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
 * Provides methods to configure the ssl connection.
 *
 * @author Mats Wilhelm (Fraunhofer IML)
 */
@ConfigProperties(prefix = SslConfiguration.PREFIX)
public interface SslConfiguration {

  /**
   * This configuration's prefix.
   */
  String PREFIX = "ssl";


  @ConfigProperty(name = "keystoreFile")
  String keystoreFile();


  @ConfigProperty(name = "keystorePassword")
  String keystorePassword();


  @ConfigProperty(name = "truststoreFile")
  String truststoreFile();


  @ConfigProperty(name = "truststorePassword")
  String truststorePassword();

}
