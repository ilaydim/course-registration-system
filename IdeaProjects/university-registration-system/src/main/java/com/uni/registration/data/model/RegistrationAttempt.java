package com.uni.registration.data.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;

@Entity
public class RegistrationAttempt {
    public enum Status { PENDING, SUCCESS, FAILED_FULL, FAILED_ERROR }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long studentId;
    private String courseId;

    @Enumerated(EnumType.STRING)
    private Status status;

    // Constructors, Getters, and Setters omitted for brevity...
}