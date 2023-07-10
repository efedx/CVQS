package com.notification.interfaces;

import com.notification.dtos.NotificationRequestDto;

public interface NotificationServiceInterface {
    void notify(NotificationRequestDto notificationRequestDto);
}
