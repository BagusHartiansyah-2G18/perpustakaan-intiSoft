/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.intisoft.Auth;

/**
 *
 * @author USER
 */
import com.mycompany.intisoft.Model.Muser;
import com.mycompany.intisoft.Model.Interface.RImember;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService{
    private final RImember rimember;
    private final PasswordEncoder passwordEncoder;
    
    public AuthService(RImember rimember, PasswordEncoder passwordEncoder) {
        this.rimember = rimember;
        this.passwordEncoder = passwordEncoder;
    }
    public List<Muser> _allMember(){
        return rimember.findAll();
    }
    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
    public boolean matcPass(String password,String passwordDB){  
        return passwordEncoder.matches(password, passwordDB);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { 
        try {
            Muser user = rimember.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User tidak ditemukan"));  
            
            Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.toString())) // role.getName() harus dalam format "ROLE_ADMIN"
                .collect(Collectors.toSet()); 
            return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(), // Password harus sudah dihash
                authorities // Berikan seluruh daftar roles 
            ); 
        } catch (Exception e) {
            return new User("defaultUser", "defaultPassword", new ArrayList<>());
        }
    }
}
