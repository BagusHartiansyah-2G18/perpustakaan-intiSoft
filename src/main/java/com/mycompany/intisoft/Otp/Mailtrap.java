/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.intisoft.Otp;

/**
 *
 * @author USER
 */
import com.mycompany.intisoft.Model.Interface.RIotp;
import com.mycompany.intisoft.Model.Motp;
import java.time.LocalDateTime;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class Mailtrap {
    private RIotp rIotp;  
    private JavaMailSender mailSender;

    public Mailtrap(RIotp rIotp,JavaMailSender mailSender){ 
        this.rIotp = rIotp;
        this.mailSender=mailSender;
    }

    public String sendOtpEmail(String email) {
        String otp = String.valueOf(new Random().nextInt(900000) + 100000); // 6 digit
        Motp otpEntity = new Motp();
        otpEntity.setEmail(email);
        otpEntity.setOtp(otp);
        otpEntity.setExpiryTime(LocalDateTime.now().plusMinutes(5));
        rIotp.save(otpEntity);
        
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Your OTP Code");
            message.setText("Your OTP code is: " + otp);
            mailSender.send(message);
            return otp;
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }
}
