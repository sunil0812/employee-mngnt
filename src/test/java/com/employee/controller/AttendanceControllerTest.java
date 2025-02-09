package com.employee.controller;

import com.employee.entity.request.AttendanceStatusRequest;
import com.employee.service.AttendanceDetailsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class AttendanceControllerTest {
    @InjectMocks
    private AttendanceController controller;

    @Mock
    private AttendanceDetailsService service;

    @Test
    void shall_test_attendance_details() {

        AttendanceStatusRequest request = AttendanceStatusRequest.builder().status("wfo").employeeId("we123").build();
        Mockito.when(service.insertAttendance(request)).thenReturn("Attendance saved successfully");
        ResponseEntity<String> response = controller.saveAttendance(request);
        Assertions.assertEquals("Attendance saved successfully", response.getBody());
    }
}
