package com.example.demo.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.models.Actor;
import com.example.demo.repositories.ActorRepository;

@Service
public class ActorService {
    private final ActorRepository actorRepository;

    public ActorService(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    public List<Actor> getAllActors() {
        return actorRepository.findAll();
    }
}
