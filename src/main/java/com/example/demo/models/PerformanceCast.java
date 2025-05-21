package com.example.demo.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "performance_cast")
@Data
public class PerformanceCast {

    @EmbeddedId
    private PerformanceCastId id = new PerformanceCastId();

    @ManyToOne
    @MapsId("performanceId")
    @JoinColumn(name = "performance_id")
    private Performance performance;

    @ManyToOne
    @MapsId("actorId")
    @JoinColumn(name = "actor_id")
    private Actor actor;

    private String role;

    private boolean isMainCharacter = false;
}
