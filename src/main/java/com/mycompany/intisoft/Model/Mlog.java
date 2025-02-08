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

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Mlog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLog;

    @Column(nullable = false)
    private String action;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String sumber;
    
    @Column(nullable = false)
    private String author_id;
    
    @CreationTimestamp // Otomatis mengisi saat pertama kali dibuat
    @Column(updatable = false) // Tidak bisa diupdate
    private LocalDateTime createdAt;
    
    // Getter & Setter
    public Long getIdLog() {
        return idLog;
    }

    public void setIdLog(Long idLog) {
        this.idLog = idLog;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getSumber() {
        return sumber;
    }

    public void setSumber(String sumber) {
        this.sumber = sumber;
    }

    public String getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
