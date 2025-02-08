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
@Table(name = "data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Mdata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idData;

    @Column(unique = true, nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String content;
    
    @Column(nullable = false)
    private String author_id;
    
    @Column(nullable = false)
    private Boolean  publik;
    
    public Boolean getPublik() {
        return this.publik;
    }

    public void setPublik(Boolean publik) {
        this.publik = publik;
    }
    
    
    @CreationTimestamp // Otomatis mengisi saat pertama kali dibuat
    @Column(updatable = false) // Tidak bisa diupdate
    private LocalDateTime createdAt;

    @UpdateTimestamp // Otomatis update saat ada perubahan
    private LocalDateTime updatedAt;
    
    public Long getIdData() {
        return idData;
    }

    public void setIdData(Long idData) {
        this.idData = idData;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
