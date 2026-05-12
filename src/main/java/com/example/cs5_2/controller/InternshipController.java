package com.example.cs5_2.controller;
import com.example.cs5_2.allvalidations.InternshipValidation;
import com.example.cs5_2.allvalidations.ValidationException;
import com.example.cs5_2.model.Application;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import com.example.cs5_2.model.Company;
import com.example.cs5_2.model.Internship;
import com.example.cs5_2.model.Student;
import com.example.cs5_2.service.ApplicationService;
import com.example.cs5_2.service.InternshipService;

import jakarta.servlet.http.HttpSession;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/internships")
public class InternshipController {

    private final InternshipService service;
    private final ApplicationService applicationService;

    public InternshipController(InternshipService service,
                                ApplicationService applicationService) {
        this.service = service;
        this.applicationService = applicationService;
    }



    @GetMapping("/edit")
    public String editForm(@RequestParam Long id,
                           HttpSession session,
                           Model model) {

        Object companyObj = session.getAttribute("company");

        if (!(companyObj instanceof Company company)) {
            return "redirect:/login";
        }

        Internship internship = service.findById(id);

        if (internship == null) {
            model.addAttribute("message", "Internship not found");
            return "redirect:/internships";
        }

        if (!internshipOwnedByCompany(internship, company)) {
            model.addAttribute("message", "Not authorized to edit this internship");
            return "redirect:/internships";
        }

        model.addAttribute("data", internship);
        model.addAttribute("internship", internship);

        return "edit-Internship-profile";
    }

    @PostMapping("/edit")
    public String submitEdit(@RequestParam Long id,
                             @RequestParam(required = false) Integer duration,
                             @RequestParam(required = false, name = "description") String description,
                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                             @RequestParam(required = false) String photoPath,
                             @RequestParam(required = false) Integer maxApplicants,
                             @RequestParam(required = false) String title,
                             HttpSession session,
                             Model model) {

        Object companyObj = session.getAttribute("company");

        if (!(companyObj instanceof Company company)) {
            return "redirect:/login";
        }

        Internship existing = service.findById(id);

        if (existing == null) {
            model.addAttribute("message", "Internship not found");
            return "redirect:/internships";
        }

        if (!internshipOwnedByCompany(existing, company)) {
            model.addAttribute("message", "Not authorized to edit this internship");
            return "redirect:/internships";
        }

        Internship updated = new Internship();

        updated.setTitle(title != null && !title.trim().isEmpty() ? title.trim() : existing.getTitle());
        updated.setCompanyName(existing.getCompanyName());
        updated.setMaxApplicants(maxApplicants != null ? maxApplicants : existing.getMaxApplicants());

        updated.setDuration(duration != null ? duration : existing.getDuration());
        updated.setDescription(description != null ? description.trim() : existing.getDescription());
        updated.setStartDate(startDate != null ? startDate : existing.getStartDate());
        updated.setEndDate(existing.getEndDate());
        updated.setPhotoPath(
                photoPath != null && !photoPath.trim().isEmpty()
                        ? photoPath.trim()
                        : existing.getPhotoPath());

        // Keep the existing company reference unchanged (do not edit company from internship form)
        updated.setCompany(existing.getCompany());


        normalizePhotoPath(updated);

        try {

            InternshipValidation.validateAdd(updated);

            service.updateInternship(id, updated);
            return "redirect:/internships/internship-details?id=" + id;
        } catch (IllegalArgumentException | ValidationException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("data", existing);
            model.addAttribute("internship", existing);
            return "edit-Internship-profile";
        }
    }

    @GetMapping("/Available-Internships")
    public String viewInternships(

            @RequestParam(required = false)
            String keyword,
            Model model) {

        if (hasText(keyword)) {

            Cookie cookie =
                    new Cookie(
                            "lastSearch",
                            keyword
                    );

            cookie.setMaxAge(
                    7 * 24 * 60 * 60
            );

            cookie.setPath("/");

            response.addCookie(cookie);
        }
        else {

            keyword = "";
        }

        List<Internship> internships =

                hasText(keyword)

                        ?

                        service.searchInternshipsByTitle(keyword)

                        :

                        service.getAllInternships();

        addInternshipListModel(
                model,
                internships
        );

        model.addAttribute(
                "keyword",
                keyword == null ? "" : keyword
        );

        return "Available-Internships";
    }


