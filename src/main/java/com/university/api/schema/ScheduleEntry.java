package com.university.api.schema;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "scheduleEntry", propOrder = {
        "courseCode",
        "courseTitle",
        "sectionId",
        "term",
        "status"
})
public class ScheduleEntry {

    @XmlElement(namespace = "http://university.com/registration", required = true)
    private String courseCode;

    @XmlElement(namespace = "http://university.com/registration", required = true)
    private String courseTitle;

    @XmlElement(namespace = "http://university.com/registration", required = true)
    private Long sectionId;

    @XmlElement(namespace = "http://university.com/registration", required = true)
    private String term;

    @XmlElement(namespace = "http://university.com/registration", required = true)
    private String status;

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public Long getSectionId() {
        return sectionId;
    }

    public void setSectionId(Long sectionId) {
        this.sectionId = sectionId;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}





