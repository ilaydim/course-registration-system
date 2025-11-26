package com.uni.registration.messaging;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_NAME = "registration.queue";
    public static final String EXCHANGE_NAME = "registration.topic";
    public static final String ROUTING_KEY = "course.register";

    // 1. Define the Queue
    @Bean
    public Queue registrationQueue() {
        return new Queue(QUEUE_NAME, true); // durable queue
    }

    // 2. Define the Exchange
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    // (Binding the queue to the exchange is also necessary, but omitted for brevity)
}