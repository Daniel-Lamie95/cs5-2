package com.example.cs5_2.service;

import com.example.cs5_2.model.Company;
import com.example.cs5_2.repository.CompanyRepository;
import com.example.cs5_2.validation.Validation;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company register(Company company) {

       Validation.validateRegister(company);

        if (companyRepository.existsByEmail(company.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        return companyRepository.save(company);
    }

    public Company login(String email, String password) {

        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }

        Company company = companyRepository.findByEmail(email);

        if (company == null || !company.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        return company;
    }

    public Company getById(Long id) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid company id");
        }

        return companyRepository.findById(id).orElse(null);
    }

    public Company updateProfile(Long id, Company updatedCompany) {

       Validation.validateUpdate(updatedCompany);

        Company company = getById(id);

        if (company == null) {
            throw new IllegalArgumentException("Company not found");
        }

        company.setName(updatedCompany.getName());
        company.setField(updatedCompany.getField());
        company.setLocation(updatedCompany.getLocation());
        company.setWebsite(updatedCompany.getWebsite());
        company.setDescription(updatedCompany.getDescription());

        return companyRepository.save(company);
    }
}