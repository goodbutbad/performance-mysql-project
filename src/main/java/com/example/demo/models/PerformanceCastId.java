package com.example.demo.models;

import jakarta.persistence.Embeddable;
import lombok.Data;
import java.io.Serializable;

@Data
@Embeddable
public class PerformanceCastId implements Serializable {

    private Long performanceId;
    private Long actorId;
}


