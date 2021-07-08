/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.strategies.basic.routing.edgeevaluator;


import io.quarkus.arc.config.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * Provides methods to configure {@link EdgeEvaluatorExplicitProperties}.
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
@ConfigProperties(prefix =ExplicitPropertiesConfiguration.PREFIX)
public interface ExplicitPropertiesConfiguration {

  /**
   * This configuration's prefix.
   */
  String PREFIX = "defaultrouter.edgeevaluator.explicitproperties";

  @ConfigProperty(name = "defaultValue")
  String defaultValue();
}
