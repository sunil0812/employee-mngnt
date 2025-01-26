package com.employee.controller;

import com.employee.entity.request.AttendanceStatusRequest;
import com.employee.model.AttendanceDetails;
import com.employee.service.AttendanceDetailsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceDetailsService service;
    @PutMapping("/save")
    public ResponseEntity<String> saveAttendance(@Valid @RequestBody AttendanceStatusRequest details){
            String response = service.insertAttendance(details);
        return ResponseEntity.ok(response);
    }
}
