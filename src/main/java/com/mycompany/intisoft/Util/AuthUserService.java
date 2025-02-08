/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.intisoft.Util;

/**
 *
 * @author USER
 */
import com.mycompany.intisoft.Model.Interface.RImember;
import com.mycompany.intisoft.Model.Muser;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
@Service
public class AuthUserService {
    private final RImember userRepository; // JPA Repository

    public AuthUserService(RImember userRepository) {
        this.userRepository = userRepository;
    }

    public Muser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Muser muser = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
            return muser;
        }
        throw new RuntimeException("User not authenticated");
    }
}
