package com.university.messaging;

public class RegistrationResultMessage {

    private Long studentId;
    private String courseCode;
    private String action;
    private boolean success;
    private String message;

    public RegistrationResultMessage() {
    }

    public RegistrationResultMessage(Long studentId, String courseCode, String action, boolean success, String message) {
        this.studentId = studentId;
        this.courseCode = courseCode;
        this.action = action;
        this.success = success;
        this.message = message;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

