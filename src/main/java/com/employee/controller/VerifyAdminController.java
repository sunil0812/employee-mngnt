package com.employee.controller;

import com.employee.service.VerifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/verify")
public class VerifyAdminController {

  @Autowired
  private VerifyService verify;

  @GetMapping("/phone")
  public ResponseEntity<String> generateOtpPhone(){

    return ResponseEntity.ok(verify.getPhoneOtp());
  }

}
