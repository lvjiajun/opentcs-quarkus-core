package org.youbai.opentcs.kernel.extensions.servicewebapi.rest;

import org.youbai.opentcs.data.notification.UserNotification;
import org.youbai.opentcs.kernel.extensions.servicewebapi.data.AppResult;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.function.Predicate;

@Path("NotificationService")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface RestNotificationService {
    @Path("UserNotifications")
    @POST
    AppResult<List<UserNotification>> fetchUserNotifications(Predicate<UserNotification> predicate);
    @Path("publishUserNotification")
    @POST
    AppResult<String> publishUserNotification(UserNotification notification);
}
