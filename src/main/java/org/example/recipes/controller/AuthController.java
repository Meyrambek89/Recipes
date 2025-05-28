package org.example.recipes.controller;

import jakarta.servlet.http.HttpSession;
import org.example.recipes.entity.User;
import org.example.recipes.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class AuthController {
    @Autowired
    private UserRepository userRepository;



    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping("/register")
    public String processRegistration(@ModelAttribute User user) {
        // Захэшировать пароль
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER"); // По умолчанию роль
        userRepository.save(user);
        return "redirect:/login";
    }



    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // Проверка пароля
            if (user.getPassword().equals(password)) {
                session.setAttribute("currentUser", user); // ✅ сохраняем в сессию

                // Перенаправление по роли
                if ("ADMIN".equals(user.getRole())) {
                    return "redirect:/admin/dashboard";
                } else {
                    return "redirect:/my-recipes"; // 👈 вместо /recipes
                }
            }
        }

        model.addAttribute("error", "Неверный email или пароль");
        return "login";
    }

}