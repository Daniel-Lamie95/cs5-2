package com.example.cs5_2.repository;

import com.example.cs5_2.model.Internship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InternshipRepository
        extends JpaRepository<Internship, Long> {


    List<Internship>
    findByCompanyNameIgnoreCase(
            String companyName
    );


    List<Internship>
    findByTitleContainingIgnoreCase(
            String title
    );

}