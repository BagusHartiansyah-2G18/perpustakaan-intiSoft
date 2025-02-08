/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.intisoft.Otp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.mycompany.intisoft.Model.Interface.RIotp;
import com.mycompany.intisoft.Model.Motp;
 
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
/**
 *
 * @author USER
 */
@Service
public class OtpService { 
    private RIotp rIotp;  
    private EmailService emailService;
    
    public OtpService(RIotp rIotp,EmailService emailService){
        this.emailService = emailService;
        this.rIotp = rIotp;
    }

    public String generateOTP(String email) {
        String otp = String.valueOf(new Random().nextInt(900000) + 100000); // 6 digit
        Motp otpEntity = new Motp();
        otpEntity.setEmail(email);
        otpEntity.setOtp(otp);
        otpEntity.setExpiryTime(LocalDateTime.now().plusMinutes(5));
        rIotp.save(otpEntity);
        emailService.sendOtpEmail(email, otp);
        return "OTP sent successfully";
    }

    public boolean validateOTP(String email, String otp) {
        Optional<Motp> otpOptional = rIotp.findByEmailAndOtp(email, otp); 
        if (otpOptional.isPresent() && otpOptional.get().getExpiryTime().isAfter(LocalDateTime.now())) {
            rIotp.delete(otpOptional.get()); // Hapus OTP setelah validasi sukses
            return true;
        }
        return false;
    }
}
