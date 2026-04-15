package com.example.apa_project.service;

import com.example.apa_project.model.Company;
import org.springframework.stereotype.Service;  
import java.util.ArrayList;
import java.util.List;

@Service
public class CompanyService {

    private final List<Company> companies = new ArrayList<>();

    public Company addCompany(Company company) {
        validateCompany(company);
        checkDuplicateEmail(company.getEmail());
        companies.add(company);
        return company;
    }

    public List<Company> getAllCompanies() {
        return companies;
    }

    public Company getCompanyByEmail(String email) {
        for (Company company : companies) {
            if (company.getEmail().equalsIgnoreCase(email)) {
                return company;
            }
        }
        return null;
    }

    public Company updateCompany(String email, Company updatedCompany) {
        validateCompany(updatedCompany);

        Company existingCompany = getCompanyByEmail(email);
        if (existingCompany == null) {
            throw new IllegalArgumentException("Company not found.");
        }

        existingCompany.setName(updatedCompany.getName());
        existingCompany.setEmail(updatedCompany.getEmail());
        existingCompany.setPassword(updatedCompany.getPassword());
        existingCompany.setField(updatedCompany.getField());
        existingCompany.setLocation(updatedCompany.getLocation());
        existingCompany.setDescription(updatedCompany.getDescription());

        return existingCompany;
    }

    public void deleteCompany(String email) {
        Company company = getCompanyByEmail(email);
        if (company == null) {
            throw new IllegalArgumentException("Company not found.");
        }
        companies.remove(company);
    }

    private void validateCompany(Company company) {
        if (company == null) {
            throw new IllegalArgumentException("Company data cannot be null.");
        }

        if (company.getName() == null || company.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required.");
        }

        if (company.getEmail() == null || company.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required.");
        }

        if (!company.getEmail().contains("@")) {
            throw new IllegalArgumentException("Invalid email format.");
        }

        if (company.getPassword() == null || company.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required.");
        }

        if (company.getPassword().length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters.");
        }

        if (company.getField() == null || company.getField().trim().isEmpty()) {
            throw new IllegalArgumentException("Field is required.");
        }

        if (company.getLocation() == null || company.getLocation().trim().isEmpty()) {
            throw new IllegalArgumentException("Location is required.");
        }

        if (company.getDescription() == null || company.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Description is required.");
        }
    }

    private void checkDuplicateEmail(String email) {
        for (Company company : companies) {
            if (company.getEmail().equalsIgnoreCase(email)) {
                throw new IllegalArgumentException("Email already exists.");
            }
        }
    }
}