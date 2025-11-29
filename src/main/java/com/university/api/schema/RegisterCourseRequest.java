package com.university.api.schema;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "RegisterCourseRequest", namespace = "http://university.com/registration")
public class RegisterCourseRequest {

    @XmlElement(namespace = "http://university.com/registration", required = true)
    private Long studentId;

    @XmlElement(namespace = "http://university.com/registration", required = true)
    private String courseCode;

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
}





