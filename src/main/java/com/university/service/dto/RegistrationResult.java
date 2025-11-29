package com.university.service.dto;

public class RegistrationResult {

    private boolean success;
    private String message;

    private RegistrationResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean success() {
        return success;
    }

    public String message() {
        return message;
    }

    public static RegistrationResult success(String message) {
        return new RegistrationResult(true, message);
    }

    public static RegistrationResult failure(String message) {
        return new RegistrationResult(false, message);
    }
}

