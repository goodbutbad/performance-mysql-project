package com.example.demo.controllers;

import com.example.demo.models.Role;
import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // Страница логина
    }

    @GetMapping("/register-form")
    public String registerPage() {
        return "register"; // Страница регистрации
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username, 
                              @RequestParam String email, 
                              @RequestParam String password, Model model) {
        
        // Проверка существования пользователя
        if (userRepository.existsByUsername(username)) {
            model.addAttribute("usernameError", "Пользователь с таким именем уже существует");
            return "register";
        }

        if (userRepository.existsByEmail(email)) {
            model.addAttribute("emailError", "Пользователь с таким email уже существует");
            return "register";
        }

        // Создание нового пользователя с хешированием пароля
        User user = new User(
                username,
                email,
                passwordEncoder.encode(password),
                Role.USER
        );

        userRepository.save(user);
        return "redirect:/home?registered=true";

    }
   
}