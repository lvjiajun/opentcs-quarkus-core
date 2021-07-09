/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.kernel;

import com.google.common.collect.Iterables;
import java.time.Instant;
import java.util.List;
import static java.util.Objects.requireNonNull;
import java.util.Set;
import java.util.function.Predicate;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.youbai.opentcs.components.kernel.OrderSequenceCleanupApproval;
import org.youbai.opentcs.components.kernel.TransportOrderCleanupApproval;
import org.youbai.opentcs.customizations.kernel.GlobalSyncObject;
import org.youbai.opentcs.data.TCSObjectReference;
import org.youbai.opentcs.data.order.OrderSequence;
import org.youbai.opentcs.data.order.TransportOrder;
import org.youbai.opentcs.kernel.workingset.TransportOrderPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A task that periodically removes orders in a final state.
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
@Singleton
class OrderCleanerTask
    implements Runnable {

  /**
   * This class's Logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(OrderCleanerTask.class);
  /**
   * A global object to be used for synchronization within the kernel.
   */
  private final Object globalSyncObject;
  /**
   * Keeps all the transport orders.
   */
  private final TransportOrderPool orderPool;
  /**
   * Check whether transport orders may be removed.
   */
  private final Instance<TransportOrderCleanupApproval> orderCleanupApprovals;
  /**
   * Check whether order sequences may be removed.
   */
  private final Instance<OrderSequenceCleanupApproval> sequenceCleanupApprovals;
  /**
   * This class's configuration.
   */
  private final OrderPoolConfiguration configuration;

  /**
   * Creates a new instance.
   *
   * @param configuration This class's configuration.
   */
  @Inject
  public OrderCleanerTask(Object globalSyncObject,
                          TransportOrderPool orderPool,
                          Instance<TransportOrderCleanupApproval> orderCleanupApprovals,
                          Instance<OrderSequenceCleanupApproval> sequenceCleanupApprovals,
                          OrderPoolConfiguration configuration) {
    this.globalSyncObject = requireNonNull(globalSyncObject, "globalSyncObject");
    this.orderPool = requireNonNull(orderPool, "orderPool");
    this.orderCleanupApprovals = requireNonNull(orderCleanupApprovals, "orderCleanupApprovals");
    this.sequenceCleanupApprovals = requireNonNull(sequenceCleanupApprovals,
                                                   "sequenceCleanupApprovals");
    this.configuration = requireNonNull(configuration, "configuration");
  }

  public long getSweepInterval() {
    return configuration.sweepInterval();
  }

  @Override
  public void run() {
    synchronized (globalSyncObject) {
      LOG.debug("Sweeping order pool...");
      // Candidates that are created before this point of time should be removed.
      Instant creationTimeThreshold = Instant.now().minusMillis(configuration.sweepAge());

      // Remove all transport orders in a final state that do NOT belong to a sequence and that are
      // older than the threshold.
      for (TransportOrder transportOrder
               : orderPool.getObjectPool().getObjects(TransportOrder.class,
                                                      new OrderApproval(creationTimeThreshold))) {
        orderPool.removeTransportOrder(transportOrder.getReference());
      }

      // Remove all order sequences that have been finished, including their transport orders.
      for (OrderSequence orderSequence
               : orderPool.getObjectPool().getObjects(
              OrderSequence.class,
              new SequenceApproval(creationTimeThreshold))) {
        orderPool.removeFinishedOrderSequenceAndOrders(orderSequence.getReference());
      }
    }
  }

  /**
   * Checks whether a transport order may be removed.
   */
  private class OrderApproval
      implements Predicate<TransportOrder> {

    private final Instant creationTimeThreshold;

    public OrderApproval(Instant creationTimeThreshold) {
      this.creationTimeThreshold = creationTimeThreshold;
    }

    @Override
    public boolean test(TransportOrder order) {
      if (!order.getState().isFinalState()) {
        return false;
      }
      if (order.getWrappingSequence() != null) {
        return false;
      }
      if (order.getCreationTime().isAfter(creationTimeThreshold)) {
        return false;
      }
      for (TransportOrderCleanupApproval approval : orderCleanupApprovals) {
        if (!approval.test(order)) {
          return false;
        }
      }
      return true;
    }
  }

  /**
   * Checks whether an order sequence may be removed.
   */
  private class SequenceApproval
      implements Predicate<OrderSequence> {

    private final Instant creationTimeThreshold;

    public SequenceApproval(Instant creationTimeThreshold) {
      this.creationTimeThreshold = creationTimeThreshold;
    }

    @Override
    public boolean test(OrderSequence seq) {
      if (!seq.isFinished()) {
        return false;
      }
      List<TCSObjectReference<TransportOrder>> orderRefs = seq.getOrders();
      if (!orderRefs.isEmpty()) {
        TransportOrder lastOrder
            = orderPool.getObjectPool().getObject(TransportOrder.class,
                                                  Iterables.getLast(orderRefs));
        if (lastOrder.getCreationTime().isAfter(creationTimeThreshold)) {
          return false;
        }
      }
      for (OrderSequenceCleanupApproval approval : sequenceCleanupApprovals) {
        if (!approval.test(seq)) {
          return false;
        }
      }
      return true;
    }
  }
}
