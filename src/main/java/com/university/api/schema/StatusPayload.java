package com.university.api.schema;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "statusPayload", propOrder = {"status", "message"})
public class StatusPayload {

    @XmlElement(namespace = "http://university.com/registration", required = true)
    private String status;

    @XmlElement(namespace = "http://university.com/registration", required = true)
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}





