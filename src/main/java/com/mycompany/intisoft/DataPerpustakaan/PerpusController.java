/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.intisoft.DataPerpustakaan;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.mycompany.intisoft.Model.Mdata;
import com.mycompany.intisoft.Model.Interface.RIdata;
import com.mycompany.intisoft.Model.Muser;
import com.mycompany.intisoft.Util.AuthUserService;
import jakarta.servlet.http.HttpServletRequest;
/**
 *
 * @author USER
 */
@RestController
@RequestMapping("/perpus")
public class PerpusController {   
    private PerpusService perpusService;
    private AuthUserService xuser; 


    @Autowired // Tambahkan ini agar dependency diinject dengan benar
    public PerpusController(PerpusService perpusService,AuthUserService xuser) { 
        this.perpusService = perpusService;
        this.xuser = xuser;
    }
    @GetMapping
    public ResponseEntity<?> get() {
        Muser dt = xuser.getCurrentUser();
        if(dt.getRoles().toString().split("ADM").length>1 || dt.getRoles().toString().split("EDI").length>1){
            return ResponseEntity.ok(perpusService.all());
        }
        return ResponseEntity.ok(perpusService.listUser(dt.getId().toString()));
    }
    @PostMapping()
    public ResponseEntity<?> add(@RequestBody Mdata pdata) {
        pdata.setAuthor_id(xuser.getCurrentUser().getId().toString());
        try {
            return ResponseEntity.ok(perpusService.save(pdata));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("msg","duplicate key value violates unique"));
        }
        
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> del(@PathVariable Long id) {
        Muser dt = xuser.getCurrentUser();
        if(dt.getRoles().toString().split("CONTRI").length>1){ //CONTRI Contribusi
            return ResponseEntity.ok("sorry, can't access !");
        }
        perpusService.del(id);
        return ResponseEntity.ok("data deleted, successfully");
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody Mdata pdata) { 
        Muser dt = xuser.getCurrentUser();
        if(dt.getRoles().toString().split("WER").length>1){ //WER VIUWER
            return ResponseEntity.ok("sorry, can't access !");
        }
        return ResponseEntity.ok(perpusService.upd(id, pdata));
    }
    
    @PutMapping("/publik")
    public ResponseEntity<?> updPublik(@RequestParam Long id, @RequestParam Boolean publik) { 
        return ResponseEntity.ok(perpusService.updPublik(id, publik));
    }
    
    @GetMapping("/publik")
    public ResponseEntity<?> allPublik() { 
        Muser dt = xuser.getCurrentUser();
        if(dt.getRoles().toString().split("WER").length>1){ //WER VIUWER
            return ResponseEntity.ok("sorry, can't access !");
        }
        return ResponseEntity.ok(perpusService.allPublik());
    } 
}
