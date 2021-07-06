/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.drivers.peripherals.management;

import java.util.List;
import javax.annotation.Nonnull;
import org.youbai.opentcs.components.Lifecycle;
import org.youbai.opentcs.data.model.Location;
import org.youbai.opentcs.data.model.TCSResourceReference;
import org.youbai.opentcs.drivers.peripherals.PeripheralCommAdapterDescription;
import org.youbai.opentcs.drivers.peripherals.PeripheralProcessModel;

/**
 * Provides peripheral comm adapter specific panels used for interaction and displaying information.
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
public interface PeripheralCommAdapterPanelFactory
    extends Lifecycle {

  /**
   * Returns a list of {@link PeripheralCommAdapterPanel}s.
   *
   * @param description The description to create panels for.
   * @param location The location to create panels for.
   * @param processModel The current state of the process model a panel may want to initialize its
   * components with.
   * @return A list of comm adapter panels, or an empty list, if this factory cannot provide panels
   * for the given description.
   */
  List<PeripheralCommAdapterPanel> getPanelsFor(
      @Nonnull PeripheralCommAdapterDescription description,
      @Nonnull TCSResourceReference<Location> location,
      @Nonnull PeripheralProcessModel processModel);
}
