package com.example.cs5_2.controller;
import com.example.cs5_2.allvalidations.InternshipValidation;
import com.example.cs5_2.allvalidations.ValidationException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import com.example.cs5_2.model.Company;
import com.example.cs5_2.model.Internship;
import com.example.cs5_2.model.Student;
import com.example.cs5_2.service.InternshipService;

import jakarta.servlet.http.HttpSession;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/internships")
public class InternshipController {

    private final InternshipService service;

    public InternshipController(InternshipService service) {
        this.service = service;
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

        // Build an updated Internship object that contains required fields
        Internship updated = new Internship();
        // ensure required fields are present for service.updateInternship
        updated.setTitle(title != null && !title.trim().isEmpty() ? title.trim() : existing.getTitle());
        updated.setCompanyName(existing.getCompanyName());
        updated.setMaxApplicants(maxApplicants != null ? maxApplicants : existing.getMaxApplicants());

        // apply editable fields from the form
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

        // Normalize photo path
        normalizePhotoPath(updated);

        try {
            // Validate updated internship data
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
    @GetMapping("/set-cookie")
    public String setCookie(HttpServletResponse response) {

        Cookie cookie =
                new Cookie("theme", "dark");

        cookie.setMaxAge(7 * 24 * 60 * 60);

        cookie.setPath("/");

        response.addCookie(cookie);

        return "redirect:/";
    }

    @GetMapping("/Available-Internships")
    public String viewInternships(

            @RequestParam(required = false)
            String keyword,

            @CookieValue(
                    value = "lastSearch",
                    defaultValue = "")
            String savedKeyword,

            HttpServletResponse response,

            Model model) {

        if (!hasText(keyword)) {
            keyword = savedKeyword;
        }

        Cookie cookie =
                new Cookie(
                        "lastSearch",
                        keyword == null ? "" : keyword
                );

        cookie.setMaxAge(
                7 * 24 * 60 * 60
        );

        cookie.setPath("/");

        response.addCookie(cookie);

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

    @GetMapping("/read-cookie")
    @ResponseBody
    public String readCookie(
            @CookieValue(
                    value = "theme",
                    defaultValue = "light")
            String theme) {

        return "Theme is: " + theme;
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

        boolean showApplyButton =
                session.getAttribute("user")
                        instanceof Student;

        boolean showOwnerEditButton = false;

        Object companySession = session.getAttribute("company");

        if (companySession instanceof Company loggedIn) {

            showOwnerEditButton =
                    internshipOwnedByCompany(
                            internship,
                            loggedIn);
        }

        model.addAttribute("showApplyButton",
                showApplyButton);

        model.addAttribute("showOwnerEditButton",
                showOwnerEditButton);

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
}

