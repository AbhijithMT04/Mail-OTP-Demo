package otp_mail.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class OtpService {

    private final JavaMailSender javaMailSender;
    private final SecureRandom secureRandom = new SecureRandom();

    private final Map<String, OtpData> otpStorage = new ConcurrentHashMap<>();

    @Value("${spring.mail.username}")
    private String fromEmail;

    public OtpService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendOtp(String toEmail) {
        String otp = generateOtp();

        otpStorage.put(
                toEmail,
                new OtpData(otp, LocalDateTime.now().plusMinutes(5))
        );

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Your OTP Code");
        message.setText(
                "Hello,\n\n" +
                "Your OTP is: " + otp + "\n\n" +
                "This OTP is valid for 5 minutes.\n\n" +
                "Thank you."
        );

        javaMailSender.send(message);

        System.out.println("OTP sent to: " + toEmail);
        System.out.println("Generated OTP: " + otp);
    }

    private String generateOtp() {
        int otp = 100000 + secureRandom.nextInt(900000);
        return String.valueOf(otp);
    }

    private record OtpData(String otp, LocalDateTime expiryTime) {
    }
   }
