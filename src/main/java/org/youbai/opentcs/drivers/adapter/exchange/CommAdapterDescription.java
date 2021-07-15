/**
 * Copyright (c) Fraunhofer IML
 */
package org.youbai.opentcs.drivers.adapter.exchange;

import java.util.ResourceBundle;
import org.youbai.opentcs.drivers.vehicle.VehicleCommAdapterDescription;

/**
 * The comm adapter's {@link VehicleCommAdapterDescription}.
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
public class CommAdapterDescription
    extends VehicleCommAdapterDescription {

  @Override
  public String getDescription() {
    return "Example communication adapter";
/*    return ResourceBundle.getBundle("de/fraunhofer/iml/opentcs/example/commadapter/vehicle/Bundle").
        getString("ExampleAdapterFactoryDescription");*/
  }

  @Override
  public boolean isSimVehicleCommAdapter() {
    return false;
  }
}
