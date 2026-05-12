package com.example.cs5_2.controller;

import com.example.cs5_2.model.Company;
import com.example.cs5_2.model.Student;
import com.example.cs5_2.service.CompanyService;
import com.example.cs5_2.service.StudentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final StudentService studentService;
    private final CompanyService companyService;

    public AuthController(StudentService studentService, CompanyService companyService) {
        this.studentService = studentService;
        this.companyService = companyService;
    }

    @GetMapping("/login")
    public String loginPage() {

        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        @RequestParam(defaultValue = "student") String userType,
                        HttpSession session,
                        Model model) {
        try {
            if ("company".equalsIgnoreCase(userType)) {
                Company company = companyService.login(email, password);
                session.removeAttribute("student");
                session.setAttribute("company", company);
                return "redirect:/company/dashboard";
            }

            Student student = studentService.loginStudent(email, password);
            session.removeAttribute("company");
            session.setAttribute("student", student);
            return "redirect:/student-dashboard";

        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("enteredEmail",email);
            model.addAttribute("selectedUserType", userType);
            return "login";
        }
    }


}
