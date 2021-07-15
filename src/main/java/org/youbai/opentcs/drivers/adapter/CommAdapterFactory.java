/**
 * Copyright (c) Fraunhofer IML
 */
package org.youbai.opentcs.drivers.adapter;

import org.youbai.opentcs.data.model.Vehicle;
import org.youbai.opentcs.drivers.adapter.exchange.CommAdapterDescription;
import static java.util.Objects.requireNonNull;

import javax.enterprise.context.ApplicationScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.youbai.opentcs.drivers.vehicle.VehicleCommAdapter;
import org.youbai.opentcs.drivers.vehicle.VehicleCommAdapterDescription;
import org.youbai.opentcs.drivers.vehicle.VehicleCommAdapterFactory;
import static org.youbai.opentcs.common.extend.dispatching.VehicleProperties.PROPKEY_VEHICLE_HOST;
import static org.youbai.opentcs.common.extend.dispatching.VehicleProperties.PROPKEY_VEHICLE_PORT;
import static org.youbai.opentcs.util.Assertions.checkInRange;
@ApplicationScoped
public class CommAdapterFactory
    implements VehicleCommAdapterFactory {

  /**
   * This class's Logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(CommAdapterFactory.class);

  /**
   * The factory to create components specific to the comm adapter.
   */
  private final AdapterComponentsFactory componentsFactory;
  /**
   * This component's initialized flag.
   */
  private boolean initialized;

  /**
   * Creates a new instance.
   *
   * @param componentsFactory The factory to create components specific to the comm adapter.
   */

  public CommAdapterFactory(AdapterComponentsFactory componentsFactory) {
    this.componentsFactory = requireNonNull(componentsFactory, "componentsFactory");
  }

  @Override
  public void initialize() {
    if (initialized) {
      LOG.debug("Already initialized.");
      return;
    }
    initialized = true;
  }

  @Override
  public boolean isInitialized() {
    return initialized;
  }

  @Override
  public void terminate() {
    if (!initialized) {
      LOG.debug("Not initialized.");
      return;
    }
    initialized = false;
  }

  @Override
  public VehicleCommAdapterDescription getDescription() {
    return new CommAdapterDescription();
  }

  @Override
  public boolean providesAdapterFor(Vehicle vehicle) {
    requireNonNull(vehicle, "vehicle");

    if (vehicle.getProperty(PROPKEY_VEHICLE_HOST) == null) {
      return false;
    }

    if (vehicle.getProperty(PROPKEY_VEHICLE_PORT) == null) {
      return false;
    }
    try {
      checkInRange(Integer.parseInt(vehicle.getProperty(PROPKEY_VEHICLE_PORT)),
                   1024,
                   65535);
    }
    catch (IllegalArgumentException exc) {
      return false;
    }

    return true;
  }

  @Override
  public VehicleCommAdapter getAdapterFor(Vehicle vehicle) {
    requireNonNull(vehicle, "vehicle");
    if (!providesAdapterFor(vehicle)) {
      return null;
    }

    CommAdapter adapter = componentsFactory.createCommAdapter(vehicle);
    adapter.getProcessModel().setVehicleHost(vehicle.getProperty(PROPKEY_VEHICLE_HOST));
    adapter.getProcessModel().setVehiclePort(
        Integer.parseInt(vehicle.getProperty(PROPKEY_VEHICLE_PORT))
    );
    return adapter;
  }
}
