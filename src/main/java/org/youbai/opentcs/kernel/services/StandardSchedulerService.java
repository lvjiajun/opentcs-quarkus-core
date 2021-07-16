/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.kernel.services;

import static java.util.Objects.requireNonNull;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import io.quarkus.runtime.Startup;
import org.youbai.opentcs.access.SchedulerAllocationState;
import org.youbai.opentcs.components.kernel.Scheduler;
import org.youbai.opentcs.components.kernel.services.SchedulerService;
import org.youbai.opentcs.kernel.GlobalSyncObject;

/**
 * This class is the standard implementation of the {@link SchedulerService} interface.
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
@Singleton
public class StandardSchedulerService
    implements SchedulerService {

  /**
   * A global object to be used for synchronization within the kernel.
   */
  private final Object globalSyncObject;
  /**
   * The scheduler.
   */
  @Inject
  @Named("bindScheduler")
  public Scheduler scheduler;

  /**
   * Creates a new instance.
   *
   * @param globalSyncObject The kernel threads' global synchronization object.
   */

  public StandardSchedulerService(GlobalSyncObject globalSyncObject) {
    this.globalSyncObject = requireNonNull(globalSyncObject, "globalSyncObject");
  }

  @Override
  public SchedulerAllocationState fetchSchedulerAllocations() {
    synchronized (globalSyncObject) {
      return new SchedulerAllocationState(scheduler.getAllocations());
    }
  }
}
