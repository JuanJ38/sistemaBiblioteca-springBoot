package com.biblioteca.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    // Redirigir raíz al dashboard
    @GetMapping("/")
    public String inicio() {
        return "redirect:/libros";
    }

    // Página de login (Spring Security la usa automáticamente)
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
