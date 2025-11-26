package com.uni.registration.worker;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uni.registration.data.model.RegistrationAttempt;
import com.uni.registration.data.repository.RegistrationAttemptRepository;
import com.uni.registration.data.service.EnrollmentService; // Service for capacity check/update
import com.uni.registration.messaging.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RegistrationMessageListener {

    private final RegistrationAttemptRepository attemptRepository;
    private final EnrollmentService enrollmentService; // Handles Course/Enrollment ORM updates
    private final ObjectMapper objectMapper = new ObjectMapper();

    public RegistrationMessageListener(
            RegistrationAttemptRepository attemptRepository,
            EnrollmentService enrollmentService) {
        this.attemptRepository = attemptRepository;
        this.enrollmentService = enrollmentService;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void handleRegistration(String message) {
        Long registrationId = null;
        try {
            JsonNode jsonNode = objectMapper.readTree(message);
            registrationId = jsonNode.get("registrationId").asLong();
            Long studentId = jsonNode.get("studentId").asLong();
            String courseId = jsonNode.get("courseId").asText();

            System.out.println("Processing Registration ID: " + registrationId + " for Course: " + courseId);

            // Core Business Logic (Data Layer/ORM interaction)
            boolean success = enrollmentService.tryEnroll(studentId, courseId);

            // Update status in the database
            RegistrationAttempt attempt = attemptRepository.findById(registrationId)
                    .orElseThrow(() -> new RuntimeException("Attempt not found"));

            if (success) {
                attempt.setStatus(RegistrationAttempt.Status.SUCCESS);
            } else {
                // Assuming failure is due to capacity here
                attempt.setStatus(RegistrationAttempt.Status.FAILED_FULL);
            }
            attemptRepository.save(attempt);

        } catch (Exception e) {
            System.err.println("Error processing message (ID: " + registrationId + "): " + e.getMessage());
            // In a real system, you would handle exceptions, maybe move to a Dead Letter Queue (DLQ)
        }
    }
}