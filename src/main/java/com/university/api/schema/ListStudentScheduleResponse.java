package com.university.api.schema;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ListStudentScheduleResponse", namespace = "http://university.com/registration")
public class ListStudentScheduleResponse {

    @XmlElement(name = "entries", namespace = "http://university.com/registration")
    private List<ScheduleEntry> entries;

    public List<ScheduleEntry> getEntries() {
        if (entries == null) {
            entries = new ArrayList<>();
        }
        return entries;
    }

    public void setEntries(List<ScheduleEntry> entries) {
        this.entries = entries;
    }
}





