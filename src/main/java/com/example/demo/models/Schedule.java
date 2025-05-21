package com.example.demo.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "schedule")
@Data
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer scheduleId;

    @ManyToOne
    @JoinColumn(name = "performance_id")
    private Performance performance;

    private LocalDate showDate;
    private LocalTime showTime;
    private String hall;
    private Integer ticketsAvailable;
}
