package otp_mail.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import otp_mail.service.OtpService;

@RestController
@RequestMapping("/api/otp")
public class OtpController {

    private final OtpService otpService;

    public OtpController(OtpService otpService) {
        this.otpService = otpService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendOtp(@RequestParam String email) {
        otpService.sendOtp(email);
        return ResponseEntity.ok("OTP sent successfully to " + email);
    }
}