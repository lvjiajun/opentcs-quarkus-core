/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.data.peripherals;

import java.io.Serializable;
import static java.util.Objects.requireNonNull;
import javax.annotation.Nonnull;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.youbai.opentcs.data.model.Location;
import org.youbai.opentcs.data.model.TCSResourceReference;

/**
 * Describes an operation that is to be executed by a peripheral device.
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
public class PeripheralOperation
    implements Serializable {

  /**
   * The location the peripheral device is associated with.
   */
  @Nonnull@JsonProperty("location")
  private TCSResourceReference<Location> location;
  /**
   * The actual operation to be executed by the peripheral device.
   */
  @Nonnull@JsonProperty("operation")
  private String operation;
  /**
   * The moment at which this operation is to be executed.
   */
  @Nonnull@JsonProperty("executionTrigger")
  private ExecutionTrigger executionTrigger;
  /**
   * Whether the completion of this operation is required to allow a vehicle to continue driving.
   */
  @JsonProperty("completionRequired")
  private boolean completionRequired;

  /**
   * Creates a new instance.
   *
   * @param location The location the peripheral device is associated with.
   * @param operation The actual operation to be executed by the peripheral device.
   * @param executionTrigger The moment at which this operation is to be executed.
   * @param completionRequired Whether the completion of this operation is required to allow a
   * vehicle to continue driving.
   */
  public PeripheralOperation(@Nonnull TCSResourceReference<Location> location,
                             @Nonnull String operation,
                             @Nonnull ExecutionTrigger executionTrigger,
                             boolean completionRequired) {
    this.location = requireNonNull(location, "location");
    this.operation = requireNonNull(operation, "operation");
    this.executionTrigger = requireNonNull(executionTrigger, "executionTrigger");
    this.completionRequired = completionRequired;
  }

  /**
   * Returns the location the peripheral device is associated with.
   *
   * @return The location the peripheral device is associated with.
   */
  @Nonnull
  public TCSResourceReference<Location> getLocation() {
    return location;
  }

  /**
   * Returns the actual operation to be executed by the peripheral device.
   *
   * @return The actual operation to be executed by the peripheral device.
   */
  @Nonnull
  public String getOperation() {
    return operation;
  }

  /**
   * Returns the moment at which this operation is to be executed.
   *
   * @return The moment at which this operation is to be executed.
   */
  @Nonnull
  public ExecutionTrigger getExecutionTrigger() {
    return executionTrigger;
  }

  /**
   * Returns whether the completion of this operation is required to allow a vehicle to continue
   * driving.
   *
   * @return Whether the completion of this operation is required to allow a vehicle to continue
   * driving.
   */
  public boolean isCompletionRequired() {
    return completionRequired;
  }

  public void setLocation(@Nonnull TCSResourceReference<Location> location) {
    this.location = location;
  }

  public void setOperation(@Nonnull String operation) {
    this.operation = operation;
  }

  public void setExecutionTrigger(@Nonnull ExecutionTrigger executionTrigger) {
    this.executionTrigger = executionTrigger;
  }

  public void setCompletionRequired(boolean completionRequired) {
    this.completionRequired = completionRequired;
  }

  /**
   * Defines the various moments at which an operation may be executed.
   */
  public static enum ExecutionTrigger {
    BEFORE_MOVEMENT,
    AFTER_MOVEMENT;
  }

  @Override
  public String toString() {
    return "PeripheralOperation{"
        + "location=" + location + ", "
        + "operation=" + operation + ", "
        + "executionTrigger=" + executionTrigger + ", "
        + "completionRequired=" + completionRequired + '}';
  }
}
