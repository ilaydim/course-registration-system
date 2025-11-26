package com.uni.registration.api.endpoint;

import com.uni.registration.data.model.RegistrationAttempt;
import com.uni.registration.data.repository.RegistrationAttemptRepository;
import com.uni.registration.messaging.RegistrationMessageSender;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
// Assume classes below are generated from XSD using JAXB
import com.uni.example.registration.RegisterForCourseRequest;
import com.uni.example.registration.RegisterForCourseResponse;

@Endpoint
public class RegistrationServiceEndpoint {

    private static final String NAMESPACE_URI = "http://uni.example.com/registration";

    private final RegistrationAttemptRepository attemptRepository;
    private final RegistrationMessageSender messageSender;

    public RegistrationServiceEndpoint(
            RegistrationAttemptRepository attemptRepository,
            RegistrationMessageSender messageSender) {
        this.attemptRepository = attemptRepository;
        this.messageSender = messageSender;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "RegisterForCourseRequest")
    @ResponsePayload
    public RegisterForCourseResponse registerForCourse(@RequestPayload RegisterForCourseRequest request) {

        // 1. Create PENDING attempt in DB (Hibernate ORM)
        RegistrationAttempt attempt = new RegistrationAttempt();
        attempt.setStudentId(request.getStudentId());
        attempt.setCourseId(request.getCourseId());
        attempt.setStatus(RegistrationAttempt.Status.PENDING);
        attempt = attemptRepository.save(attempt);

        // 2. Send message to RabbitMQ (Messaging Layer)
        messageSender.sendRegistrationRequest(
                attempt.getId(),
                attempt.getStudentId(),
                attempt.getCourseId()
        );

        // 3. Prepare immediate SOAP response
        RegisterForCourseResponse response = new RegisterForCourseResponse();
        response.setRegistrationId(attempt.getId());
        response.setStatus(attempt.getStatus().name());

        // Response is sent immediately with PENDING status.
        return response;
    }

    // GetRegistrationStatus method would be implemented here as well
}