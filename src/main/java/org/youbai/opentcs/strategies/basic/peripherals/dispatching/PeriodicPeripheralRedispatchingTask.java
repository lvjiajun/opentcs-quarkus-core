/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.strategies.basic.peripherals.dispatching;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.youbai.opentcs.components.kernel.services.PeripheralDispatcherService;
import org.youbai.opentcs.components.kernel.services.TCSObjectService;
import org.youbai.opentcs.data.model.Location;
import org.youbai.opentcs.data.model.PeripheralInformation;
import org.youbai.opentcs.kernel.annotations.StandardPeripheralDispatcherServiceAnnotations;
import org.youbai.opentcs.kernel.annotations.StandardTCSObjectAnnotations;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;

import static java.util.Objects.requireNonNull;

/**
 * Periodically checks for idle peripheral devices that could process a peripheral job.
 * 检查可以使用的的空闲peripheral devices
 * @author Martin Grzenia (Fraunhofer IML)
 */
@Dependent
public class PeriodicPeripheralRedispatchingTask
    implements Runnable {

  /**
   * This class's Logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(PeriodicPeripheralRedispatchingTask.class);

  private final PeripheralDispatcherService dispatcherService;

  private final TCSObjectService objectService;

  /**
   * Creates a new instance.
   *
   * @param dispatcherService The dispatcher service used to dispatch peripheral devices.
   * @param objectService The object service.
   */

  public PeriodicPeripheralRedispatchingTask(@StandardPeripheralDispatcherServiceAnnotations PeripheralDispatcherService dispatcherService,
                                             @StandardTCSObjectAnnotations TCSObjectService objectService) {
    this.dispatcherService = requireNonNull(dispatcherService, "dispatcherService");
    this.objectService = requireNonNull(objectService, "objectService");
  }

  @Override
  public void run() {
    // If there are any peripheral devices that could process a peripheral job,
    // trigger the dispatcher once.
    objectService.fetchObjects(Location.class, this::couldProcessJob).stream()
        .findAny()
        .ifPresent(location -> {
          LOG.debug("Peripheral {} could process peripheral job, triggering dispatcher ...",
                    location);
          dispatcherService.dispatch();
        });
  }

  private boolean couldProcessJob(Location location) {
    return location.getPeripheralInformation().getState() != PeripheralInformation.State.NO_PERIPHERAL
        && (processesNoJob(location));
  }

  private boolean processesNoJob(Location location) {
    return location.getPeripheralInformation().getProcState()
        == PeripheralInformation.ProcState.IDLE
        && location.getPeripheralInformation().getState()
        == PeripheralInformation.State.IDLE;
  }
}
