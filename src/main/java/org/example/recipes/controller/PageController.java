package org.example.recipes.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/register")
    public String registerPage() {
        return "register"; // это вернёт templates/register.html
    }
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // это вернёт templates/register.html
    }
    @GetMapping("/favorite")
    public String favoritePage() {
        return "favorites"; // это вернёт templates/register.html
    }

   
    @GetMapping("/")
    public String homePage() {
        return "index"; // если есть index.html
    }
}

