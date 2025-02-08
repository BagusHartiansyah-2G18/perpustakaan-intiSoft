/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.intisoft.DataPerpustakaan;

import org.springframework.stereotype.Service;

/**
 *
 * @author USER
 */
import com.mycompany.intisoft.Model.Mdata;
import com.mycompany.intisoft.Model.Interface.RIdata;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;   
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class PerpusService {
    @Autowired
    private RIdata rIdata;
    
    public Mdata save(Mdata data){
        return rIdata.save(data);
    }
    public Mdata upd(Long id, Mdata data){ 
        return rIdata.findById(id)
            .map(x -> {
                x.setTitle(data.getTitle());
                x.setContent(data.getContent());
                return rIdata.save(x);
            })
            .orElseThrow(() -> new RuntimeException("User not found"));
    }
    public void del(Long id){
        rIdata.deleteById(id); 
    }
    public List<Mdata> all(){
        return rIdata.findAll();
    }
    public Optional<Mdata> one(Long id){
        return rIdata.findById(id);
    }
    
    public Mdata updPublik(Long id, Boolean publik){ 
        return rIdata.findById(id)
            .map(x -> {
                x.setPublik(publik);
                x.setIdData(id);
                return rIdata.save(x);
            })
            .orElseThrow(() -> new RuntimeException("data not found"));
    }
    public List<Mdata> allPublik(){
        return rIdata.findAll().stream().filter(d-> d.getPublik()).collect(Collectors.toList());
    }
    public List<Mdata> listUser(String author_id){
        return rIdata.findAll().stream().filter(d-> d.getAuthor_id()==author_id).collect(Collectors.toList());
    }
}
