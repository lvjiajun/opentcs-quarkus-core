/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.drivers.vehicle.management;

import java.io.Serializable;
import org.youbai.opentcs.drivers.LowLevelCommunicationEvent;

/**
 * Instances of this class represent events emitted by/for comm adapter changes.
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
public abstract class CommAdapterEvent
    implements LowLevelCommunicationEvent,
               Serializable {

  /**
   * Creates an empty event.
   */
  public CommAdapterEvent() {
  }
}
