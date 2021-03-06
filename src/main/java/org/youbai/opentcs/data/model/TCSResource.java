/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.data.model;

import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.youbai.opentcs.components.kernel.Scheduler;
import org.youbai.opentcs.data.ObjectHistory;
import org.youbai.opentcs.data.TCSObject;

/**
 * Describes a resource that {@link Vehicle}s may claim for exclusive usage.
 *
 * @see Scheduler
 * @author Stefan Walter (Fraunhofer IML)
 * @param <E> The actual resource class.
 */
public abstract class TCSResource<E extends TCSResource<E>>
    extends TCSObject<E>
    implements Serializable {

  /**
   * Creates a new TCSResource.
   *
   * @param name The new resource's name.
   */
  protected TCSResource(String name) {
    super(name);
    reference = new TCSResourceReference<>(this);
  }

  /**
   * Creates a new TCSResource.
   *
   * @param name The new resource's name.
   * @param properties A set of properties (key-value pairs) associated with this object.
   * @param history A history of events related to this object.
   */
  @JsonCreator
  public TCSResource(@JsonProperty("name") String name,
                     @JsonProperty("properties")Map<String, String> properties,
                     @JsonProperty("history")ObjectHistory history) {
    super(name, properties, history);
    reference = new TCSResourceReference<>(this);
  }
  // Methods inherited from TCSObject<E> start here.
  @Override
  public TCSResourceReference<E> getReference() {
    return (TCSResourceReference<E>) reference;
  }
}
