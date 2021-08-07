/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.kernel.services;

import java.util.List;
import static java.util.Objects.requireNonNull;
import java.util.function.Predicate;
import javax.inject.Inject;
import javax.inject.Singleton;

import io.quarkus.runtime.Startup;
import org.youbai.opentcs.components.kernel.services.NotificationService;
import org.youbai.opentcs.data.notification.UserNotification;
import org.youbai.opentcs.kernel.GlobalSyncObject;
import org.youbai.opentcs.kernel.annotations.StandardNotificationServiceAnnotation;
import org.youbai.opentcs.kernel.workingset.NotificationBuffer;

/**
 * This class is the standard implementation of the {@link NotificationService} interface.
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */
@Singleton
@StandardNotificationServiceAnnotation
public class StandardNotificationService
    implements NotificationService {
  /**
   * The buffer for all messages published.
   */
  private final NotificationBuffer notificationBuffer;

  /**
   * Creates a new instance.
   *
   * @param notificationBuffer The notification buffer to be used.
   */

  public StandardNotificationService(NotificationBuffer notificationBuffer) {
    this.notificationBuffer = requireNonNull(notificationBuffer, "notificationBuffer");
  }

  @Override
  public List<UserNotification> fetchUserNotifications(Predicate<UserNotification> predicate) {
    return notificationBuffer.getNotifications(predicate);
  }

  @Override
  public void publishUserNotification(UserNotification notification) {

    notificationBuffer.addNotification(notification);
  }
}
