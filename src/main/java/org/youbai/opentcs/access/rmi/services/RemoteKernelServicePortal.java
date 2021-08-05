/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.access.rmi.services;

import org.youbai.opentcs.access.CredentialsException;
import org.youbai.opentcs.access.Kernel;
import org.youbai.opentcs.access.KernelServicePortal;

import java.rmi.RemoteException;

/**
 * Declares the methods provided by the {@link KernelServicePortal} via RMI.
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */

public interface RemoteKernelServicePortal{

  /**
   * Introduce the calling client to the server and authenticate for operations.
   *
   * @return An identification object that is required for subsequent method calls.
   * @throws CredentialsException If authentication with the given username and password failed.
   * @throws RemoteException If there was an RMI-related problem.
   */

  Kernel.State getState();

  void publishEvent(Object event);
}
