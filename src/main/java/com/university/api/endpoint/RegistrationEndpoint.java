package com.university.api.endpoint;

import com.university.api.schema.DropCourseRequest;
import com.university.api.schema.DropCourseResponse;
import com.university.api.schema.ListStudentScheduleRequest;
import com.university.api.schema.ListStudentScheduleResponse;
import com.university.api.schema.RegisterCourseRequest;
import com.university.api.schema.RegisterCourseResponse;
import com.university.api.schema.ScheduleEntry;
import com.university.api.schema.StatusPayload;
import com.university.messaging.RegistrationCommandProducer;
import com.university.service.RegistrationService;
import java.util.List;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class RegistrationEndpoint {

    private static final String NAMESPACE = "http://university.com/registration";

    private final RegistrationCommandProducer commandProducer;
    private final RegistrationService registrationService;

    public RegistrationEndpoint(RegistrationCommandProducer commandProducer,
                                RegistrationService registrationService) {
        this.commandProducer = commandProducer;
        this.registrationService = registrationService;
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "RegisterCourseRequest")
    @ResponsePayload
    public RegisterCourseResponse registerCourse(@RequestPayload RegisterCourseRequest request) {
        commandProducer.publishRegister(request.getStudentId(), request.getCourseCode());
        RegisterCourseResponse response = new RegisterCourseResponse();
        response.setResult(accepted("Kayıt isteği kuyruğa gönderildi."));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "DropCourseRequest")
    @ResponsePayload
    public DropCourseResponse dropCourse(@RequestPayload DropCourseRequest request) {
        commandProducer.publishDrop(request.getStudentId(), request.getCourseCode());
        DropCourseResponse response = new DropCourseResponse();
        response.setResult(accepted("Silme isteği kuyruğa gönderildi."));
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE, localPart = "ListStudentScheduleRequest")
    @ResponsePayload
    public ListStudentScheduleResponse listSchedule(@RequestPayload ListStudentScheduleRequest request) {
        List<ScheduleEntry> entries = registrationService.listSchedule(request.getStudentId());
        ListStudentScheduleResponse response = new ListStudentScheduleResponse();
        response.setEntries(entries);
        return response;
    }

    private StatusPayload accepted(String message) {
        StatusPayload payload = new StatusPayload();
        payload.setStatus("ACCEPTED");
        payload.setMessage(message);
        return payload;
    }
}

