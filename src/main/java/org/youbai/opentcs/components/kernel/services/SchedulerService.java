/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.components.kernel.services;

import org.youbai.opentcs.access.KernelRuntimeException;
import org.youbai.opentcs.access.SchedulerAllocationState;
import org.youbai.opentcs.components.kernel.Scheduler;

/**
 * Provides methods concerning the {@link Scheduler}.
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
public interface SchedulerService {

  /**
   * Returns the current state of resource allocations.
   *
   * @return The current state of resource allocations.
   * @throws KernelRuntimeException In case there is an exception executing this method.
   */
  SchedulerAllocationState fetchSchedulerAllocations()
      throws KernelRuntimeException;
}
