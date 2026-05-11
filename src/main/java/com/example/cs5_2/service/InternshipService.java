package com.example.cs5_2.service;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import com.example.cs5_2.model.Internship;
import com.example.cs5_2.repository.InternshipRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InternshipService {

    private final InternshipRepository repository;


    public InternshipService(InternshipRepository repository){
        this.repository = repository;
    }


    public String addInternship(Internship internship) {

        if (internship.getTitle() == null || internship.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Title is required");
        }

        if (internship.getCompanyName() == null || internship.getCompanyName().isEmpty()) {
            throw new IllegalArgumentException("Company name is required");
        }

        internship.setApplicantsCount(0);
        internship.setMaxApplicants(5);

        repository.save(internship);

        return "Internship added successfully!";
    }



    public List<Internship> getAllInternships() {
        return repository.findAll();
    }



    public Internship findById(Long id) {
        return repository.findById(id).orElse(null);
    }



    public String updateInternship(Long id,
                                   Internship updated) {

        Internship existing = findById(id);

        if(existing == null){
            throw new IllegalArgumentException(
                    "Internship not found");
        }

        // Update only the fields that the company can edit from UI
        if(updated.getStartDate()!=null){
            existing.setStartDate(
                    updated.getStartDate()
            );
        }

        if(updated.getEndDate()!=null){
            existing.setEndDate(
                    updated.getEndDate()
            );
        }

        if(updated.getDuration()>0){
            existing.setDuration(
                    updated.getDuration()
            );
        }

        if(updated.getRequirements()!=null &&
                !updated.getRequirements().isEmpty()){

            existing.setRequirements(
                    updated.getRequirements()
            );
        }

        // Note: applicantsCount and maxApplicants are NOT updated here
        // as they are system-managed values

        repository.save(existing);

        return "Internship updated successfully!";
    }




    public List<Internship>
    getInternshipsByCompany(String companyName){

        return repository
                .findByCompanyNameIgnoreCase(
                        companyName
                );
    }



    public List<Internship>
    searchInternshipsByTitle(String title){

        return repository.findByTitleContainingIgnoreCase(title);
    }

    public String calculateRemainingTime(LocalDate startDate) {

        if (startDate == null) {
            return "No deadline";
        }

        LocalDate today = LocalDate.now();

        if (startDate.isBefore(today)) {
            return "Closed";
        }

        long days = java.time.temporal.ChronoUnit.DAYS
                .between(today, startDate);

        long weeks = days / 7;

        long remainingDays = days % 7;

        if (weeks > 0 && remainingDays > 0) {

            return weeks + " week(s) and "
                    + remainingDays + " day(s) left";
        }

        if (weeks > 0) {

            return weeks + " week(s) left";
        }

        return remainingDays + " day(s) left";
    }
}