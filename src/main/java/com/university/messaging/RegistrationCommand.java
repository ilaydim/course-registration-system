package com.university.messaging;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = RegistrationCommand.RegisterPayload.class, name = "REGISTER"),
        @JsonSubTypes.Type(value = RegistrationCommand.DropPayload.class, name = "DROP")
})
public interface RegistrationCommand {

    @JsonProperty("type")
    CommandType type();

    final class RegisterPayload implements RegistrationCommand {
        private Long studentId;
        private String courseCode;

        public RegisterPayload() {
        }

        public RegisterPayload(Long studentId, String courseCode) {
            this.studentId = studentId;
            this.courseCode = courseCode;
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

        @Override
        public CommandType type() {
            return CommandType.REGISTER;
        }
    }

    final class DropPayload implements RegistrationCommand {
        private Long studentId;
        private String courseCode;

        public DropPayload() {
        }

        public DropPayload(Long studentId, String courseCode) {
            this.studentId = studentId;
            this.courseCode = courseCode;
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

        @Override
        public CommandType type() {
            return CommandType.DROP;
        }
    }
}

