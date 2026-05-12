package com.example.cs5_2.controller;
import com.example.cs5_2.DTO.StudentRegisterDTO;
import com.example.cs5_2.model.ApplicationStatus;

import org.springframework.ui.Model;
import com.example.cs5_2.model.Company;
import com.example.cs5_2.model.Student;
import com.example.cs5_2.service.CompanyService;
import com.example.cs5_2.service.InternshipService;
import com.example.cs5_2.service.StudentService;
import com.example.cs5_2.service.ApplicationService;
import com.example.cs5_2.model.Application;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
@Controller
public class StudentController {
    private final StudentService studentService;
    private final CompanyService companyService;
    private final ApplicationService applicationService;
    
    //private final InternshipService internshipService;


    public StudentController(StudentService studentService, CompanyService companyService, ApplicationService applicationService, InternshipService internshipService) {
        this.studentService = studentService;
        this.companyService = companyService;
        this.applicationService = applicationService;
      //  this.internshipService = internshipService;
    }

    @GetMapping("/register")
    public String registerPage() {
        return "student-register";
    }


    @PostMapping("/register")
    public String register(@ModelAttribute StudentRegisterDTO studentDto, Model model) {
        try {
            studentService.registerStudent(studentDto);
            model.addAttribute("message", "Registration successful! Please login.");
            return "login"; // Redirect to login page
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "student-register";
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "student-register";
        }
    }

    @GetMapping("/student-profile")
    public String studentProfile(HttpSession session, Model model) {
        Object user = session.getAttribute("user");
        if (!(user instanceof Student student)) {
            return "redirect:/login";
        }

        model.addAttribute("student", student);
        return "student-profile";
    }

    @GetMapping("/student-dashboard")
    public String studentDashboard(HttpSession session, Model model) {
        Object user = session.getAttribute("user");
        if (!(user instanceof Student student)) {
            return "redirect:/login";
        }

        // add student to model
        model.addAttribute("student", student);

        // compute application counts and list from ApplicationService
        java.util.List<Application> allApps = applicationService.getAllApplications();
        java.util.List<Application> studentApps = new java.util.ArrayList<>();
        for (Application app : allApps) {
            if (app.getStudent() != null && app.getStudent().getName() != null
                    && app.getStudent().getName().equals(student.getName())) {
                studentApps.add(app);
            }
        }

        int appliedCount = studentApps.size();
        long acceptedCountLong = studentApps.stream().filter(a -> a.getStatus() == ApplicationStatus.ACCEPTED).count();
        int acceptedCount = (int) acceptedCountLong;

        model.addAttribute("appliedCount", appliedCount);
        model.addAttribute("acceptedCount", acceptedCount);
        model.addAttribute("studentApplications", studentApps);

        return "student-dashboard";
    }

    @GetMapping("/edit-student-profile")
    public String editStudentProfile(HttpSession session, Model model) {
        Object user = session.getAttribute("user");
        if (!(user instanceof Student student)) {
            return "redirect:/login";
        }

        model.addAttribute("student", student);
        return "edit-student-profile";
    }


    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

   @PostMapping("/update")
    public String updateStudent(@ModelAttribute Student updated,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        try {
            Object user = session.getAttribute("user");
            if (!(user instanceof Student currentStudent)) {
                return "redirect:/login";
            }

            Student savedStudent = studentService.updateStudent(currentStudent.getEmail(), updated);
            session.setAttribute("user", savedStudent);
            redirectAttributes.addFlashAttribute("message", "Profile updated successfully!");
            return "redirect:/student-profile";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/student-profile";
        }
    }
   
   
   
   
 /* @GetMapping("/latest-internships")
public String latestInternships(HttpSession session, Model model) {

    Object user = session.getAttribute("user");

    if (!(user instanceof Student student)) {
        return "redirect:/login";
    }

    List<Internship> internships = internshipService.getAllInternships();

    Map<String, List<Internship>> internshipsByCompany =
            internships.stream()
                    .collect(Collectors.groupingBy(Internship::getCompanyName));

    model.addAttribute("student", student);
    model.addAttribute("internshipsByCompany", internshipsByCompany);

    return "latest-internships";
}
   */
   
   
   
   
   
}
