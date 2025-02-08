/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.intisoft.Auth;

/**
 *
 * @author USER
 */
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import com.mycompany.intisoft.Auth.AuthService;
import com.mycompany.intisoft.Model.Muser;
import java.util.List;
import com.mycompany.intisoft.Model.Interface.RImember;
import org.springframework.http.ResponseEntity;

import com.mycompany.intisoft.Auth.AuthDTO; 
import com.mycompany.intisoft.Util.Ujwt; 

import com.mycompany.intisoft.LogAudit.LAservice;
import com.mycompany.intisoft.Model.Mmember;
import com.mycompany.intisoft.Otp.Mailgun;
import com.mycompany.intisoft.Otp.Mailtrap;
import java.util.Optional;
import org.springframework.context.annotation.Lazy;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import com.mycompany.intisoft.Otp.OtpService;
import com.mycompany.intisoft.member.MemberService;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService; 
    private final MemberService memberService;
    private final AuthenticationManager authenticationManager;
    private final RImember rImember;
    private final Ujwt ujwt; 
    private final PasswordEncoder passwordEncoder;
    private OtpService otpService;

    private Mailgun mailgun;
    private Mailtrap mailtrap;

        
    public AuthController (@Lazy AuthService authService, RImember rImember,
            AuthenticationManager authenticationManager,Ujwt ujwt,
            PasswordEncoder passwordEncoder,
            OtpService otpService,
            Mailgun mailgun,
            Mailtrap mailtrap,
            MemberService memberService
    ){
        this.authService=authService; 
        this.rImember = rImember;
        this.authenticationManager=authenticationManager;
        this.ujwt = ujwt;
        this.passwordEncoder = passwordEncoder;
        this.otpService = otpService;
        this.mailgun = mailgun;
        this.mailtrap = mailtrap;
        this.memberService = memberService;
    }
    
//    @GetMapping("/hello")
//    public String sayHello(HttpServletRequest request) {
////        logA.logAction("public", "hello", "uji coba",request);
//        return "Hello, World!";
//    System.out.println("Input Password: " + password);
//    }
    
    @GetMapping("all")
    public List<Muser> getUsers() {
        return this.authService._allMember();
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Muser request) {
        Optional<Muser> existingUser = rImember.findByUsername(request.getUsername());
        if (existingUser.isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        request.setPassword(authService.encodePassword(request.getPassword()));
        
        Set<String> droles = new HashSet<>();
        droles.add("viewer");
        request.setRoles(droles);
        return ResponseEntity.ok(memberService.save(request));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthDTO request) {
        String username = request.getUsername();
        String email = request.getEmail();
        String password = authService.encodePassword(request.getPassword());  
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, request.getPassword())
            ); 
            UserDetails userDetails = authService.loadUserByUsername(username); 
        } catch (Exception e) {  
            Boolean adaUser = rImember.findByEmail(email).filter(user -> authService.matcPass(request.getPassword(), user.getPassword())).isEmpty();
            if(adaUser){
                return ResponseEntity.ok(Map.of("message", "User atau Email dengan Password tersebut tidak ditemukan !!!"));
            } 
        }  
        String resOtp = mailtrap.sendOtpEmail(email);
        if(resOtp.isEmpty()){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Failed to send email"));
        }  
//        return ResponseEntity.ok("Success to send email code OTP "); 
        return ResponseEntity.ok(Map.of("otp", resOtp)); 
    }
    
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestParam String email, @RequestParam String otp) { 
        if(!otpService.validateOTP(email, otp)){
//            ResponseEntity.ok("OTP Verified!")
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid OTP");
        } 
        String token = ujwt.generateToken(email); 
        return ResponseEntity.ok(Map.of("token", token));
    } 
}
