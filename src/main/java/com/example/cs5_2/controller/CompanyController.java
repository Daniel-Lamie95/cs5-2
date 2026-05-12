package com.example.cs5_2.controller;

import com.example.cs5_2.allvalidations.ValidationException;
import com.example.cs5_2.allvalidations.InternshipValidation;
import com.example.cs5_2.model.*;
import com.example.cs5_2.service.*;
import java.util.List;
import com.example.cs5_2.model.SimpleRanking;
import java.util.Collections;
import java.util.List;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import com.example.cs5_2.model.Application;
import com.example.cs5_2.service.ApplicationService;
import java.util.List;
import java.util.Map;

import com.example.cs5_2.model.ApplicationStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/company")
public class CompanyController{

    private final CompanyService companyService;
    private final InternshipService internshipService;
    private final ApplicationService applicationService;
    private final CVService cvService;
    private final RankingService rankingService;
    private final StudentService studentService;


    public CompanyController(CompanyService companyService, InternshipService internshipService,
			ApplicationService applicationService, CVService cvService, RankingService rankingService, StudentService studentService) {
		super();
		this.companyService = companyService;
		this.internshipService = internshipService;
		this.applicationService = applicationService;
        this.cvService = cvService;
		this.rankingService = rankingService;
		this.studentService = studentService;
	}

	@GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("company", new Company());
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
            model.addAttribute("company", company);
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

        // Get all applications for this company's internships
        List<Application> applications = applicationService.getApplicationsByCompany(company.getName());
        applications = applications != null ? applications : Collections.emptyList();

        // Calculate statistics
        int totalApplicants = applications.size();

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

    // GET mapping to show applications
    @GetMapping("/application")
    public String viewApplications(HttpSession session, Model model) {
        Object companyObj = session.getAttribute("company");
        if (!(companyObj instanceof Company company)) {
            return "redirect:/login";
        }

        // Get all applications for this company's internships
        List<Application> applications = applicationService.getApplicationsByCompany(company.getName());

        // Count statuses
        long acceptedCount = applications.stream()
                .filter(a -> a.getStatus() == ApplicationStatus.ACCEPTED)
                .count();
        long pendingCount = applications.stream()
                .filter(a -> a.getStatus() == ApplicationStatus.PENDING)
                .count();
        long rejectedCount = applications.stream()
                .filter(a -> a.getStatus() == ApplicationStatus.REJECTED)
                .count();

        model.addAttribute("applications", applications);
        model.addAttribute("statuses", ApplicationStatus.values()); // Pass enum values for dropdown
        model.addAttribute("acceptedCount", acceptedCount);
        model.addAttribute("pendingCount", pendingCount);
        model.addAttribute("rejectedCount", rejectedCount);
        model.addAttribute("company", company);

        return "applications"; // Must match applications.html
    }

    @PostMapping("/application/update")
    public String updateApplicationStatus(@RequestParam Integer id, @RequestParam String status) {
        applicationService.updateStatus(id, status); // method handles enum conversion
        return "redirect:/company/application";
    }




    @GetMapping("/university-ranking")
    public String universityRanking(HttpSession session, Model model) {

        Object companyObj = session.getAttribute("company");

        if (!(companyObj instanceof Company company)) {
            return "redirect:/login";
        }

        List<SimpleRanking> rankings =
                rankingService.getUniversityRanking(studentService.getAllStudents());

        model.addAttribute("rankings", rankings);
        model.addAttribute("company", company);

        return "university-ranking";
    }
    @GetMapping("/company/cv/{id}")
    public String viewCompanyCV(@PathVariable Long id, Model model) {
        BuildCV cv = cvService.findById(id);  // return null if not found
        if (cv == null) {
            return "redirect:/company/application";
        }
        model.addAttribute("cv", cv);
        return "company-cv-view"; // Thymeleaf template name
    }
}
