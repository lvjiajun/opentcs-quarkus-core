/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.kernel.extensions.controlcenter.vehicles;

import com.google.common.base.Strings;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import static java.util.Objects.requireNonNull;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.youbai.opentcs.components.Lifecycle;
import org.youbai.opentcs.components.kernel.services.TCSObjectService;
import org.youbai.opentcs.customizations.ApplicationEventBus;
import org.youbai.opentcs.data.model.Vehicle;
import org.youbai.opentcs.drivers.vehicle.VehicleCommAdapter;
import org.youbai.opentcs.drivers.vehicle.VehicleCommAdapterDescription;
import org.youbai.opentcs.drivers.vehicle.VehicleCommAdapterFactory;
import org.youbai.opentcs.drivers.vehicle.management.AttachmentEvent;
import org.youbai.opentcs.drivers.vehicle.management.AttachmentInformation;
import org.youbai.opentcs.drivers.vehicle.management.ProcessModelEvent;
import org.youbai.opentcs.drivers.vehicle.management.VehicleProcessModelTO;
import org.youbai.opentcs.kernel.KernelApplicationConfiguration;
import org.youbai.opentcs.kernel.annotations.SimpleEventBusAnnotation;
import org.youbai.opentcs.kernel.annotations.StandardTCSObjectAnnotations;
import org.youbai.opentcs.kernel.vehicles.LocalVehicleControllerPool;
import org.youbai.opentcs.kernel.vehicles.VehicleCommAdapterRegistry;
import org.youbai.opentcs.util.Assertions;
import org.youbai.opentcs.util.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages attachment and detachment of communication adapters to vehicles.
 *
 * @author Stefan Walter (Fraunhofer IML)
 * @author Martin Grzenia (Fraunhofer IML)
 */
