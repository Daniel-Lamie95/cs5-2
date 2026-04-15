package com.example.cs5_2.controller;


import com.example.cs5_2.model.Company;
import com.example.cs5_2.service.CompanyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService service;

    public CompanyController(CompanyService service) {
        this.service = service;
    }

    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("company", new Company());
        return "company-form";
    }

    @PostMapping
    public String createCompany(@ModelAttribute Company company, Model model) {
        try {
            Company saved = service.addCompany(company);
            model.addAttribute("company", saved);
            model.addAttribute("message", "Company account created successfully.");
            return "company-profile";
        } catch (IllegalArgumentException e) {
            model.addAttribute("message", e.getMessage());
            model.addAttribute("company", company);
            return "company-form";
        }
    }

    @GetMapping
    public String viewCompanies(Model model) {
        model.addAttribute("companies", service.getAllCompanies());
        return "company-list";
    }

    @GetMapping("/profile")
    public String showProfile(@RequestParam String email, Model model) {
        Company company = service.getCompanyByEmail(email);

        if (company == null) {
            model.addAttribute("message", "Company not found.");
            return "result";
        }

        model.addAttribute("company", company);
        return "company-profile";
    }

    @PostMapping("/update")
    public String updateCompany(@RequestParam String email,
                                @ModelAttribute Company company,
                                Model model) {
        try {
            Company updated = service.updateCompany(email, company);
            model.addAttribute("company", updated);
            model.addAttribute("message", "Company updated successfully.");
            return "company-profile";
        } catch (IllegalArgumentException e) {
            model.addAttribute("message", e.getMessage());
            model.addAttribute("company", company);
            return "company-form";
        }
    }

    @PostMapping("/delete")
    public String deleteCompany(@RequestParam String email, Model model) {
        try {
            service.deleteCompany(email);
            model.addAttribute("message", "Company deleted successfully.");
        } catch (IllegalArgumentException e) {
            model.addAttribute("message", e.getMessage());
        }

        model.addAttribute("companies", service.getAllCompanies());
        return "company-list";
    }
}