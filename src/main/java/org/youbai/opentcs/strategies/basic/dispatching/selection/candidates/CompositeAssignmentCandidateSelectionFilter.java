/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.strategies.basic.dispatching.selection.candidates;

import java.util.Collection;
import static java.util.Objects.requireNonNull;
import java.util.Set;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.youbai.opentcs.strategies.basic.dispatching.AssignmentCandidate;
import org.youbai.opentcs.strategies.basic.dispatching.selection.AssignmentCandidateSelectionFilter;

/**
 * A collection of {@link AssignmentCandidateSelectionFilter}s.
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
@Singleton
public class CompositeAssignmentCandidateSelectionFilter
    implements AssignmentCandidateSelectionFilter {

  /**
   * The {@link AssignmentCandidateSelectionFilter}s.
   */
  private final Instance<AssignmentCandidateSelectionFilter> filters;


  public CompositeAssignmentCandidateSelectionFilter(
          Instance<AssignmentCandidateSelectionFilter> filters) {
    this.filters = requireNonNull(filters, "filters");
  }

  @Override
  public Collection<String> apply(AssignmentCandidate candidate) {
    return filters.stream()
        .flatMap(filter -> filter.apply(candidate).stream())
        .collect(Collectors.toList());
  }
}
