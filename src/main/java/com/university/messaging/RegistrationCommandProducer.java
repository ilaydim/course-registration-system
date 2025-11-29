package com.university.messaging;

import com.university.config.RabbitMqConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RegistrationCommandProducer {

    private static final Logger log = LoggerFactory.getLogger(RegistrationCommandProducer.class);
    private final RabbitTemplate rabbitTemplate;

    public RegistrationCommandProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishRegister(Long studentId, String courseCode) {
        RegistrationCommand.RegisterPayload command = new RegistrationCommand.RegisterPayload(studentId, courseCode);
        rabbitTemplate.convertAndSend(RabbitMqConfig.COMMANDS_EXCHANGE, RabbitMqConfig.REGISTER_ROUTING_KEY, command);
        log.info("Published register command for student {} course {}", studentId, courseCode);
    }

    public void publishDrop(Long studentId, String courseCode) {
        RegistrationCommand.DropPayload command = new RegistrationCommand.DropPayload(studentId, courseCode);
        rabbitTemplate.convertAndSend(RabbitMqConfig.COMMANDS_EXCHANGE, RabbitMqConfig.DROP_ROUTING_KEY, command);
        log.info("Published drop command for student {} course {}", studentId, courseCode);
    }

    public void publishResult(RegistrationResultMessage result) {
        rabbitTemplate.convertAndSend(RabbitMqConfig.RESULTS_EXCHANGE, RabbitMqConfig.RESULTS_ROUTING_KEY, result);
        log.info("Published result {} for student {}", result.getAction(), result.getStudentId());
    }
}

