package com.employee.controller;

import com.employee.entity.request.AdminRegisterRequest;
import com.employee.model.AdminRegister;
import com.employee.model.ValidateDetails;
import com.employee.service.AdminRegisterService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/register")
public class AdminRegistrationController {


    @Autowired
    private AdminRegisterService adminService;

    @PostMapping("/admin")
    public ResponseEntity<String> registerAdmin(@Valid @RequestBody AdminRegisterRequest adminRegister) {
        return ResponseEntity.ok(adminService.register(adminRegister));
    }

    @PutMapping("/validate-details")
    public ResponseEntity<String> validateAdminDetails(@Valid @RequestBody ValidateDetails details) {
        return ResponseEntity.ok(adminService.validate(details));
    }

@PostMapping("/hello")
public ResponseEntity<String> sendHello(@Valid @RequestBody String name){
        return ResponseEntity.ok(name);
}

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody Map<String, String> request) throws JSONException {
        String phone = request.get("phone");
        if (phone == null || phone.isBlank()) {
            return ResponseEntity.badRequest().body("Phone number is required");
        }
        boolean isSent = adminService.sendOtpMsg91WhatsApp(phone);

        if (isSent) {
            return ResponseEntity.ok("OTP sent successfully via WhatsApp");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send OTP");
        }
    }




}
