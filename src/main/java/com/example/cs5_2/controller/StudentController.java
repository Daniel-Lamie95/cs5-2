package com.example.cs5_2.controller;
import org.springframework.ui.Model;
import com.example.cs5_2.model.Student;

import com.example.cs5_2.service.StudentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class StudentController {
    private StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute Student student, Model model) {
        try {
            studentService.registerStudent(student);
            model.addAttribute("message", "Registration successful! Please login.");
            return "login"; // Redirect to login page
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "register"; // Stay on register page
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "register";
        }
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        try {
            Student student = studentService.loginStudent(email, password);

            if (student == null) {
                model.addAttribute("error", "Invalid Login!");
                return "login";
            }

            // Store student in session
            session.setAttribute("user", student);
            return "dashboard"; // Redirect to dashboard/main page

        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @PostMapping("/update")
    public String updateStudent(@RequestParam String email,
                                @ModelAttribute Student updated,
                                Model model) {
        try {
            Student student = studentService.updateStudent(email, updated);
            model.addAttribute("message", "Profile updated successfully!");
            return "redirect:/profile";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "profile";
        }
    }
}
