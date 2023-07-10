package com.terminal.amqp;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@RequiredArgsConstructor
public class RabbitMQMessagePublisher {

//    private String routingKey = "notification_terminal_routing_key";
//    private String exchange = "notification_terminal_exchange";

    @Value("${notification.defect.routingKey}")
    private String routingKey;
    @Value("${notification.defect.exchange}")
    private String exchange;


    private final Logger logger = LogManager.getLogger(RabbitMQMessagePublisher.class);

    private final AmqpTemplate amqpTemplate;

    public void publishNotificationTerminal(Object payload) {

        logger.info("Publishing to {} using routingKey {}. Payload: {}", exchange, routingKey, payload);
        amqpTemplate.convertAndSend("notification_terminal_exchange", "notification_terminal_routing_key", payload);
        logger.info("Published to {} using routingKey {}. Payload: {}", exchange, routingKey, payload);
    }

}
