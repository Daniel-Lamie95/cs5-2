package com.example.cs5_2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        // This looks for "index.html" in the templates folder
        return "index";
    }

    @GetMapping("/login.html")
    public String legacyLoginRoute() {
        return "redirect:/login";
    }

    @GetMapping("/signup.html")
    public String legacySignupRoute() {
        return "redirect:/register";
    }

}