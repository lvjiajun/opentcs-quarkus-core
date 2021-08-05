/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.access;

import java.util.List;
import javax.annotation.Nonnull;
import org.youbai.opentcs.components.kernel.services.DispatcherService;
import org.youbai.opentcs.components.kernel.services.NotificationService;
import org.youbai.opentcs.components.kernel.services.PeripheralDispatcherService;
import org.youbai.opentcs.components.kernel.services.PeripheralJobService;
import org.youbai.opentcs.components.kernel.services.PeripheralService;
import org.youbai.opentcs.components.kernel.services.PlantModelService;
import org.youbai.opentcs.components.kernel.services.QueryService;
import org.youbai.opentcs.components.kernel.services.RouterService;
import org.youbai.opentcs.components.kernel.services.SchedulerService;
import org.youbai.opentcs.components.kernel.services.TransportOrderService;
import org.youbai.opentcs.components.kernel.services.VehicleService;

/**
 * Provides clients access to kernel services.
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
public interface KernelServicePortal {
  /**
   * Returns the current state of the kernel.
   *
   * @return The current state of the kernel.
   * @throws KernelRuntimeException In case there is an exception executing this method.
   */
  Kernel.State getState()
      throws KernelRuntimeException;


  /**
   * Publishes an event.
   *
   * @param event The event to be published.
   * @throws KernelRuntimeException In case there is an exception executing this method.
   */
  void publishEvent(Object event)
      throws KernelRuntimeException;

  /**
   * Returns the service a client can use to access methods regarding the plant model.
   *
   * @return The service a client can use to access methods regarding the plant model.
   */
  @Nonnull
  PlantModelService getPlantModelService();

  /**
   * Returns the service a client can use to access methods regarding transport orders and order
   * sequences.
   *
   * @return The service a client can use to access methods regarding transport orders and order
   * sequences.
   */
  @Nonnull
  TransportOrderService getTransportOrderService();

  /**
   * Returns the service a client can use to access methods regarding vehicles.
   *
   * @return The service a client can use to access methods regarding vehicles.
   */
  @Nonnull
  VehicleService getVehicleService();

  /**
   * Returns the service a client can use to access methods regarding user notifications.
   *
   * @return The service a client can use to access methods regarding user notifications.
   */
  @Nonnull
  NotificationService getNotificationService();

  /**
   * Returns the service a client can use to access methods regarding the dispatcher.
   *
   * @return The service a client can use to access methods regarding the dispatcher.
   */
  @Nonnull
  DispatcherService getDispatcherService();

  /**
   * Returns the service a client can use to access methods regarding the router.
   *
   * @return The service a client can use to access methods regarding the router.
   */
  @Nonnull
  RouterService getRouterService();

  /**
   * Returns the service a client can use to access methods regarding the scheduler.
   *
   * @return The service a client can use to access methods regarding the scheduler.
   */
  @Nonnull
  SchedulerService getSchedulerService();

  /**
   * Returns the service a client can use to access methods for generic queries.
   *
   * @return The service a client can use to access methods for generic queries.
   */
  @Nonnull
  QueryService getQueryService();

  /**
   * Returns the service a client can use to access methods regarding peripherals.
   *
   * @return The service a client can use to access methods regarding peripherals.
   */
  @Nonnull
  PeripheralService getPeripheralService();

  /**
   * Returns the service a client can use to access methods regarding peripheral jobs.
   *
   * @return The service a client can use to access methods regarding peripheral jobs.
   */
  @Nonnull
  PeripheralJobService getPeripheralJobService();
  
  /**
   * Returns the service a client can use to access methods regarding the peripheral dispatcher.
   *
   * @return The service a client can use to access methods regarding the peripheral dispatcher.
   */
  @Nonnull
  PeripheralDispatcherService getPeripheralDispatcherService();
}