@Singleton
public class AttachmentManager
    implements Lifecycle {

  /**
   * This class's logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(AttachmentManager.class);
  /**
   * This class's configuration.
   */
  public final KernelApplicationConfiguration configuration;
  /**
   * The object service.
   */
  @Nonnull
  @StandardTCSObjectAnnotations
  @Inject
  public TCSObjectService objectService;
  /**
   * The vehicle controller pool.
   */
  @Nonnull
  @Inject
  public LocalVehicleControllerPool controllerPool;
  /**
   * The comm adapter registry.
   */
  @Nonnull
  @Inject
  public VehicleCommAdapterRegistry commAdapterRegistry;
  /**
   * The pool of vehicle entries.
   */
  @Nonnull
  @Inject
  public VehicleEntryPool vehicleEntryPool;
  /**
   * The handler to send events to.
   */
  @Nonnull
  @SimpleEventBusAnnotation
  @Inject
  public EventHandler eventHandler;
  /**
   * The pool of comm adapter attachments.
   */
  private final Map<String, AttachmentInformation> attachmentPool = new HashMap<>();
  /**
   * Whether the attachment manager is initialized or not.
   */
  private boolean initialized;

  /**
   * Creates a new instance.
   *
   * @param configuration This class's configuration.
   */

  public AttachmentManager(KernelApplicationConfiguration configuration) {
    this.configuration = requireNonNull(configuration, "configuration");
  }

  @Override
  public void initialize() {
    if (isInitialized()) {
      LOG.debug("Already initialized.");
      return;
    }

    commAdapterRegistry.initialize();
    vehicleEntryPool.initialize();

    initAttachmentPool();

    autoAttachAllAdapters();

    if (configuration.autoEnableDriversOnStartup()) {
      autoEnableAllAdapters();
    }

    initialized = true;
  }

  @Override
  public boolean isInitialized() {
    return initialized;
  }

  @Override
  public void terminate() {
    if (!isInitialized()) {
      LOG.debug("Not initialized.");
      return;
    }

    // Detach all attached drivers to clean up.
    detachAllAdapters();
    vehicleEntryPool.terminate();
    commAdapterRegistry.terminate();

    initialized = false;
  }

  /**
   * Attaches an adapter to a vehicle.
   *
   * @param vehicleName The vehicle name.
   * @param factory The factory that provides the adapter to be assigned.
   */
  public void attachAdapterToVehicle(@Nonnull String vehicleName,
                                     @Nonnull VehicleCommAdapterFactory factory) {
    requireNonNull(vehicleName, "vehicleName");
    requireNonNull(factory, "factory");

    VehicleEntry vehicleEntry = vehicleEntryPool.getEntryFor(vehicleName);
    if (vehicleEntry == null) {
      LOG.warn("No vehicle entry found for '{}'. Entries: {}",
               vehicleName,
               vehicleEntryPool);
      return;
    }

    VehicleCommAdapter commAdapter = factory.getAdapterFor(vehicleEntry.getVehicle());
    if (commAdapter == null) {
      LOG.warn("Factory {} did not provide adapter for vehicle {}, ignoring.",
               factory,
               vehicleEntry.getVehicle().getName());
      return;
    }
    
    // Perform a cleanup for the old adapter.
    disableAndTerminateAdapter(vehicleEntry);
    controllerPool.detachVehicleController(vehicleEntry.getVehicle().getName());

    commAdapter.initialize();
    controllerPool.attachVehicleController(vehicleEntry.getVehicle().getName(), commAdapter);

    vehicleEntry.setCommAdapterFactory(factory);
    vehicleEntry.setCommAdapter(commAdapter);
    vehicleEntry.setProcessModel(commAdapter.getProcessModel());

    objectService.updateObjectProperty(vehicleEntry.getVehicle().getReference(),
                                       Vehicle.PREFERRED_ADAPTER,
                                       factory.getClass().getName());

    updateAttachmentInformation(vehicleEntry);
  }

  public void autoAttachAdapterToVehicle(@Nonnull String vehicleName) {
    requireNonNull(vehicleName, "vehicleName");

    VehicleEntry vehicleEntry = vehicleEntryPool.getEntryFor(vehicleName);
    if (vehicleEntry == null) {
      LOG.warn("No vehicle entry found for '{}'. Entries: {}",
               vehicleName,
               vehicleEntryPool);
      return;
    }

    // Do not auto-attach if there is already a comm adapter attached to the vehicle.
    if (vehicleEntry.getCommAdapter() != null) {
      return;
    }

    Vehicle vehicle = getUpdatedVehicle(vehicleEntry.getVehicle());
    String prefAdapter = vehicle.getProperties().get(Vehicle.PREFERRED_ADAPTER);
    VehicleCommAdapterFactory factory = findFactoryWithName(prefAdapter);
    if (factory != null) {
      attachAdapterToVehicle(vehicleName, factory);
    }
    else {
      if (!Strings.isNullOrEmpty(prefAdapter)) {
        LOG.warn("Couldn't attach preferred adapter {} to {}.  Attaching first available adapter.",
                 prefAdapter,
                 vehicleEntry.getVehicle().getName());
      }
      List<VehicleCommAdapterFactory> factories
          = commAdapterRegistry.findFactoriesFor(vehicleEntry.getVehicle());
      if (!factories.isEmpty()) {
        attachAdapterToVehicle(vehicleName, factories.get(0));
      }
    }
  }

  public void autoAttachAllAdapters() {
    vehicleEntryPool.getEntries().forEach((vehicleName, entry) -> {
      autoAttachAdapterToVehicle(vehicleName);
    });
  }

  public AttachmentInformation getAttachmentInformation(String vehicleName) {
    requireNonNull(vehicleName, "vehicleName");
    Assertions.checkArgument(attachmentPool.get(vehicleName) != null,
                             "No attachment information for vehicle %s",
                             vehicleName);

    return attachmentPool.get(vehicleName);
  }

  public Map<String, AttachmentInformation> getAttachmentPool() {
    return attachmentPool;
  }

  private void disableAndTerminateAdapter(@Nonnull VehicleEntry vehicleEntry) {
    requireNonNull(vehicleEntry, "vehicleEntry");

    VehicleCommAdapter commAdapter = vehicleEntry.getCommAdapter();
    if (commAdapter != null) {
      commAdapter.disable();
      // Let the adapter know cleanup time is here.
      commAdapter.terminate();
    }
  }

  private void initAttachmentPool() {
    vehicleEntryPool.getEntries().forEach((vehicleName, entry) -> {
      List<VehicleCommAdapterDescription> availableCommAdapters
          = commAdapterRegistry.getFactories().stream()
              .filter(f -> f.providesAdapterFor(entry.getVehicle()))
              .map(f -> f.getDescription())
              .collect(Collectors.toList());

      attachmentPool.put(vehicleName,
                         new AttachmentInformation(entry.getVehicle().getReference(),
                                                   availableCommAdapters,
                                                   new NullVehicleCommAdapterDescription()));
    });
  }

  private void updateAttachmentInformation(VehicleEntry entry) {
    String vehicleName = entry.getVehicleName();
    VehicleCommAdapterFactory factory = entry.getCommAdapterFactory();
    AttachmentInformation newAttachment = attachmentPool.get(vehicleName)
        .withAttachedCommAdapter(factory.getDescription());

    attachmentPool.put(vehicleName, newAttachment);

    eventHandler.onEvent(new AttachmentEvent(vehicleName, newAttachment));
    if (entry.getCommAdapter() == null) {
      // In case we are detached
      eventHandler.onEvent(new ProcessModelEvent(vehicleName, new VehicleProcessModelTO()));
    }
    else {
      eventHandler.onEvent(new ProcessModelEvent(vehicleName,
                                                 entry.getCommAdapter().createTransferableProcessModel()));
    }
  }

  /**
   * Returns a fresh copy of a vehicle from the kernel.
   *
   * @param vehicle The old vehicle instance.
   * @return The fresh vehicle instance.
   */
  private Vehicle getUpdatedVehicle(@Nonnull Vehicle vehicle) {
    requireNonNull(vehicle, "vehicle");

    return objectService.fetchObjects(Vehicle.class).stream()
        .filter(updatedVehicle -> Objects.equals(updatedVehicle.getName(), vehicle.getName()))
        .findFirst().orElse(vehicle);
  }

  private void autoEnableAllAdapters() {
    vehicleEntryPool.getEntries().values().stream()
        .map(entry -> entry.getCommAdapter())
        .filter(adapter -> adapter != null)
        .filter(adapter -> !adapter.isEnabled())
        .forEach(adapter -> adapter.enable());
  }

  private void detachAllAdapters() {
    LOG.debug("Detaching vehicle communication adapters...");
    vehicleEntryPool.getEntries().forEach((vehicleName, entry) -> {
      disableAndTerminateAdapter(entry);
    });
    LOG.debug("Detached vehicle communication adapters");
  }

  @Nullable
  private VehicleCommAdapterFactory findFactoryWithName(@Nullable String name) {
    return commAdapterRegistry.getFactories().stream()
        .filter(factory -> factory.getClass().getName().equals(name))
        .findFirst()
        .orElse(null);
  }
}
