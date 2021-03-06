/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.kernel.peripherals;

import java.util.List;
import static java.util.Objects.requireNonNull;
import javax.annotation.Nonnull;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.youbai.opentcs.components.Lifecycle;
import org.youbai.opentcs.components.kernel.services.InternalPeripheralService;
import org.youbai.opentcs.customizations.ApplicationEventBus;
import org.youbai.opentcs.data.model.Location;
import org.youbai.opentcs.data.model.TCSResourceReference;
import org.youbai.opentcs.drivers.peripherals.PeripheralCommAdapter;
import org.youbai.opentcs.drivers.peripherals.PeripheralCommAdapterDescription;
import org.youbai.opentcs.drivers.peripherals.PeripheralCommAdapterFactory;
import org.youbai.opentcs.drivers.peripherals.PeripheralProcessModel;
import org.youbai.opentcs.drivers.peripherals.management.PeripheralAttachmentEvent;
import org.youbai.opentcs.drivers.peripherals.management.PeripheralAttachmentInformation;
import org.youbai.opentcs.drivers.peripherals.management.PeripheralProcessModelEvent;
import org.youbai.opentcs.kernel.KernelApplicationConfiguration;
import org.youbai.opentcs.kernel.annotations.SimpleEventBusAnnotation;
import org.youbai.opentcs.kernel.annotations.StandardPeripheralServiceAnnotations;
import org.youbai.opentcs.util.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages attachment and detachment of peripheral communication adapters to location.
 *
 * @author Stefan Walter (Fraunhofer IML)
 * @author Martin Grzenia (Fraunhofer IML)
 */
@ApplicationScoped
public class PeripheralAttachmentManager
    implements Lifecycle {

  /**
   * This class's logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(PeripheralAttachmentManager.class);
  /**
   * This class's configuration.
   */
  private final KernelApplicationConfiguration configuration;
  /**
   * The peripheral service.
   */
  @Inject@StandardPeripheralServiceAnnotations
  InternalPeripheralService peripheralService;
  /**
   * The peripheral controller pool.
   */
  @Inject
  LocalPeripheralControllerPool controllerPool;
  /**
   * The peripheral comm adapter registry.
   */
  @Inject
  PeripheralCommAdapterRegistry commAdapterRegistry;
  /**
   * The pool of peripheral entries.
   */
  @Inject
  PeripheralEntryPool peripheralEntryPool;
  /**
   * The handler to send events to.
   */
  private final EventHandler eventHandler;
  /**
   * Whether the attachment manager is initialized or not.
   */
  private boolean initialized;

  /**
   * Creates a new instance.
   *
   * @param eventHandler The handler to send events to.
   * @param configuration This class's configuration.
   */

  public PeripheralAttachmentManager(@Nonnull @SimpleEventBusAnnotation EventHandler eventHandler,
                                     @Nonnull KernelApplicationConfiguration configuration) {
    this.eventHandler = requireNonNull(eventHandler, "eventHandler");
    this.configuration = requireNonNull(configuration, "configuration");
  }

  @Override
  public void initialize() {
    if (isInitialized()) {
      return;
    }

    commAdapterRegistry.initialize();
    peripheralEntryPool.initialize();

    autoAttachAllAdapters();
    LOG.debug("Locations attached: {}", peripheralEntryPool.getEntries());

    if (configuration.autoEnablePeripheralDriversOnStartup()) {
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

    // Disable and terminate all attached drivers to clean up.
    disableAndTerminateAllAdapters();
    peripheralEntryPool.terminate();
    commAdapterRegistry.terminate();

    initialized = false;
  }

  public void attachAdapterToLocation(@Nonnull TCSResourceReference<Location> location,
                                      @Nonnull PeripheralCommAdapterDescription description) {
    requireNonNull(location, "location");
    requireNonNull(description, "description");

    attachAdapterToLocation(peripheralEntryPool.getEntryFor(location),
                            commAdapterRegistry.findFactoryFor(description));
  }

  @Nonnull
  public PeripheralAttachmentInformation getAttachmentInformation(
      @Nonnull TCSResourceReference<Location> location) {
    requireNonNull(location, "location");

    PeripheralEntry entry = peripheralEntryPool.getEntryFor(location);
    return new PeripheralAttachmentInformation(entry.getLocation(),
                                               entry.getCommAdapterFactory().getDescription());
  }

  private void attachAdapterToLocation(PeripheralEntry entry,
                                       PeripheralCommAdapterFactory factory) {
    Location location = peripheralService.fetchObject(Location.class, entry.getLocation());
    PeripheralCommAdapter commAdapter = factory.getAdapterFor(location);
    if (commAdapter == null) {
      LOG.warn("Factory {} did not provide adapter for location {}, ignoring.",
               factory,
               entry.getLocation().getName());
      return;
    }

    // Perform a cleanup for the old adapter.
    disableAndTerminateAdapter(entry);
    controllerPool.detachPeripheralController(entry.getLocation());

    commAdapter.initialize();
    controllerPool.attachPeripheralController(entry.getLocation(), commAdapter);

    entry.setCommAdapterFactory(factory);
    entry.setCommAdapter(commAdapter);

    // Publish events about the new attached adapter.
    eventHandler.onEvent(new PeripheralAttachmentEvent(
        entry.getLocation(),
        new PeripheralAttachmentInformation(entry.getLocation(),
                                            entry.getCommAdapterFactory().getDescription()))
    );
    eventHandler.onEvent(new PeripheralProcessModelEvent(
        entry.getLocation(),
        PeripheralProcessModel.Attribute.LOCATION.name(),
        entry.getProcessModel()
    ));
  }

  private void autoAttachAdapterToLocation(PeripheralEntry peripheralEntry) {
    // Do not auto-attach if there is already a (real) comm adapter attached to the location.
    if (!(peripheralEntry.getCommAdapter() instanceof NullPeripheralCommAdapter)) {
      return;
    }

    Location location = peripheralService.fetchObject(Location.class, peripheralEntry.getLocation());
    List<PeripheralCommAdapterFactory> factories = commAdapterRegistry.findFactoriesFor(location);
    if (!factories.isEmpty()) {
      LOG.debug("Attaching {} to first available adapter: {}.",
                peripheralEntry.getLocation().getName(),
                factories.get(0).getDescription().getDescription());
      attachAdapterToLocation(peripheralEntry, factories.get(0));
    }
  }

  private void autoAttachAllAdapters() {
    peripheralEntryPool.getEntries().forEach((location, entry) -> {
      autoAttachAdapterToLocation(entry);
    });
  }

  private void disableAndTerminateAdapter(PeripheralEntry peripheralEntry) {
    peripheralEntry.getCommAdapter().disable();
    peripheralEntry.getCommAdapter().terminate();
  }

  private void autoEnableAllAdapters() {
    peripheralEntryPool.getEntries().values().stream()
        .map(entry -> entry.getCommAdapter())
        .filter(adapter -> !adapter.isEnabled())
        .forEach(adapter -> adapter.enable());
  }

  private void disableAndTerminateAllAdapters() {
    LOG.debug("Detaching peripheral communication adapters...");
    peripheralEntryPool.getEntries().forEach((location, entry) -> {
      disableAndTerminateAdapter(entry);
    });
    LOG.debug("Detached peripheral communication adapters");
  }
}
