package org.youbai.opentcs.kernel.extensions.servicewebapi.imp;

import org.youbai.opentcs.access.rmi.services.RemoteNotificationService;
import org.youbai.opentcs.data.notification.UserNotification;
import org.youbai.opentcs.kernel.extensions.servicewebapi.data.AppResult;
import org.youbai.opentcs.kernel.extensions.servicewebapi.data.AppResultBuilder;
import org.youbai.opentcs.kernel.extensions.servicewebapi.data.ResultCode;
import org.youbai.opentcs.kernel.extensions.servicewebapi.rest.RestNotificationService;
import org.youbai.opentcs.kernel.services.StandardPeripheralDispatcherService;

import javax.inject.Inject;
import java.util.List;
import java.util.function.Predicate;

public class NotificationService implements RestNotificationService {


    @Inject
    RemoteNotificationService remoteNotificationService;


    @Override
    public AppResult<List<UserNotification>> fetchUserNotifications(Predicate<UserNotification> predicate) {

        List<UserNotification> userNotifications = remoteNotificationService.fetchUserNotifications(predicate);
        return AppResultBuilder.success(userNotifications, ResultCode.SUCCESS);
    }

    @Override
    public AppResult<String> publishUserNotification(UserNotification notification) {
        remoteNotificationService.publishUserNotification(notification);
        return AppResultBuilder.success(ResultCode.SUCCESS);
    }
}
