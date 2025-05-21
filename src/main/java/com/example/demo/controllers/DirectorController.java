package com.example.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DirectorController {
    @GetMapping("/directors")
    public String directors() {
        return "directors";
    }
    
}
