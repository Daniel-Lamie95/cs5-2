package com.example.cs5_2.service;

import com.example.cs5_2.model.Internship;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InternshipService {

    private final List<Internship> internships = new ArrayList<>();

    public String addInternship(Internship internship) {

        if (internship.getTitle() == null || internship.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Title is required");
        }

        if (internship.getCompanyName() == null || internship.getCompanyName().isEmpty()) {
            throw new IllegalArgumentException("Company name is required");
        }

        internship.setApplicantsCount(0);
        internship.setMaxApplicants(5);

        internships.add(internship);

        return "Internship added successfully!";
    }


    public List<Internship> getAllInternships() {
        return internships;
    }


    public Internship findByTitle(String title) {
        for (Internship i : internships) {
            if (i.getTitle() != null && i.getTitle().equalsIgnoreCase(title)) {
                return i;
            }
        }
        return null;
    }

    public String deleteInternship(String title) {
        Internship i = findByTitle(title);

        if (i == null) {
            throw new IllegalArgumentException("Internship not found");
        }

        internships.remove(i);
        return "Internship deleted successfully!";
    }

    public String updateInternship(String title, Internship updated) {
        Internship existing = findByTitle(title);

        if (existing == null) {
            throw new IllegalArgumentException("Internship not found");
        }

        if (updated.getTitle() != null && !updated.getTitle().isEmpty()) {
            existing.setTitle(updated.getTitle());
        }

        if (updated.getCompanyName() != null && !updated.getCompanyName().isEmpty()) {
            existing.setCompanyName(updated.getCompanyName());
        }

        return "Internship updated successfully!";
    }

    public String applyToInternship(String title) {

        Internship internship = findByTitle(title);

        if (internship == null) {
            throw new IllegalArgumentException("Internship not found");
        }


        if (internship.getApplicantsCount() >= internship.getMaxApplicants()) {
            throw new IllegalArgumentException("Application limit reached!");
        }
        internship.setApplicantsCount(internship.getApplicantsCount() + 1);
        return "Application successful!";
    }

}

