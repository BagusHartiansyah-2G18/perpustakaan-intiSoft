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
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Mailgun {
    private RIotp rIotp;  
     public Mailgun(RIotp rIotp){ 
        this.rIotp = rIotp;
    }
    @Value("${mailgun.api.key}")
    private String apiKey;

    @Value("${mailgun.domain}")
    private String domain;

    public void sendOtpEmail(String to, String otp) {
        
    }
    public boolean generateOTP(String email) {
        String otp = String.valueOf(new Random().nextInt(900000) + 100000); // 6 digit
        Motp otpEntity = new Motp();
        otpEntity.setEmail(email);
        otpEntity.setOtp(otp);
        otpEntity.setExpiryTime(LocalDateTime.now().plusMinutes(5));
        rIotp.save(otpEntity);
        
        String requestUrl = "https://api.mailgun.net/v3/" + domain + "/messages";

        HttpResponse<JsonNode> response = Unirest.post(requestUrl)
            .basicAuth("api", apiKey)
            .field("from", "YourApp <no-reply@" + domain + ">")
            .field("to", email)
            .field("subject", "Your OTP Code")
            .field("text", "Your OTP code is: " + otp)
            .asJson();

        if (response.getStatus() != 200) {
            System.out.println(response);
            return false;
//            throw new RuntimeException("Failed to send email: " + response.getBody());
        }
        return true;
    }
} 