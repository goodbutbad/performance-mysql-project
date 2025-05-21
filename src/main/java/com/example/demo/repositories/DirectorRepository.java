package com.example.demo.repositories;

import com.example.demo.models.Director;



import org.springframework.data.jpa.repository.JpaRepository;


public interface DirectorRepository extends JpaRepository<Director, Integer> {

}
