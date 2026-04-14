package com.example.apa_project.service;

import com.example.apa_project.model.Company;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CompanyService {

    public Company save(Company company) {
        if (company == null) {
            throw new IllegalArgumentException("Company cannot be null");
        }
        return company;
    }

    public Company findById(int id) {
        if ( id <= 0) {
            throw new IllegalArgumentException("Invalid company id");
        }
        return null;
    }

    public Company update(int id, Company updatedCompany) {
        if ( id <= 0) {
            throw new IllegalArgumentException("Invalid company id");
        }

        if (updatedCompany == null) {
            throw new IllegalArgumentException("Updated company cannot be null");
        }

        return updatedCompany;
    }

    public List<Company> findAll() {
        return null;
    }

    public void deleteById(int id) {
        if ( id <= 0) {
            throw new IllegalArgumentException("Invalid company id");
        }
    }
}