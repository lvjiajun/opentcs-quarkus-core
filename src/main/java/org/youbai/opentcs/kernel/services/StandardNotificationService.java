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
   * A global object to be used for synchronization within the kernel.
   */
  private final Object globalSyncObject;
  /**
   * The buffer for all messages published.
   */
  private final NotificationBuffer notificationBuffer;

  /**
   * Creates a new instance.
   *
   * @param globalSyncObject The kernel threads' global synchronization object.
   * @param notificationBuffer The notification buffer to be used.
   */
  @Inject
  public StandardNotificationService(GlobalSyncObject globalSyncObject,
                                     NotificationBuffer notificationBuffer) {
    this.globalSyncObject = requireNonNull(globalSyncObject, "globalSyncObject");
    this.notificationBuffer = requireNonNull(notificationBuffer, "notificationBuffer");
  }

  @Override
  public List<UserNotification> fetchUserNotifications(Predicate<UserNotification> predicate) {
    synchronized (globalSyncObject) {
      return notificationBuffer.getNotifications(predicate);
    }
  }

  @Override
  public void publishUserNotification(UserNotification notification) {
    synchronized (globalSyncObject) {
      notificationBuffer.addNotification(notification);
    }
  }
}
