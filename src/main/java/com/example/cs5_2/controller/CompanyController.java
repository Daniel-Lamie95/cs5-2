package com.example.cs5_2.controller;

import com.example.cs5_2.model.Company;
import com.example.cs5_2.model.Internship;
import com.example.cs5_2.model.User;
import com.example.cs5_2.service.CompanyService;
import com.example.cs5_2.service.InternshipService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/company")
public class CompanyController {

 
    private CompanyService companyService;
    private InternshipService internshipService;


    @GetMapping("/login")
    public String showLoginPage() {
        return "company-login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User u , HttpSession session, Model model) {
        try {
            Company company = companyService.loginCompany(u.getName(), u.getEmail(), u.getPassword());
            session.setAttribute("loggedCompany", company);

            if ("company".equalsIgnoreCase(company.getRole())) {
                return "redirect:/company/dashboard";
            }

            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("user",u);
            return "company-login";
        }
    }

  
    @GetMapping("/signup")
    public String showSignupPage(Model model) {
        model.addAttribute("company", new Company());
        return "company-signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute Company company, HttpSession session, Model model) {
        try {
            companyService.registerCompany(company);

            Company registeredCompany = companyService.findByEmail(company.getEmail());
            session.setAttribute("loggedCompany", registeredCompany);

            if ("company".equalsIgnoreCase(registeredCompany.getRole())) {
                return "redirect:/company/complete-profile";
            }

            return "redirect:/company/login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "company-signup";
        }
    }

    
    @GetMapping("/complete-profile")
    public String showCompleteProfilePage(HttpSession session, Model model) {
        Company loggedCompany = (Company) session.getAttribute("loggedCompany");

        if (loggedCompany == null) {
            return "redirect:/company/login";
        }

        model.addAttribute("company", new Company());
        return "company-complete-profile";
    }

    @PostMapping("/complete-profile")
    public String completeProfile(@ModelAttribute Company company, HttpSession session, Model model) {
        Company loggedCompany = (Company) session.getAttribute("loggedCompany");

        if (loggedCompany == null) {
            return "redirect:/company/login";
        }

        try {
            companyService.completeCompanyProfile(loggedCompany.getEmail(), company);

            Company updatedCompany = companyService.findByEmail(loggedCompany.getEmail());
            session.setAttribute("loggedCompany", updatedCompany);

            return "redirect:/company/dashboard";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "company-complete-profile";
        }
    }

    
    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        Company loggedCompany = (Company) session.getAttribute("loggedCompany");

        if (loggedCompany == null) {
            return "redirect:/company/login";
        }

        model.addAttribute("company", loggedCompany);
       // model.addAttribute("internships", internshipService.getInternshipsByCompany(loggedCompany));

        return "company-dashboard";
    }

    
    @GetMapping("/edit-profile")
    public String showEditProfilePage(HttpSession session, Model model) {
        Company loggedCompany = (Company) session.getAttribute("loggedCompany");

        if (loggedCompany == null) {
            return "redirect:/company/login";
        }

        model.addAttribute("company", loggedCompany);
        return "company-edit-profile";
    }

    @PostMapping("/edit-profile")
    public String editProfile(@ModelAttribute Company company,
                              HttpSession session,
                              Model model) {
        Company loggedCompany = (Company) session.getAttribute("loggedCompany");

        if (loggedCompany == null) {
            return "redirect:/company/login";
        }

        try {
            companyService.editCompanyProfile(loggedCompany.getEmail(), company);

            Company updatedCompany = companyService.findByEmail(loggedCompany.getEmail());
            session.setAttribute("loggedCompany", updatedCompany);

            return "redirect:/company/dashboard";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "company-edit-profile";
        }
    }

    /*
    @GetMapping("/add-internship")
    public String showAddInternshipPage(HttpSession session, Model model) {
        Company loggedCompany = (Company) session.getAttribute("loggedCompany");

        if (loggedCompany == null) {
            return "redirect:/company/login";
        }

        model.addAttribute("internship", new Internship());
        return "add-internship";
    }

    @PostMapping("/add-internship")
    public String addInternship(@ModelAttribute Internship internship,
                                HttpSession session,
                                Model model) {
        Company loggedCompany = (Company) session.getAttribute("loggedCompany");

        if (loggedCompany == null) {
            return "redirect:/company/login";
        }

        try {
            internshipService.addInternship(internship, loggedCompany);
            return "redirect:/company/dashboard";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "add-internship";
        }
    }

   
    @GetMapping("/edit-internship/{id}")
    public String showEditInternshipPage(@PathVariable int id,
                                         HttpSession session,
                                         Model model) {
        Company loggedCompany = (Company) session.getAttribute("loggedCompany");

        if (loggedCompany == null) {
            return "redirect:/company/login";
        }

        Internship internship = internshipService.findById(id);

        if (internship == null) {
            return "redirect:/company/dashboard";
        }

        if (internship.getCompany() == null ||
            !internship.getCompany().getEmail().equalsIgnoreCase(loggedCompany.getEmail())) {
            return "redirect:/company/dashboard";
        }

        model.addAttribute("internship", internship);
        return "edit-internship";
    }

    @PostMapping("/edit-internship/{id}")
    public String editInternship(@PathVariable int id,
                                 @ModelAttribute Internship internship,
                                 HttpSession session,
                                 Model model) {
        Company loggedCompany = (Company) session.getAttribute("loggedCompany");

        if (loggedCompany == null) {
            return "redirect:/company/login";
        }

        try {
            internshipService.updateInternship(id, internship, loggedCompany);
            return "redirect:/company/dashboard";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "edit-internship";
        }
    }

    
    @PostMapping("/delete-internship/{id}")
    public String deleteInternship(@PathVariable int id,
                                   HttpSession session,
                                   Model model) {
        Company loggedCompany = (Company) session.getAttribute("loggedCompany");

        if (loggedCompany == null) {
            return "redirect:/company/login";
        }

        try {
            internshipService.deleteInternship(id, loggedCompany);
            return "redirect:/company/dashboard";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("company", loggedCompany);
            model.addAttribute("internships",
                    internshipService.getInternshipsByCompany(loggedCompany));
            return "company-dashboard";
        }
    }*/

    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/company/login";
    }
}
