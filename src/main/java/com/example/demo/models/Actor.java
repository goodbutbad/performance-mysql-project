package com.example.demo.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
@Entity
@Table(name = "actors")
@Data
public class Actor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer actorId;
    
    private String firstName;
    private String lastName;
    
    @Column(columnDefinition = "TEXT")
    private String bio;
    
    private LocalDate birthDate;
    private String contactInfo;
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @OneToMany(mappedBy = "actor", cascade = CascadeType.ALL, orphanRemoval = true)
private List<PerformanceCast> performanceCasts;

}