/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.intisoft.LogAudit;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.intisoft.Model.Interface.RIlog;
import com.mycompany.intisoft.Model.Mlog;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
/**
 *
 * @author USER
 */
@RestController
@RequestMapping("/la")
public class LAcontroller {
    
    private RIlog rIlog;
    public LAcontroller (RIlog rIlog){
        this.rIlog = rIlog;
    }
    
    @GetMapping()
    public ResponseEntity<?> get() {
        List<Mlog> dt = rIlog.findAll();
        return ResponseEntity.ok(dt);
    }
}
