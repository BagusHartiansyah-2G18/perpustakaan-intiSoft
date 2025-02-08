/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.intisoft.Config;

/**
 *
 * @author USER
 */
import com.mycompany.intisoft.Util.JwtRequestFilter;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class ConfigSecurity { 
    private final UserDetailsService userDetailsService; 
    private final JwtRequestFilter jwtRequestFilter;
    public ConfigSecurity(@Lazy UserDetailsService userDetailsService,@Lazy JwtRequestFilter jwtRequestFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
    }
        
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Menonaktifkan CSRF (opsional, hati-hati jika API publik)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/login", "/auth/register","/auth/verify-otp","/perpus/publik/**").permitAll()
//                    "/auth/all","/member","/perpus"
                .requestMatchers("/auth/all","/member/**","/la").hasRole("ADMIN")
                .requestMatchers("/perpus","/perpus/**").hasAnyRole("CONTRIBUTOR","ADMIN","EDITOR") 
//                .anyRequest().authenticated()
                
//                .requestMatchers("/perpus").hasAnyRole("ADMIN","EDITOR","CONTRIBUTOR") 
//                .requestMatchers("/perpus","/la").hasAuthority("VIEWER")  
//                .anyRequest().permitAll() // Mengizinkan semua request tanpa login
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class) // JWT Filter

            .formLogin(form -> form.disable()) // Menonaktifkan form login
            .httpBasic(basic -> basic.disable()); // Menonaktifkan basic authentication

        return http.build();
    } 
    
//     @Bean
//    public AuthenticationManager authenticationManager() {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(userDetailsService);
//        authProvider.setPasswordEncoder(passwordEncoder());
//        return new ProviderManager(List.of(authProvider));
//    }
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
//        return authConfig.getAuthenticationManager();
//    }
    
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder)
            throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder)
                .and()
                .build();
    }
    
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder()); // HARUS SAMA!
        return authProvider;
    }

}
