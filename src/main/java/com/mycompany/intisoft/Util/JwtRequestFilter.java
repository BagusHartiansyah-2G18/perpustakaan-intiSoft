/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.intisoft.Util;

/**
 *
 * @author USER
 */
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.mycompany.intisoft.Model.Interface.RImember;
import com.mycompany.intisoft.Model.Mmember;
import com.mycompany.intisoft.Model.Muser;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
@Component
public class JwtRequestFilter extends OncePerRequestFilter{
    private Ujwt jwtUtil;
    private UserDetailsService userDetailsService;
    private RImember rImember;
    
    public JwtRequestFilter(Ujwt jwtUtil,UserDetailsService userDetailsService,RImember rImember){
        this.jwtUtil = jwtUtil;
        this.userDetailsService=userDetailsService;
        this.rImember = rImember;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        
        final String authorizationHeader = request.getHeader("Authorization"); 
        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7); // Hapus 'Bearer ' dari token 
            username = jwtUtil.extractUsername(jwt); 
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            Optional<Muser> akun = rImember.findByEmail(username); 
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(akun.get().getUsername()); 
            if (jwtUtil.validateToken(jwt, akun.get().getEmail()) && userDetails.isEnabled()) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } 
        }
        chain.doFilter(request, response);
        
    }
    
}
