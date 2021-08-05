/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.kernel.extensions.rmi;

import java.rmi.registry.Registry;

import static java.util.Objects.requireNonNull;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.youbai.opentcs.access.SchedulerAllocationState;
import org.youbai.opentcs.access.rmi.factories.SocketFactoryProvider;
import org.youbai.opentcs.access.rmi.services.RemoteSchedulerService;
import org.youbai.opentcs.components.kernel.services.SchedulerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.youbai.opentcs.kernel.annotations.StandardSchedulerServiceAnnotations;

/**
 * This class is the standard implementation of the {@link RemoteSchedulerService} interface.
 * <p>
 * Upon creation, an instance of this class registers itself with the RMI registry by the name
 * </p>
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
@ApplicationScoped
public class StandardRemoteSchedulerService
    extends KernelRemoteService
    implements RemoteSchedulerService {

  /**
   * This class's logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(StandardRemoteSchedulerService.class);
  /**
   * The scheduler service to invoke methods on.
   */
  @StandardSchedulerServiceAnnotations
  SchedulerService schedulerService;

  /**
   * Creates a new instance.
   *
   */

  public StandardRemoteSchedulerService() {

  }
  @Override
  public SchedulerAllocationState fetchSchedulerAllocations() {

    return schedulerService.fetchSchedulerAllocations();
  }
}
