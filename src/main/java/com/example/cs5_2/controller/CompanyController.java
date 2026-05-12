package com.example.cs5_2.controller;

import com.example.cs5_2.allvalidations.ValidationException;
import com.example.cs5_2.allvalidations.InternshipValidation;
import com.example.cs5_2.model.Company;
import com.example.cs5_2.model.Internship;
import com.example.cs5_2.service.CompanyService;
import com.example.cs5_2.service.InternshipService;
import java.util.Collections;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/company")
public class CompanyController{

    private final CompanyService companyService;
    private final InternshipService internshipService;

    public CompanyController(CompanyService companyService, InternshipService internshipService) {
               this.companyService = companyService;
               this.internshipService = internshipService;
}

    @GetMapping("/register")
    public String registerPage() {
        return "company-register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute Company company, Model model) {
        try {
            companyService.register(company);

            model.addAttribute("message", "Company registration successful! Please login.");
            model.addAttribute("selectedUserType", "company");

            return "login";

        } catch (IllegalArgumentException | ValidationException e) {
            model.addAttribute("error", e.getMessage());
            return "company-register";
        }
    }
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {

        Object companyObj = session.getAttribute("company");

        if (!(companyObj instanceof Company company)) {
            return "redirect:/login";
        }

        var postedInternships = internshipService.getInternshipsByCompany(company.getName());
        postedInternships = postedInternships != null ? postedInternships : Collections.emptyList();
        
        // Calculate statistics
        int totalApplicants = (int) postedInternships.stream()
                .mapToInt(Internship::getApplicantsCount)
                .sum();
        
        long activeInternships = postedInternships.stream()
                .filter(i -> i.getEndDate() != null && i.getEndDate().isAfter(java.time.LocalDate.now()))
                .count();

        model.addAttribute("postedInternships", postedInternships);
        model.addAttribute("totalApplicants", totalApplicants);
        model.addAttribute("activeInternships", activeInternships);
        
        model.addAttribute("company", company);
        return "company-dashboard";
        
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {

        Object companyObj = session.getAttribute("company");

        if (!(companyObj instanceof Company company)) {
            return "redirect:/login";
        }

        model.addAttribute("company", company);
        return "company-profile";
    }

    @GetMapping("/edit-profile")
    public String editProfile(HttpSession session, Model model) {

        Object companyObj = session.getAttribute("company");

        if (!(companyObj instanceof Company company)) {
            return "redirect:/login";
        }

        model.addAttribute("company", company);
        return "edit-company-profile";
    }

    @PostMapping("/update")
    public String updateProfile(@ModelAttribute Company updatedCompany,
                                HttpSession session,
                                Model model) {

        Object companyObj = session.getAttribute("company");

        if (!(companyObj instanceof Company company)) {
            return "redirect:/login";
        }

        try {
            Company savedCompany =
                    companyService.updateProfile(company.getId(), updatedCompany);

            session.setAttribute("company", savedCompany);

            return "redirect:/company/profile";

        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("company", updatedCompany);
            return "edit-company-profile";
        }
    }
    
    // logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
    
    
    
    

    @GetMapping("/post-internship")
    public String showPostInternshipForm(HttpSession session, Model model) {
        Object companyObj = session.getAttribute("company");

        if (!(companyObj instanceof Company company)) {
            return "redirect:/login";
        }

        model.addAttribute("internship", new Internship());
        model.addAttribute("company", company);
        return "company-post-internship";
    }

    @PostMapping("/post-internship")
    public String postInternship(@ModelAttribute Internship internship,
                                 HttpSession session,
                                 Model model) {
        Object companyObj = session.getAttribute("company");

        if (!(companyObj instanceof Company company)) {
            return "redirect:/login";
        }

        try {
            // Set company name and company reference
            internship.setCompanyName(company.getName());
            internship.setCompany(company);
            alignInternshipPhotoWithCompany(internship, company);

            // Validate internship data
            InternshipValidation.validateAdd(internship);

            // Save internship
            internshipService.addInternship(internship);

            return "redirect:/company/dashboard";

        } catch (IllegalArgumentException | ValidationException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("internship", internship);
            model.addAttribute("company", company);
            return "company-post-internship";
        }
    }

    
    /*** Internship templates use {@code /images/} + {@link Internship#getPhotoPath()} (file name only).
     * When no image is given, use the same asset as the company logo so listing and detail views match.
     */
    private static void alignInternshipPhotoWithCompany(Internship internship, Company company) {
        String p = internship.getPhotoPath();
        if (p != null) {
            p = p.trim();
            String lower = p.toLowerCase();
            if (lower.startsWith("/images/")) {
                p = p.substring("/images/".length());
            } else if (lower.startsWith("images/")) {
                p = p.substring("images/".length());
            }
            internship.setPhotoPath(p.isEmpty() ? null : p);
        }
        if (internship.getPhotoPath() == null || internship.getPhotoPath().isEmpty()) {
            String fileName = imagesFilenameFromLogoPath(company.getLogo());
            if (fileName != null && !fileName.isEmpty()) {
                internship.setPhotoPath(fileName);
            }
        }
    }

    private static String imagesFilenameFromLogoPath(String logo) {
        if (logo == null || logo.isBlank()) {
            return null;
        }
        String t = logo.trim();
        String lower = t.toLowerCase();
        if (lower.startsWith("/images/")) {
            return t.substring("/images/".length());
        }
        if (lower.startsWith("images/")) {
            return t.substring("images/".length());
        }
        int slash = t.lastIndexOf('/');
        if (slash >= 0 && slash < t.length() - 1) {
            return t.substring(slash + 1);
        }
        return t;
    }
}