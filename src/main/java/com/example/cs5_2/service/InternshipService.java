package com.example.cs5_2.service;

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



    public String deleteInternship(Long id) {

        Internship i = findById(id);

        if(i == null){
            throw new IllegalArgumentException(
                    "Internship not found");
        }

        repository.delete(i);

        return "Internship deleted successfully!";
    }



    public String updateInternship(Long id,
                                   Internship updated) {

        Internship existing = findById(id);

        if(existing == null){
            throw new IllegalArgumentException(
                    "Internship not found");
        }


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

        if(updated.getMaxApplicants()>0){
            existing.setMaxApplicants(
                    updated.getMaxApplicants()
            );
        }


        repository.save(existing);

        return "Internship updated successfully!";
    }




    public String applyToInternship(Long id){

        Internship internship = findById(id);

        if(internship==null){
            throw new IllegalArgumentException(
                    "Internship not found");
        }


        if(internship.getApplicantsCount()
                >= internship.getMaxApplicants()){

            throw new IllegalArgumentException(
                    "Application limit reached!"
            );
        }

        internship.setApplicantsCount(
                internship.getApplicantsCount()+1
        );

        repository.save(internship);

        return "Application successful!";
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

}