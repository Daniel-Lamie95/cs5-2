package com.example.cs5_2.service;
import java.time.LocalDate;
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


    public void addInternship(Internship internship) {

        if (internship == null) {
            throw new IllegalArgumentException("Internship is required");
        }

        if (isBlank(internship.getTitle())) {
            throw new IllegalArgumentException("Title is required");
        }

        if (isBlank(internship.getCompanyName())) {
            throw new IllegalArgumentException("Company name is required");
        }

        if (internship.getDuration() <= 0) {
            throw new IllegalArgumentException("Duration must be greater than zero");
        }

        internship.setApplicantsCount(0);
        internship.setMaxApplicants(5);

        repository.save(internship);
    }


    public List<Internship> getAllInternships() {
        return repository.findAll();
    }



    public Internship findById(Long id) {
        return repository.findById(id).orElse(null);
    }



    @SuppressWarnings("unused")
    public Internship updateInternship(Long id, Internship updated) {

        if (id == null) {
            throw new IllegalArgumentException("ID is required");
        }

        if (updated == null) {
            throw new IllegalArgumentException("Internship is required");
        }

        // Required fields (similar to how student edits are validated)
        if (isBlank(updated.getTitle())) {
            throw new IllegalArgumentException("Title is required");
        }

        if (isBlank(updated.getCompanyName())) {
            throw new IllegalArgumentException("Company name is required");
        }

        if (updated.getDuration() <= 0) {
            throw new IllegalArgumentException("Duration must be greater than zero");
        }

        Internship existing = findById(id);

        if (existing == null) {
            throw new IllegalArgumentException("Internship not found");
        }

        // Update allowed fields (mirror student update behavior: assign provided values)
        existing.setTitle(updated.getTitle().trim());
        existing.setCompanyName(updated.getCompanyName().trim());
        existing.setStartDate(updated.getStartDate());
        existing.setEndDate(updated.getEndDate());
        existing.setDuration(updated.getDuration());
        existing.setDescription(updated.getDescription());
        existing.setPhotoPath(updated.getPhotoPath());
        existing.setMaxApplicants(updated.getMaxApplicants());
        existing.setField(updated.getField());
        existing.setLocation(updated.getLocation());

        return repository.save(existing);
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
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