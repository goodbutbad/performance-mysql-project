package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.models.Director;
import com.example.demo.repositories.DirectorRepository;

@Service
public class DirectorService {
    private final DirectorRepository directorRepository;

    public DirectorService(DirectorRepository directorRepository) {
        this.directorRepository = directorRepository;
    }

    public List<Director> getAllDirectors() {
        return directorRepository.findAll();
    }
    public Director findById(int directorId) {
        Optional<Director> director = directorRepository.findById(directorId);
        return director.orElseThrow(() -> new RuntimeException("Director not found"));
    }
}




