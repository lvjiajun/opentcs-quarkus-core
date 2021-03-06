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
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;

import io.quarkus.runtime.Startup;
import org.youbai.opentcs.strategies.basic.dispatching.AssignmentCandidate;
import org.youbai.opentcs.strategies.basic.dispatching.DefaultDispatcherConfiguration;
import org.youbai.opentcs.strategies.basic.dispatching.priorization.candidate.CandidateComparatorByOrderAge;
import org.youbai.opentcs.strategies.basic.dispatching.priorization.candidate.CandidateComparatorByOrderName;
import org.youbai.opentcs.util.Assertions;

import static org.youbai.opentcs.util.Assertions.checkArgument;

/**
 * A composite of all configured transport order candidate comparators.
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
@Singleton
public class CompositeOrderCandidateComparator
    implements Comparator<AssignmentCandidate> {

  /**
   * A comparator composed of all configured comparators, in the configured order.
   */
  private final Comparator<AssignmentCandidate> compositeComparator;


  public CompositeOrderCandidateComparator(
      DefaultDispatcherConfiguration configuration,
      @Named("Candidate")Map<String, Comparator<AssignmentCandidate>> availableComparators) {
    // At the end, if all other comparators failed to see a difference, compare by age.
    // As the age of two distinct transport orders may still be the same, finally compare by name.
    // Add configured comparators before these two.
    Comparator<AssignmentCandidate> composite
        = new CandidateComparatorByOrderAge().thenComparing(new CandidateComparatorByOrderName());

    for (String priorityKey : Lists.reverse(configuration.orderCandidatePriorities())) {
      Comparator<AssignmentCandidate> configuredComparator = availableComparators.get(priorityKey);
      Assertions.checkArgument(configuredComparator != null,
                    "Unknown order candidate priority key: %s",
                    priorityKey);
      composite = configuredComparator.thenComparing(composite);
    }
    this.compositeComparator = composite;
  }

  @Override
  public int compare(AssignmentCandidate o1, AssignmentCandidate o2) {
    return compositeComparator.compare(o1, o2);
  }

}
