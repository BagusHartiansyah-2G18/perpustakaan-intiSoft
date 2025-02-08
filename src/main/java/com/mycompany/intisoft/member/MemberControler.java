/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.intisoft.member;

/**
 *
 * @author USER
 */
import jakarta.servlet.http.HttpServletRequest;
import com.mycompany.intisoft.LogAudit.LAservice;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import com.mycompany.intisoft.member.MemberService;
import com.mycompany.intisoft.Model.Muser;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

 
@RestController
@RequestMapping("/member")
public class MemberControler {
//    private static final Logger logger = LoggerFactory.getLogger(MemberControler.class);
    private final PasswordEncoder passwordEncoder;
    private final MemberService memberService;
    private final LAservice logA;

    public MemberControler (MemberService memberService,PasswordEncoder passwordEncoder,LAservice logA){
        this.memberService=memberService;
        this.passwordEncoder= passwordEncoder;
        this.logA =logA;
    } 
    
    @PostMapping()
    public ResponseEntity<?> add(@RequestBody Muser muser,HttpServletRequest request) {
        Optional<Muser> existingUser = memberService.findByUsername(muser.getUsername());
        if (existingUser.isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        try {
            muser.setPassword(passwordEncoder.encode(muser.getPassword())); 
            return ResponseEntity.ok( memberService.save(muser));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("query did not return a unique");
        } 
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> del(@PathVariable Long id,HttpServletRequest request) {
        memberService.del(id);
        return ResponseEntity.ok("data deleted, successfully");
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Muser> updateUser(@PathVariable Long id, @RequestBody Muser user,HttpServletRequest request) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return ResponseEntity.ok(memberService.upd(id, user));
    }
    
    @GetMapping()
    public ResponseEntity<?> get() { 
        return ResponseEntity.ok(memberService.all());
    }
}
