package com.example.demo.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Entity
@Table(name = "performances")
@Data
public class Performance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long performanceId;
    
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    private String imagePath;
    private Integer duration;
    private String genre;
    private LocalDate premiereDate;
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @ManyToOne
    @JoinColumn(name = "director_id")
    private Director director;
    
    @OneToMany(mappedBy = "performance", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PerformanceCast> cast;
    
   

}