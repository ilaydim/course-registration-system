package com.university.messaging;

import com.university.config.RabbitMqConfig;
import com.university.service.RegistrationService;
import com.university.service.dto.RegistrationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RegistrationCommandListener {

    private static final Logger log = LoggerFactory.getLogger(RegistrationCommandListener.class);

    private final RegistrationService registrationService;
    private final RegistrationCommandProducer resultProducer;

    public RegistrationCommandListener(RegistrationService registrationService,
                                       RegistrationCommandProducer resultProducer) {
        this.registrationService = registrationService;
        this.resultProducer = resultProducer;
    }

    @RabbitListener(queues = RabbitMqConfig.REGISTER_QUEUE)
    public void handleRegister(RegistrationCommand.RegisterPayload payload) {
        log.info("Processing register command {}", payload);
        RegistrationResult result = registrationService.registerStudent(payload.getStudentId(), payload.getCourseCode());
        resultProducer.publishResult(new RegistrationResultMessage(
                payload.getStudentId(),
                payload.getCourseCode(),
                "REGISTER",
                result.success(),
                result.message()
        ));
    }

    @RabbitListener(queues = RabbitMqConfig.DROP_QUEUE)
    public void handleDrop(RegistrationCommand.DropPayload payload) {
        log.info("Processing drop command {}", payload);
        RegistrationResult result = registrationService.dropStudent(payload.getStudentId(), payload.getCourseCode());
        resultProducer.publishResult(new RegistrationResultMessage(
                payload.getStudentId(),
                payload.getCourseCode(),
                "DROP",
                result.success(),
                result.message()
        ));
    }
}

