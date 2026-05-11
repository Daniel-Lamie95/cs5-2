package com.example.cs5_2.controller;

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

    @GetMapping
    public String viewInternships(
            @RequestParam(required = false) String keyword,
            Model model) {

        List<Internship> internships = hasText(keyword)
                ? service.searchInternshipsByTitle(keyword)
                : service.getAllInternships();

        addInternshipListModel(model, internships);

        model.addAttribute("keyword",
                keyword == null ? "" : keyword);

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

    @GetMapping("/edit")
    public String showEditForm(
            @RequestParam Long id,
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

        return "edit-Internship-profile";
    }

    @PostMapping("/edit")
    public String updateFromEditForm(

            @RequestParam Long id,

            @RequestParam(required = false)
            Integer duration,

            @RequestParam(required = false)
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @RequestParam(required = false)
            String description,

            Model model) {

        Internship internship = new Internship();

        if (duration != null) {
            internship.setDuration(duration);
        }

        internship.setStartDate(startDate);

        internship.setRequirements(description);

        return updateExistingInternship(
                id,
                internship,
                model
        );
    }

    private String updateExistingInternship(
            Long id,
            Internship internship,
            Model model) {

        try {

            service.updateInternship(id,
                    internship);

            return "redirect:/internships";
        }

        catch (IllegalArgumentException e) {

            model.addAttribute("message",
                    e.getMessage());

            Internship existing =
                    service.findById(id);

            model.addAttribute("internship",
                    existing);

            model.addAttribute("data",
                    existing);

            return "edit-Internship-profile";
        }
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
}

