package com.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationService notificationService;

    @RabbitListener(queues = "notification_terminal_queue")
    public void consumer(NotificationRequestDto notificationRequestDto) {
        notificationService.notify(notificationRequestDto);
    }

}
