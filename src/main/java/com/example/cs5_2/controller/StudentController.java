package com.example.cs5_2.controller;
import com.example.cs5_2.model.ApplicationStatus;
import org.springframework.ui.Model;
import com.example.cs5_2.model.Company;
import com.example.cs5_2.model.Student;

import com.example.cs5_2.service.CompanyService;
import com.example.cs5_2.service.StudentService;
import com.example.cs5_2.service.ApplicationService;
import com.example.cs5_2.model.Application;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class StudentController {
    private final StudentService studentService;
    private final CompanyService companyService;
    private final ApplicationService applicationService;

    public StudentController(StudentService studentService, CompanyService companyService, ApplicationService applicationService) {
        this.studentService = studentService;
        this.companyService = companyService;
        this.applicationService = applicationService;
    }

    @GetMapping("/register")
    public String registerPage() {
        return "student-register";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute Student student, Model model) {
        try {
            studentService.registerStudent(student);
            model.addAttribute("message", "Registration successful! Please login.");
            return "login"; // Redirect to login page
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "student-register"; // Stay on register page
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "student-register";
        }
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
                session.removeAttribute("user");
                session.setAttribute("company", company);
                return "redirect:/company/dashboard";
            }

            Student student = studentService.loginStudent(email, password);
            session.removeAttribute("company");
            session.setAttribute("user", student);
            return "redirect:/student-dashboard";

        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("selectedUserType", userType);
            return "login";
        }
    }

    @GetMapping("/student-home")
    public String studentHome(HttpSession session) {
        if (!(session.getAttribute("user") instanceof Student)) {
            return "redirect:/login";
        }
        return "student-home";
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

    @PostMapping("/profile-photo")
    public String uploadProfilePhoto(@RequestParam("photo") MultipartFile photo,
                                     HttpSession session,
                                     RedirectAttributes redirectAttributes) {
        Object user = session.getAttribute("user");
        if (!(user instanceof Student student)) {
            return "redirect:/login";
        }

        if (photo.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Please select an image.");
            return "redirect:/student-profile";
        }

        try {
            studentService.uploadProfilePhoto(student.getId(), photo.getContentType(), photo.getBytes());
            student.setProfilePhotoContentType(photo.getContentType());
            session.setAttribute("user", student);
            redirectAttributes.addFlashAttribute("message", "Profile photo updated.");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Could not read uploaded file.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/student-profile";
    }

    @GetMapping("/student/{id}/photo")
    @ResponseBody
    public ResponseEntity<byte[]> getStudentProfilePhoto(@PathVariable Long id) {
        byte[] photo = studentService.getProfilePhoto(id);
        if (photo == null || photo.length == 0) {
            return ResponseEntity.notFound().build();
        }

        String contentType = studentService.getProfilePhotoContentType(id);
        MediaType mediaType;
        try {
            mediaType = (contentType == null || contentType.isBlank())
                    ? MediaType.IMAGE_JPEG
                    : MediaType.parseMediaType(contentType);
        } catch (Exception e) {
            mediaType = MediaType.IMAGE_JPEG;
        }

        return ResponseEntity.ok().contentType(mediaType).body(photo);
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

   @PostMapping("/update")
    public String updateStudent(@RequestParam String email,
                                @ModelAttribute Student updated,
                                Model model) {
        try {
            // call service to update student; result isn't needed here so don't create an unused local variable
            studentService.updateStudent(email, updated);
            model.addAttribute("message", "Profile updated successfully!");
            return "redirect:/student-profile";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "student-profile";
        }
    }
}
