package com.uni.registration.data.repository;

import com.uni.registration.data.model.RegistrationAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationAttemptRepository extends JpaRepository<RegistrationAttempt, Long> {
    // Spring Data JPA provides basic CRUD methods automatically
}