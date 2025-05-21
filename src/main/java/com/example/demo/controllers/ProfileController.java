package com.example.demo.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.models.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import com.example.demo.repositories.UserRepository;

import org.springframework.stereotype.Controller;

@Controller
public class ProfileController {
    @Autowired
    private UserRepository userRepository;

  
@GetMapping("/profile/{id}")
public String showProfile(@PathVariable Long id, Model model) {
    User user = userRepository.findById(id).orElse(null);

    if (user == null) {
        return "redirect:/error"; // или можно отобразить свою кастомную страницу
    }

    model.addAttribute("user", user); // передаём объект в шаблон
    return "profile";
}



}
