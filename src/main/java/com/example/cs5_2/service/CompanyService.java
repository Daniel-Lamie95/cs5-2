package com.example.cs5_2.service;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.example.cs5_2.model.Company;
import com.example.cs5_2.repository.CompanyRepository;
import com.example.cs5_2.allvalidations.CompanyValidation;
import java.util.List;


@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;

    public CompanyService(CompanyRepository companyRepository, PasswordEncoder passwordEncoder) {
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Company register(Company company) {

        CompanyValidation.validateRegister(company);

        if (companyRepository.existsByEmail(company.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        if (companyRepository.existsByName(company.getName())) {
            throw new IllegalArgumentException("Company name already exists");
        }

        company.setPassword(passwordEncoder.encode(company.getPassword()));
        return companyRepository.save(company);
    }

    public Company login(String email, String password) {
        CompanyValidation.validateLogin(email, password);
    	Company company = companyRepository.findByEmail(email);

        if (company == null ||
                !passwordEncoder.matches(password, company.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        return company;
    }

    public Company getById(Long id) {
        CompanyValidation.validateGetById(id);

        return companyRepository.findById(id).orElse(null);
    }

    public Company updateProfile(Long id, Company updatedCompany) {

        CompanyValidation.validateGetById(id);
        CompanyValidation.validateUpdate(updatedCompany);

        Company company = getById(id);

        if (company == null) {
            throw new IllegalArgumentException("Company not found");
        }

        company.setName(updatedCompany.getName());
        company.setField(updatedCompany.getField());
        company.setPhone(updatedCompany.getPhone());
        company.setLocation(updatedCompany.getLocation());
        company.setWebsite(updatedCompany.getWebsite());
        company.setLogo(updatedCompany.getLogo());
        company.setDescription(updatedCompany.getDescription());

        return companyRepository.save(company);
    }

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }
}