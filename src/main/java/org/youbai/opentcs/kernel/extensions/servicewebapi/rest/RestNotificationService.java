package org.youbai.opentcs.kernel.extensions.servicewebapi.rest;

import org.youbai.opentcs.data.notification.UserNotification;
import org.youbai.opentcs.kernel.extensions.servicewebapi.data.AppResult;

import javax.ws.rs.Path;
import java.util.List;
import java.util.function.Predicate;


public interface RestNotificationService {
    @Path("UserNotifications")
    AppResult<List<UserNotification>> fetchUserNotifications(Predicate<UserNotification> predicate);
    @Path("publishUserNotification")
    AppResult<String> publishUserNotification(UserNotification notification);
}
