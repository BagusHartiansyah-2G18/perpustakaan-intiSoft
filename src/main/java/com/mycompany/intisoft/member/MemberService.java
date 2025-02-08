/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.intisoft.member;

/**
 *
 * @author USER
 */
import com.mycompany.intisoft.Model.Muser;
import com.mycompany.intisoft.Model.Interface.RImember;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;   
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class MemberService {
    
    @Autowired
    private RImember rImember;
    
    public Muser save(Muser data){
        return rImember.save(data);
    }
    public Muser upd(Long id, Muser data){ 
        return rImember.findById(id)
            .map(user -> {
                user.setUsername(data.getUsername());
                user.setEmail(data.getEmail());
                user.setRoles(data.getRoles());
                user.setNama(data.getNama());
                user.setPassword(data.getPassword());
                return rImember.save(user);
            })
            .orElseThrow(() -> new RuntimeException("User not found"));
    }
    public void del(Long id){
        rImember.deleteById(id); 
    }
    public List<Muser> all(){
        return rImember.findAll();
    }
    public Optional<Muser> one(Long id){
        return rImember.findById(id);
    }
    public Optional<Muser> findByUsername(String username){
        return rImember.findByUsername(username);
    }
}
