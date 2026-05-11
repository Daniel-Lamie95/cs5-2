package com.example.cs5_2.controller;

import com.example.cs5_2.model.Internship;
import com.example.cs5_2.service.InternshipService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

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
    public String viewInternships(@RequestParam(required = false) String keyword, Model model) {
        List<Internship> internships = hasText(keyword)
                ? service.searchInternshipsByTitle(keyword)
                : service.getAllInternships();

        addInternshipListModel(model, internships);
        model.addAttribute("keyword", keyword == null ? "" : keyword);
        return "Available-Internships";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam Long id, Model model) {
        Internship internship = service.findById(id);

        if (internship == null) {
            model.addAttribute("message", "Internship not found");
            addInternshipListModel(model, service.getAllInternships());
            return "Available-Internships";
        }

        model.addAttribute("internship", internship);
        model.addAttribute("data", InternshipEditData.from(internship));
        return "edit-Internship-profile";
    }

    @PostMapping("/edit")
    public String updateFromEditForm(@RequestParam Long id,
                                     @RequestParam(required = false) Integer duration,
                                     @RequestParam(required = false)
                                     @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                     LocalDate startDate,
                                     @RequestParam(required = false) String description,
                                     @RequestParam(required = false) String location,
                                     @RequestParam(required = false) String field,
                                     Model model) {
        Internship internship = new Internship();
        if (duration != null) {
            internship.setDuration(duration);
        }
        internship.setStartDate(startDate);
        internship.setRequirements(description);

        // Ignore location and field as they come from company, not internship

        return updateExistingInternship(id, internship, model);
    }

    private String updateExistingInternship(Long id, Internship internship, Model model) {
        try {
            service.updateInternship(id, internship);
            return "redirect:/company-dashboard";
        } catch (IllegalArgumentException e) {
            model.addAttribute("message", e.getMessage());

            Internship existing = service.findById(id);
            Internship dataSource = existing == null ? internship : existing;
            model.addAttribute("internship", dataSource);
            model.addAttribute("data", InternshipEditData.from(dataSource));
            return "edit-Internship-profile";
        }
    }


    @GetMapping("/internship-details")
    public String showInternshipDetails(@RequestParam Long id, Model model) {
        Internship internship = service.findById(id);

        if (internship == null) {
            model.addAttribute("message", "Internship not found");
            return "redirect:/internships";
        }

        model.addAttribute("internship", internship);
        model.addAttribute("data", InternshipEditData.from(internship));
        return "edit-Internship-profile"; // Reuse the edit template for viewing
    }

    private void addInternshipListModel(Model model, List<Internship> internships) {
        model.addAttribute("internships", internships.stream()
                .map(InternshipCard::from)
                .toList());
        model.addAttribute("totalInternships", internships.size());
        model.addAttribute("totalApplicants", internships.stream().mapToInt(Internship::getApplicantsCount).sum());
        model.addAttribute("activeInternships", internships.stream()
                .filter(i -> i.getEndDate() != null && i.getEndDate().isAfter(LocalDate.now()))
                .count());
    }

    private static void applyTemplateFields(Internship internship, String description) {
        if (hasText(description)) {
            internship.setRequirements(description);
        }
    }

    private static boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private static String displayField(Internship internship) {
        if (internship.getCompany() != null && hasText(internship.getCompany().getField())) {
            return internship.getCompany().getField();
        }
        if (hasText(internship.getCompanyName())) {
            return internship.getCompanyName();
        }
        if (hasText(internship.getRequirements())) {
            return internship.getRequirements();
        }
        return "";
    }

    private static String displayLocation(Internship internship) {
        if (internship.getCompany() != null && hasText(internship.getCompany().getLocation())) {
            return internship.getCompany().getLocation();
        }
        return "";
    }

    private static String displayLogo(Internship internship) {
        if (internship.getCompany() == null || !hasText(internship.getCompany().getLogo())) {
            return "logo.png";
        }

        String logo = internship.getCompany().getLogo().replace("\\", "/");
        int slashIndex = logo.lastIndexOf('/');
        return slashIndex >= 0 ? logo.substring(slashIndex + 1) : logo;
    }

    public static final class InternshipCard {
        private final Long id;
        private final String title;
        private final String field;
        private final LocalDate startDate;

        private InternshipCard(Long id, String title, String field, LocalDate startDate) {
            this.id = id;
            this.title = title;
            this.field = field;
            this.startDate = startDate;
        }

        public static InternshipCard from(Internship internship) {
            return new InternshipCard(
                    internship.getId(),
                    internship.getTitle(),
                    displayField(internship),
                    internship.getStartDate()
            );
        }

        public Long getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getField() {
            return field;
        }

        public LocalDate getStartDate() {
            return startDate;
        }
    }

    public static final class InternshipEditData {
        private final Long id;
        private final String logo;
        private final String title;
        private final String field;
        private final int duration;
        private final String description;
        private final LocalDate startDate;
        private final String location;

        private InternshipEditData(Long id,
                                   String logo,
                                   String title,
                                   String field,
                                   int duration,
                                   String description,
                                   LocalDate startDate,
                                   String location) {
            this.id = id;
            this.logo = logo;
            this.title = title;
            this.field = field;
            this.duration = duration;
            this.description = description;
            this.startDate = startDate;
            this.location = location;
        }

        public static InternshipEditData from(Internship internship) {
            return new InternshipEditData(
                    internship.getId(),
                    displayLogo(internship),
                    hasText(internship.getTitle()) ? internship.getTitle() : "New Internship",
                    displayField(internship),
                    internship.getDuration(),
                    internship.getRequirements(),
                    internship.getStartDate(),
                    displayLocation(internship)
            );
        }

        public Long getId() {
            return id;
        }

        public String getLogo() {
            return logo;
        }

        public String getTitle() {
            return title;
        }

        public String getField() {
            return field;
        }

        public int getDuration() {
            return duration;
        }

        public String getDescription() {
            return description;
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public String getLocation() {
            return location;
        }
    }
}

