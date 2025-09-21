package com.employee.controller;

import com.employee.entity.request.AdminRegisterRequest;
import com.employee.model.ValidateDetails;
import com.employee.service.AdminRegisterService;
import jakarta.validation.Valid;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@CrossOrigin(origins = "https://sunil0812.github.io")
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

    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestBody Map<String, String> request) throws JSONException {
        String phone = request.get("phone");
        if (phone == null || phone.isBlank()) {
            return ResponseEntity.badRequest().body("Phone number is required");
        }
        return ResponseEntity.ok().body(adminService.sendOtpMsg91WhatsApp(phone));
    }

    @PutMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok().body(adminService.verifyOtp(request));
    }

}
