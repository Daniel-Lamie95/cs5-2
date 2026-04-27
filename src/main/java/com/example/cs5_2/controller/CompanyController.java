package com.example.cs5_2.controller;

import com.example.cs5_2.model.Company;
import com.example.cs5_2.service.CompanyService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/company")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

   
    @GetMapping("/register")
    public String showRegister(Model model) {
        model.addAttribute("company", new Company());
        return "company-register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute Company company,HttpSession session,Model model) {

        try {
            Company savedCompany = companyService.register(company);

            session.setAttribute("company", savedCompany);

            return "redirect:/company/dashboard";

        } catch (IllegalArgumentException e) {
            model.addAttribute("message", e.getMessage());
            model.addAttribute("company", company);
            return "company-register";
        }
    }

  
    @GetMapping("/login")
    public String showLogin(Model model) {
        model.addAttribute("company", new Company());
        return "company-login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute Company company,HttpSession session,Model model) {

        try {
            Company loggedCompany = companyService.login(
                    company.getEmail(),
                    company.getPassword()
            );

            session.setAttribute("company", loggedCompany);

            return "redirect:/company/dashboard";

        } catch (IllegalArgumentException e) {
            model.addAttribute("message", e.getMessage());
            model.addAttribute("company", company);
            return "company-login";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {

        Company company = (Company) session.getAttribute("company");

        if (company == null) return "redirect:/company/login";

        model.addAttribute("company", company);

        return "company-dashboard";
    }
    
    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {

        Company company = (Company) session.getAttribute("company");

        if (company == null) return "redirect:/company/login";

        Company c = companyService.getById(company.getId());

        model.addAttribute("company", c);

        return "company-profile";
    }

   
    @GetMapping("/profile/edit")
    public String editProfile(HttpSession session, Model model) {

        Company company = (Company) session.getAttribute("company");

        if (company == null) return "redirect:/company/login";

        model.addAttribute("company", company);

        return "edit-company-profile";
    }

   
    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute Company updatedCompany,HttpSession session,Model model) {

        Company company = (Company) session.getAttribute("company");

        if (company == null) return "redirect:/company/login";

        try {
            Company saved = companyService.updateProfile(
                    company.getId(),
                    updatedCompany
            );

            session.setAttribute("company", saved);

            return "redirect:/company/profile";

        } catch (IllegalArgumentException e) {
            model.addAttribute("message", e.getMessage());
            model.addAttribute("company", updatedCompany);
            return "edit-company-profile";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/company/login";
    }
}