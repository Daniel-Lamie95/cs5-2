package com.example.cs5_2.controller;

import com.example.cs5_2.model.Company;
import com.example.cs5_2.model.Internship;
import com.example.cs5_2.service.CompanyService;
import com.example.cs5_2.service.InternshipService;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/company")
public class CompanyController {

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

        } catch (IllegalArgumentException e) {
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

        model.addAttribute("company", company);
        // provide the company's posted internships so the template can render without errors
        model.addAttribute("postedInternships",
                internshipService.getInternshipsByCompany(company.getName()));

        return "company-dashboard";
    }

    @GetMapping({"/profile", "/company-profile"})
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
            model.addAttribute("company", company);
            return "edit-company-profile";
        }
    }
    // logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}