package com.example.apa_project.controller;

import com.example.apa_project.model.StudentProfile;
import com.example.apa_project.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/students")
    public String studentsPage(Model model) {
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("studentForm", new StudentProfile());
        return "students";
    }

    @PostMapping("/students")
    public String addStudent(@ModelAttribute("studentForm") StudentProfile studentProfile,
                             RedirectAttributes redirectAttributes) {
        studentService.addStudent(studentProfile);
        redirectAttributes.addFlashAttribute("successMessage", "Student added successfully.");
        return "redirect:/students";
    }
}

