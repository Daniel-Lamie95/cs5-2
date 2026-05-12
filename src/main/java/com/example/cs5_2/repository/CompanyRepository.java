package com.example.cs5_2.repository;

import com.example.cs5_2.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
   @Query("SELECT c FROM Company c WHERE c.email = :email")
   Company findByEmail(@Param("email") String email);

    boolean existsByEmail(String email);
    
    boolean existsByName(String name );
}