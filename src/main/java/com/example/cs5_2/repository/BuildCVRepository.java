package com.example.cs5_2.repository;

import com.example.cs5_2.model.BuildCV;
import com.example.cs5_2.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuildCVRepository extends JpaRepository<BuildCV, Long> {
    BuildCV findByStudent(Student student);
}