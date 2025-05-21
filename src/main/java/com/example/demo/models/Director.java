

package com.example.demo.models;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
@Entity
@Table(name = "directors")
@Data
public class Director {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer directorId;
    private String firstName;
    private String lastName;
    @Column(columnDefinition = "TEXT")
    private String bio;
    private String contactInfo;
    private LocalDateTime createdAt = LocalDateTime.now();
    @OneToMany(mappedBy = "director")
    private List<Performance> performances;
}