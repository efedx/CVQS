package com.notification.consumers;

import com.notification.dtos.NotificationRequestDto;
import com.notification.interfaces.NotificationServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationServiceInterface notificationService;

    @RabbitListener(queues = "notification_terminal_queue")
    public void consumer(NotificationRequestDto notificationRequestDto) {
        notificationService.notify(notificationRequestDto);
    }

}
