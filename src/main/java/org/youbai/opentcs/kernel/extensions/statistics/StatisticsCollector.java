/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.kernel.extensions.statistics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.youbai.opentcs.components.kernel.KernelExtension;
import org.youbai.opentcs.kernel.annotations.SimpleEventBusAnnotation;
import org.youbai.opentcs.util.event.EventSource;

import java.io.File;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.util.Objects.requireNonNull;
import static org.youbai.opentcs.util.Assertions.checkArgument;

/**
 * Collects data from kernel events and logs interesting events to a file that
 * can later be processed for statistical purposes.
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
public class StatisticsCollector
    implements KernelExtension {

  /**
   * This class's logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(StatisticsCollector.class);
  /**
   * Where we register for application events.
   */
  private final EventSource eventSource;
  /**
   * The directory to log event data to.
   */
  private final File logDir;
  /**
   * Whether this instance is currently initialized.
   */
  private boolean initialized;
  /**
   * An event logger for persisting the event data collected.
   */
  private StatisticsEventLogger statisticsLogger;

  /**
   * Creates a new instance.
   *
   * @param eventSource Where this instance registers for application events.
   * @param homeDir The kernel's home directory.
   */

  public StatisticsCollector(@SimpleEventBusAnnotation EventSource eventSource,
                             File homeDir) {
    this.eventSource = requireNonNull(eventSource, "eventSource");
    requireNonNull(homeDir, "homeDir");
    this.logDir = new File(homeDir, "log/statistics");
  }

  @Override
  public boolean isInitialized() {
    return initialized;
  }

  @Override
  public void initialize() {
    if (isInitialized()) {
      return;
    }
    checkArgument(logDir.isDirectory() || logDir.mkdirs(),
                  "Directory %s does not exist and could not be created, either.",
                  logDir.getPath());
    Format format = new SimpleDateFormat("yyyyMMdd-HHmmss");
    File logFile = new File(logDir, "opentcs-statistics-" + format.format(new Date()) + ".txt");

    // Create event processor and register it for kernel events.
    LOG.info("Logging events to {}...", logFile.getAbsolutePath());
    statisticsLogger = new StatisticsEventLogger(logFile);
    statisticsLogger.initialize();
    eventSource.subscribe(statisticsLogger);

    // Remember we're plugged in.
    initialized = true;
  }

  @Override
  public void terminate() {
    if (!isInitialized()) {
      return;
    }
    // Unregister event listener, terminate event processing.
    eventSource.unsubscribe(statisticsLogger);
    statisticsLogger.terminate();

    statisticsLogger = null;
    initialized = false;
  }
}
