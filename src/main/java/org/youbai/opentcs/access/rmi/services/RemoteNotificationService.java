/**
 * Copyright (c) The openTCS Authors.
 *
 * This program is free software and subject to the MIT license. (For details,
 * see the licensing information (LICENSE.txt) you should have received with
 * this copy of the software.)
 */
package org.youbai.opentcs.access.rmi.services;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.function.Predicate;

import org.youbai.opentcs.components.kernel.services.NotificationService;
import org.youbai.opentcs.data.notification.UserNotification;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Declares the methods provided by the {@link NotificationService} via RMI.
 *
 * <p>
 * The majority of the methods declared here have signatures analogous to their counterparts in
 * {@link NotificationService}, with an additional {@link ClientID} parameter which serves the
 * purpose of identifying the calling client and determining its permissions.
 * </p>
 * <p>
 * To avoid redundancy, the semantics of methods that only pass through their arguments are not
 * explicitly documented here again. See the corresponding API documentation in
 * {@link NotificationService} for these, instead.
 * </p>
 *
 * @author Martin Grzenia (Fraunhofer IML)
 */

public interface RemoteNotificationService {

  List<UserNotification> fetchUserNotifications(Predicate<UserNotification> predicate);

  void publishUserNotification(UserNotification notification);
}
