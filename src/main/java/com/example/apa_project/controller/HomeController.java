package com.example.apa_project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("message", "Hello World!");
        return "index";
    }

    @GetMapping("/hello-servlet")
    public String hello(Model model) {
        model.addAttribute("message", "Hello World!");
        return "index";
    }

    @GetMapping("/test")
    public String testPage() {
        return "test";
    }
}


