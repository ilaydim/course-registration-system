package com.university.api.schema;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "DropCourseResponse", namespace = "http://university.com/registration")
public class DropCourseResponse {

    @XmlElement(namespace = "http://university.com/registration", required = true)
    private StatusPayload result;

    public StatusPayload getResult() {
        return result;
    }

    public void setResult(StatusPayload result) {
        this.result = result;
    }
}





