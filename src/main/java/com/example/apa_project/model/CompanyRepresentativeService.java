package com.example.apa_project;

import com.example.apa_project.CompanyRepresentative;

import java.util.List;

public class CompanyRepresentativeService {
    private CompanyRepresentative CompanyRepresentative;

    //create
    public CompanyRepresentative save(CompanyRepresentative rep) {
        if (rep == null) {
            throw new IllegalArgumentException("Representative cannot be null");
        }

        return rep;
    }


    public CompanyRepresentative find(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid ID");
        }

        return null;
    }

  
    public CompanyRepresentative update(Long id, CompanyRepresentative updatedRep) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid ID");
        }

        if (updatedRep == null) {
            throw new IllegalArgumentException("Updated data cannot be null");
        }

        return updatedRep;
    }

   
    public List<CompanyRepresentative> findAll() {
        return null;
    }

    public void delete(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid ID");
        }
    }
}