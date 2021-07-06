/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.strategies.basic.dispatching.priorization;

import com.google.common.collect.Lists;
import java.util.Comparator;
import java.util.Map;
import javax.inject.Inject;
import org.youbai.opentcs.data.order.TransportOrder;
import org.youbai.opentcs.strategies.basic.dispatching.DefaultDispatcherConfiguration;
import org.youbai.opentcs.strategies.basic.dispatching.priorization.transportorder.TransportOrderComparatorByAge;
import org.youbai.opentcs.strategies.basic.dispatching.priorization.transportorder.TransportOrderComparatorByName;
import org.youbai.opentcs.util.Assertions;

import static org.youbai.opentcs.util.Assertions.checkArgument;

/**
 * A composite of all configured transport order comparators.
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
public class CompositeOrderComparator
    implements Comparator<TransportOrder> {

  /**
   * A comparator composed of all configured comparators, in the configured order.
   */
  private final Comparator<TransportOrder> compositeComparator;


  public CompositeOrderComparator(DefaultDispatcherConfiguration configuration,
                                  Map<String, Comparator<TransportOrder>> availableComparators) {
    // At the end, if all other comparators failed to see a difference, compare by age.
    // As the age of two distinct transport orders may still be the same, finally compare by name.
    // Add configured comparators before these two.
    Comparator<TransportOrder> composite
        = new TransportOrderComparatorByAge().thenComparing(new TransportOrderComparatorByName());

    for (String priorityKey : Lists.reverse(configuration.orderPriorities())) {
      Comparator<TransportOrder> configuredComparator = availableComparators.get(priorityKey);
      Assertions.checkArgument(configuredComparator != null, "Unknown order priority key: %s", priorityKey);
      composite = configuredComparator.thenComparing(composite);
    }
    this.compositeComparator = composite;
  }

  @Override
  public int compare(TransportOrder o1, TransportOrder o2) {
    return compositeComparator.compare(o1, o2);
  }

}
