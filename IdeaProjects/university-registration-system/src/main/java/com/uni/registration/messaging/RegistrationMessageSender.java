package com.uni.registration.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class RegistrationMessageSender {

    private final RabbitTemplate rabbitTemplate;

    public RegistrationMessageSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    // The payload is typically a simple DTO or JSON string
    public void sendRegistrationRequest(Long registrationId, Long studentId, String courseId) {
        String payload = String.format("{\"registrationId\":%d, \"studentId\":%d, \"courseId\":\"%s\"}",
                registrationId, studentId, courseId);

        // Send the message to the exchange with the routing key
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY,
                payload
        );
        System.out.println("Message sent to RabbitMQ for Registration ID: " + registrationId);
    }
}