/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.strategies.basic.dispatching;

import io.quarkus.arc.config.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.List;

/**
 * Provides methods to configure the {@link DefaultDispatcher}
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
@ConfigProperties(prefix = DefaultDispatcherConfiguration.PREFIX)
public interface DefaultDispatcherConfiguration {

  /**
   * This configuration's prefix.
   */
  String PREFIX = "defaultdispatcher";

  @ConfigProperty(name = "orderPriorities")
  List<String> orderPriorities();


  @ConfigProperty(name = "vehiclePriorities")
  List<String> vehiclePriorities();


  @ConfigProperty(name = "vehicleCandidatePriorities")
  List<String> vehicleCandidatePriorities();


  @ConfigProperty(name = "orderCandidatePriorities")
  List<String> orderCandidatePriorities();


  @ConfigProperty(name = "deadlineAtRiskPeriod")
  long deadlineAtRiskPeriod();


  @ConfigProperty(name = "assignRedundantOrders")
  boolean assignRedundantOrders();


  @ConfigProperty(name = "dismissUnroutableTransportOrders")
  boolean dismissUnroutableTransportOrders();


  @ConfigProperty(name = "rerouteTrigger")
  RerouteTrigger rerouteTrigger();


  @ConfigProperty(name = "reroutingImpossibleStrategy")
  ReroutingImpossibleStrategy reroutingImpossibleStrategy();


  @ConfigProperty(name = "parkIdleVehicles")
  boolean parkIdleVehicles();


  @ConfigProperty(name = "considerParkingPositionPriorities")
  boolean considerParkingPositionPriorities();


  @ConfigProperty(name = "reparkVehiclesToHigherPriorityPositions")
  boolean reparkVehiclesToHigherPriorityPositions();


  @ConfigProperty(name = "rechargeIdleVehicles")
  boolean rechargeIdleVehicles();


  @ConfigProperty(name = "keepRechargingUntilFullyCharged")
  boolean keepRechargingUntilFullyCharged();


  @ConfigProperty(name = "idleVehicleRedispatchingInterval")
  long idleVehicleRedispatchingInterval();

  enum RerouteTrigger {
    NONE,
    DRIVE_ORDER_FINISHED,
    TOPOLOGY_CHANGE;
  }

  enum ReroutingImpossibleStrategy {
    IGNORE_PATH_LOCKS,
    PAUSE_IMMEDIATELY,
    PAUSE_AT_PATH_LOCK;
  }
}
