/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.intisoft.Model;

/**
 *
 * @author USER
 */
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "member")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Mmember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    private String nama;
    
    @Column(unique = true,nullable = false)
    private String email;
    
    @Column(nullable = false)
    private int kdAkses;
     
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getKdAkses() {
        return kdAkses;
    }

    public void setKdAkses(int kdAkses) {
        this.kdAkses = kdAkses;
    }
}