    @GetMapping("/internship-details")
    public String showInternshipDetails(
            @RequestParam Long id,
            HttpSession session,
            Model model) {

        Internship internship = service.findById(id);

        if (internship == null) {

            model.addAttribute("message",
                    "Internship not found");

            return "redirect:/internships";
        }

        model.addAttribute("internship",
                internship);

        model.addAttribute("data",
                internship);

        // This is the correct place to add it:
        boolean showApplyButton = session.getAttribute("user") instanceof Student;
        model.addAttribute("showApplyButton", showApplyButton);

        // Check if student already applied to this internship
        Student student = null;
        if (session.getAttribute("user") instanceof Student s) {
            student = s;
        }

        if (student != null) {
            try {
                Application existingApp = applicationService.getStudentApplicationForInternship(student.getId(), id);
                if (existingApp != null) {
                    model.addAttribute("studentApplication", existingApp);
                    model.addAttribute("hasAlreadyApplied", true);
                    model.addAttribute("applicationStatus", existingApp.getStatus());
                } else {
                    model.addAttribute("hasAlreadyApplied", false);
                }
            } catch (Exception e) {
                model.addAttribute("hasAlreadyApplied", false);
            }
        }

        // Existing logic for showing edit button for the owner
        boolean showOwnerEditButton = false;

        Object companySession = session.getAttribute("company");

        if (companySession instanceof Company loggedIn) {
            showOwnerEditButton = internshipOwnedByCompany(internship, loggedIn);
        }
        model.addAttribute("showOwnerEditButton", showOwnerEditButton);

        // Determine logged-in user type for navigation
        boolean isStudent = session.getAttribute("user") instanceof Student;
        boolean isCompany = session.getAttribute("company") instanceof Company;
        model.addAttribute("isStudent", isStudent);
        model.addAttribute("isCompany", isCompany);

        return "internship-details";
    }

        private static boolean internshipOwnedByCompany(
                Internship internship,
                Company company) {

            if (internship.getCompany() != null
                    && company.getId() != null) {

                return company.getId()
                        .equals(
                                internship.getCompany()
                                        .getId());
            }

            if (internship.getCompanyName() != null
                    && company.getName() != null) {

                return internship.getCompanyName()
                        .equalsIgnoreCase(
                                company.getName());
            }

            return false;
        }



    private void addInternshipListModel(
            Model model,
            List<Internship> internships) {

        model.addAttribute("internships",
                internships);

        model.addAttribute("totalInternships",
                internships.size());

        model.addAttribute("totalApplicants",

                internships.stream()
                        .mapToInt(
                                Internship::getApplicantsCount)
                        .sum());

        model.addAttribute("activeInternships",

                internships.stream()

                        .filter(i ->
                                i.getEndDate() != null
                                        &&
                                        i.getEndDate()
                                                .isAfter(
                                                        LocalDate.now()
                                                ))

                        .count());
    }

    private static boolean hasText(
            String value) {

        return value != null
                &&
                !value.trim().isEmpty();
    }

    private static void normalizePhotoPath(Internship internship) {
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
    }

    // Apply to internship - direct submission with validation
    @PostMapping("/apply")
    public String applyToInternship(@RequestParam(name = "id") Long internshipId,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        // Get logged-in student from session
        Object user = session.getAttribute("user");
        if (!(user instanceof Student student)) {
            return "redirect:/login";
        }

        // Fetch internship
        Internship internship = service.findById(internshipId);
        if (internship == null) {
            redirectAttributes.addFlashAttribute("error", "Internship not found");
            return "redirect:/internships/Available-Internships";
        }

        try {
            // Check if student already applied to this internship
            if (applicationService.hasStudentApplied(student.getId(), internshipId)) {
                redirectAttributes.addFlashAttribute("error", "You have already applied to this internship. You can revoke your application if you'd like to apply again.");
                return "redirect:/internships/internship-details?id=" + internshipId;
            }

            // Validate student has a CV (this will throw exception if not)
            applicationService.addApplication(student.getId(), student.getName(), internshipId);

            // Success - redirect back to internship details with success message
            redirectAttributes.addFlashAttribute("message", "Application submitted successfully!");
            return "redirect:/internships/internship-details?id=" + internshipId;
        } catch (IllegalArgumentException e) {
            // Student has no CV or other validation error
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/internships/internship-details?id=" + internshipId;
        }
    }

    // Revoke/Withdraw application
    @PostMapping("/revoke-application")
    public String revokeApplication(@RequestParam(name = "id") Long internshipId,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        // Get logged-in student from session
        Object user = session.getAttribute("user");
        if (!(user instanceof Student student)) {
            return "redirect:/login";
        }

        try {
            applicationService.revokeApplication(student.getId(), internshipId);
            redirectAttributes.addFlashAttribute("message", "Application withdrawn successfully. You can apply again if you'd like.");
            return "redirect:/internships/internship-details?id=" + internshipId;
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/internships/internship-details?id=" + internshipId;
        }
    }
}
