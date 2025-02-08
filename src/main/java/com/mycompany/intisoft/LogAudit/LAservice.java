/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.intisoft.LogAudit;

/**
 *
 * @author USER
 */
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.mycompany.intisoft.Model.Mlog;
import com.mycompany.intisoft.Model.Interface.RIlog;

@Component
@Order(1) // Pastikan filter dijalankan lebih awal
public class LAservice implements Filter {

    private static final Logger auditLogger = LoggerFactory.getLogger("AUDIT");
    private RIlog rIlog;
    
    public LAservice (RIlog rIlog){
        this.rIlog =rIlog;
    }
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Opsional: bisa diisi jika ada konfigurasi khusus
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String method = httpRequest.getMethod();
        String path = httpRequest.getRequestURI();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (auth != null) ? auth.getName() : "ANONYMOUS";

        Map<String, Object> logData = new HashMap<>();
        logData.put("user", username);
        logData.put("action", method);
        logData.put("details", path);
        logData.put("data", this.sumberRequest(httpRequest));

        auditLogger.info("{}", logData); // ✅ Perbaikan logging
        
        Mlog dt = new Mlog();
        dt.setAction(method);
        dt.setAuthor_id(username);
        dt.setSumber(this.sumberRequest(httpRequest));
        dt.setAction(method);
        rIlog.save(dt);
        // Lanjutkan filter chain
        chain.doFilter(request, response);
    }

    private String sumberRequest(HttpServletRequest request) {
        Map<String, String> info = new HashMap<>();

        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = request.getRemoteAddr();
        }
        info.put("Client IP", ipAddress);
        info.put("Method", request.getMethod());
        info.put("Request URI", request.getRequestURI());
        info.put("Query String", request.getQueryString());
        info.put("User-Agent", request.getHeader("User-Agent"));
        info.put("Referer", request.getHeader("Referer"));

        return info.toString();
    }

    @Override
    public void destroy() {
        // Opsional: bisa digunakan untuk membersihkan resource jika perlu
    }
}
