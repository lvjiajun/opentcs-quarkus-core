/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.kernel.extensions.rmi;

import java.rmi.registry.Registry;
import java.util.List;
import static java.util.Objects.requireNonNull;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.function.Predicate;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.youbai.opentcs.access.rmi.factories.SocketFactoryProvider;
import org.youbai.opentcs.access.rmi.services.RemoteNotificationService;
import org.youbai.opentcs.components.kernel.services.NotificationService;
import org.youbai.opentcs.data.notification.UserNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.youbai.opentcs.kernel.annotations.ExecutorServiceAnnotations;
import org.youbai.opentcs.kernel.annotations.StandardNotificationServiceAnnotation;

/**
 * This class is the standard implementation of the {@link RemoteNotificationService} interface.
 * <p>
 * Upon creation, an instance of this class registers itself with the RMI registry by the name
 * </p>
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
@ApplicationScoped
public class StandardRemoteNotificationService
    extends KernelRemoteService
    implements RemoteNotificationService {

  /**
   * This class's logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(StandardRemoteNotificationService.class);
  /**
   * The notification service to invoke methods on.
   */
  @Inject
  @StandardNotificationServiceAnnotation
  NotificationService notificationService;
  /**
   * Executes tasks modifying kernel data.
   */
  @Inject
  @ExecutorServiceAnnotations
  ExecutorService kernelExecutor;

  public StandardRemoteNotificationService() {
  }


  @Override
  public List<UserNotification> fetchUserNotifications(Predicate<UserNotification> predicate) {


    return notificationService.fetchUserNotifications(predicate);
  }

  @Override
  public void publishUserNotification(UserNotification notification) {
    try {
      kernelExecutor.submit(() -> notificationService.publishUserNotification(notification)).get();
    }
    catch (InterruptedException | ExecutionException exc) {
      throw findSuitableExceptionFor(exc);
    }
  }
}
