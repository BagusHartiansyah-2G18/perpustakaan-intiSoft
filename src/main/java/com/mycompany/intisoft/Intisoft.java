/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.intisoft;

/**
 *
 * @author USER
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.web.server.WebServerFactoryCustomizer;
//import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
//import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class Intisoft {

    public static void main(String[] args) {
        SpringApplication.run(Intisoft.class, args);
    }
    
//    @Bean
//    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> customizer() {
//        return factory -> factory.setPort(9090);
//    }
}
