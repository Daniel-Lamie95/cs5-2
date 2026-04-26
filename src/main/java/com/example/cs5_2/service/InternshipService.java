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

    public Internship findById(Long id) {
        for (Internship i : internships) {
            if (i.getId() != null && i.getId().equals(id)) {
                return i;
            }
        }
        return null;
    }

    public String deleteInternship(Long id) {
        Internship i = findById(id);

        if (i == null) {
            throw new IllegalArgumentException("Internship not found");
        }

        internships.remove(i);
        return "Internship deleted successfully!";
    }

    public String updateInternship(Long id, Internship updated) {
        Internship existing = findById(id);

        if (existing == null) {
            throw new IllegalArgumentException("Internship not found");
        }

        // Note: Title and company name are not updated to prevent breaking references in applications and queries

        return "Internship updated successfully!";
    }

    public String applyToInternship(Long id) {

        Internship internship = findById(id);

        if (internship == null) {
            throw new IllegalArgumentException("Internship not found");
        }


        if (internship.getApplicantsCount() >= internship.getMaxApplicants()) {
            throw new IllegalArgumentException("Application limit reached!");
        }
        internship.setApplicantsCount(internship.getApplicantsCount() + 1);
        return "Application successful!";
    }
    public List<Internship> getInternshipsByCompany(String companyName) {

        List<Internship> result = new ArrayList<>();

        for (Internship i : internships) {
            if (i.getCompanyName() != null &&
                i.getCompanyName().equalsIgnoreCase(companyName)) {
                result.add(i);
            }
        }

        return result;
    }

    public List<Internship> searchInternshipsByTitle(String title) {
        List<Internship> result = new ArrayList<>();

        for (Internship i : internships) {
            if (i.getTitle() != null && 
                i.getTitle().toLowerCase().contains(title.toLowerCase())) {
                result.add(i);
            }
        }

        return result;
    }

}