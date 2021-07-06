/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.kernel.peripherals;


import java.util.Objects;
import static java.util.Objects.requireNonNull;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import org.youbai.opentcs.components.kernel.services.InternalPeripheralService;
import org.youbai.opentcs.customizations.ApplicationEventBus;
import org.youbai.opentcs.data.model.Location;
import org.youbai.opentcs.data.model.PeripheralInformation;
import org.youbai.opentcs.data.model.TCSResourceReference;
import org.youbai.opentcs.data.peripherals.PeripheralJob;
import org.youbai.opentcs.drivers.peripherals.PeripheralAdapterCommand;
import org.youbai.opentcs.drivers.peripherals.PeripheralCommAdapter;
import org.youbai.opentcs.drivers.peripherals.PeripheralController;
import org.youbai.opentcs.drivers.peripherals.PeripheralJobCallback;
import org.youbai.opentcs.drivers.peripherals.PeripheralProcessModel;
import org.youbai.opentcs.drivers.peripherals.management.PeripheralProcessModelEvent;
import static org.youbai.opentcs.util.Assertions.checkState;
import org.youbai.opentcs.util.ExplainedBoolean;
import org.youbai.opentcs.util.event.EventBus;
import org.youbai.opentcs.util.event.EventHandler;

/**
 * Realizes a bidirectional connection between the kernel and a comm adapter controlling a
 * peripheral device.
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
public class DefaultPeripheralController
    implements PeripheralController,
               EventHandler {

  /**
   * The location representing the peripheral device controlled by this controller/the comm adapter.
   */
  private final TCSResourceReference<Location> location;
  /**
   * The comm adapter controling the peripheral device.
   */
  private final PeripheralCommAdapter commAdapter;
  /**
   * The peripheral service to use.
   */
  private final InternalPeripheralService peripheralService;
  /**
   * The event bus we should register with and send events to.
   */
  private final EventBus eventBus;
  /**
   * Indicates whether this controller is initialized.
   */
  private boolean initialized;


  public DefaultPeripheralController(@Nonnull TCSResourceReference<Location> location,
                                     @Nonnull PeripheralCommAdapter commAdapter,
                                     @Nonnull InternalPeripheralService peripheralService,
                                     @Nonnull @ApplicationEventBus EventBus eventBus) {
    this.location = requireNonNull(location, "location");
    this.commAdapter = requireNonNull(commAdapter, "commAdapter");
    this.peripheralService = requireNonNull(peripheralService, "peripheralService");
    this.eventBus = requireNonNull(eventBus, "eventBus");
  }

  @Override
  public void initialize() {
    if (isInitialized()) {
      return;
    }

    eventBus.subscribe(this);

    updatePeripheralState(commAdapter.getProcessModel().getState());

    initialized = true;
  }

  @Override
  public boolean isInitialized() {
    return initialized;
  }

  @Override
  public void terminate() {
    if (!isInitialized()) {
      return;
    }

    updatePeripheralState(PeripheralInformation.State.UNKNOWN);

    eventBus.unsubscribe(this);

    initialized = false;
  }

  @Override
  public void onEvent(Object event) {
    if (!(event instanceof PeripheralProcessModelEvent)) {
      return;
    }

    PeripheralProcessModelEvent processModelEvent = (PeripheralProcessModelEvent) event;
    if (Objects.equals(processModelEvent.getAttributeChanged(),
                       PeripheralProcessModel.Attribute.STATE.name())
        && Objects.equals(processModelEvent.getLocation(), location)) {
      updatePeripheralState(processModelEvent.getProcessModel().getState());
    }
  }

  @Override
  public void process(PeripheralJob job, PeripheralJobCallback callback)
      throws IllegalStateException {
    requireNonNull(job, "job");
    requireNonNull(callback, "callback");

    ExplainedBoolean canProcess = canProcess(job);
    checkState(canProcess.getValue(),
               "%s: Can't process job: %s",
               location.getName(),
               canProcess.getReason());

    commAdapter.process(job, callback);
  }

  @Override
  public ExplainedBoolean canProcess(PeripheralJob job) {
    requireNonNull(job, "job");
    return commAdapter.canProcess(job);
  }

  @Override
  public void sendCommAdapterCommand(PeripheralAdapterCommand command) {
    requireNonNull(command, "command");
    commAdapter.execute(command);
  }

  private void updatePeripheralState(PeripheralInformation.State newState) {
    requireNonNull(newState, "newState");
    peripheralService.updatePeripheralState(location, newState);
  }
}
