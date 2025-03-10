/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.intisoft.Model.Interface;

/**
 *
 * @author USER
 */
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.mycompany.intisoft.Model.Motp;
import org.springframework.stereotype.Repository;
 
@Repository
public interface RIotp extends JpaRepository<Motp,Long>{
        Optional<Motp> findByEmailAndOtp(String email, String otp);
}
