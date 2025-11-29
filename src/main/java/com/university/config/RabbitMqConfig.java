package com.university.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;

@EnableRabbit
@Configuration
public class RabbitMqConfig {

    public static final String COMMANDS_EXCHANGE = "registration.commands";
    public static final String RESULTS_EXCHANGE = "registration.results";
    public static final String REGISTER_QUEUE = "registration.register.queue";
    public static final String DROP_QUEUE = "registration.drop.queue";
    public static final String RESULTS_QUEUE = "registration.result.queue";

    public static final String REGISTER_ROUTING_KEY = "register";
    public static final String DROP_ROUTING_KEY = "drop";
    public static final String RESULTS_ROUTING_KEY = "result";

    @Bean
    public DirectExchange registrationCommandsExchange() {
        return new DirectExchange(COMMANDS_EXCHANGE);
    }

    @Bean
    public DirectExchange registrationResultsExchange() {
        return new DirectExchange(RESULTS_EXCHANGE);
    }

    @Bean
    public Queue registerQueue() {
        return new Queue(REGISTER_QUEUE, true);
    }

    @Bean
    public Queue dropQueue() {
        return new Queue(DROP_QUEUE, true);
    }

    @Bean
    public Queue resultQueue() {
        return new Queue(RESULTS_QUEUE, true);
    }

    @Bean
    public Binding registerBinding(@Qualifier("registerQueue") Queue registerQueue,
                                   @Qualifier("registrationCommandsExchange") DirectExchange registrationCommandsExchange) {
        return BindingBuilder.bind(registerQueue).to(registrationCommandsExchange).with(REGISTER_ROUTING_KEY);
    }

    @Bean
    public Binding dropBinding(@Qualifier("dropQueue") Queue dropQueue,
                               @Qualifier("registrationCommandsExchange") DirectExchange registrationCommandsExchange) {
        return BindingBuilder.bind(dropQueue).to(registrationCommandsExchange).with(DROP_ROUTING_KEY);
    }

    @Bean
    public Binding resultsBinding(@Qualifier("resultQueue") Queue resultQueue,
                                  @Qualifier("registrationResultsExchange") DirectExchange registrationResultsExchange) {
        return BindingBuilder.bind(resultQueue).to(registrationResultsExchange).with(RESULTS_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter);
        return template;
    }
}

