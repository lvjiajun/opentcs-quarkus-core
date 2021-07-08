/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.kernel.extensions.statistics;

import io.quarkus.arc.config.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * Configuration entries for the statistics collector.
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
@ConfigProperties(prefix = StatisticsCollectorConfiguration.PREFIX)
public interface StatisticsCollectorConfiguration {

  String PREFIX = "statisticscollector";

  @ConfigProperty(name = "enable")
  boolean enable();
}
