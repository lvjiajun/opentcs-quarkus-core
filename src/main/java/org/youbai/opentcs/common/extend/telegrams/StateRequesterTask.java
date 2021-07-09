/**
 * Copyright (c) Fraunhofer IML
 */
package org.youbai.opentcs.common.extend.telegrams;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.swing.*;
import java.awt.event.ActionListener;

import static java.util.Objects.requireNonNull;

/**
 * A task for enqueuing state request telegrams periodically.
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
public class StateRequesterTask {

  /**
   * This class's logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(StateRequesterTask.class);
  /**
   * The actual action to be performed to enqueue requests.
   */
  private final ActionListener stateRequestAction;
  /**
   * A timer for enqueuing requests.
   */
  private Timer stateRequestTimer;
  /**
   * The interval requests should be enqueued.
   */
  private int requestInterval = 500;

  /**
   * Creates a new instance.
   *
   * @param stateRequestAction The actual action to be performed to enqueue requests.
   */
  @Inject
  public StateRequesterTask(@Nonnull ActionListener stateRequestAction) {
    this.stateRequestAction = requireNonNull(stateRequestAction, "stateRequestAction");
  }

  public void enable() {
    if (stateRequestTimer != null) {
      return;
    }
    LOG.debug("Starting state requester task.");
    stateRequestTimer = new Timer(requestInterval, stateRequestAction);
    stateRequestTimer.start();
  }

  public void disable() {
    if (stateRequestTimer == null) {
      return;
    }
    LOG.debug("Stopping state requester task.");
    stateRequestTimer.stop();
    stateRequestTimer = null;
  }

  /**
   * Restarts the timer for enqueuing new requests.
   */
  public void restart() {
    if (stateRequestTimer == null) {
      LOG.debug("Not enabled, doing nothing.");
      return;
    }
    stateRequestTimer.restart();
  }

  /**
   * Sets the interval the task should enqueue request.
   *
   * @param requestInterval The new interval.
   */
  public void setRequestInterval(int requestInterval) {
    this.requestInterval = requestInterval;
  }
}
