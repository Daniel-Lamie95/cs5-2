package com.example.cs5_2.service;

import com.example.cs5_2.model.Company;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {

    private final List<Company> companies = new ArrayList<>();

    public String registerCompany(Company company) {
        if (company == null) {
            throw new IllegalArgumentException("Company data is required");
        }

        if (company.getName() == null || company.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }

        if (company.getEmail() == null || company.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }

        if (company.getPassword() == null || company.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }

        if (findByEmail(company.getEmail()) != null) {
            throw new IllegalArgumentException("Account already exists");
        }


        return "Company account created successfully";
    }

    public Company loginCompany(String name, String email, String password) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }

        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }

        for (Company company : companies) {
            if (company.getName().equalsIgnoreCase(name)
                    && company.getEmail().equalsIgnoreCase(email)
                    && company.getPassword().equals(password)) {
                return company;
            }
        }

        throw new IllegalArgumentException("Invalid login data");
    }

    public Company findByEmail(String email) {
        if (email == null) {
            return null;
        }

        for (Company company : companies) {
            if (company.getEmail().equalsIgnoreCase(email)) {
                return company;
            }
        }
        return null;
    }

    public String completeCompanyProfile(String email, Company profileData) {
        Company company = findByEmail(email);

        if (company == null) {
            throw new IllegalArgumentException("Company not found");
        }

      //  validateProfileFields(profileData);

        company.setIndustry(profileData.getIndustry());
        company.setLocation(profileData.getLocation());
        company.setWebsite(profileData.getWebsite());
        company.setDescription(profileData.getDescription());

        return "Company profile completed successfully";
    }

    public String editCompanyProfile(String email, Company updatedData) {
        Company company = findByEmail(email);

        if (company == null) {
            throw new IllegalArgumentException("Company not found");
        }

        if (updatedData.getName() == null || updatedData.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }

        //validateProfileFields(updatedData);

        company.setName(updatedData.getName());
        company.setIndustry(updatedData.getIndustry());
        company.setLocation(updatedData.getLocation());
        company.setWebsite(updatedData.getWebsite());
        company.setDescription(updatedData.getDescription());

        return "Company profile updated successfully";
    }

    public List<Company> getAllCompanies() {
        return companies;
    }

    /*private void validateProfileFields(Company company) {
        if (company == null) {
            throw new IllegalArgumentException("Profile data is required");
        }

        if (company.getIndustry() == null || company.getIndustry().trim().isEmpty()) {
            throw new IllegalArgumentException("Industry is required");
        }

        if (company.getLocation() == null || company.getLocation().trim().isEmpty()) {
            throw new IllegalArgumentException("Location is required");
        }

        if (company.getWebsite() == null || company.getWebsite().trim().isEmpty()) {
            throw new IllegalArgumentException("Website is required");
        }

        if (company.getDescription() == null || company.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Description is required");
        }
    
    
   // private void validateRepresentative(Company rep) {
        if (rep == null) {
            throw new IllegalArgumentException("Representative object cannot be null");
        }

        if (rep.getName() == null || rep.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }

        if (rep.getEmail() == null || rep.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }

        if (!rep.getEmail().contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }

        if (rep.getPassword() == null || rep.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }

        if (rep.getPassword().length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }

        if (rep.getPositionTitle() == null || rep.getPositionTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Position title is required");
        }
    }*/

}
