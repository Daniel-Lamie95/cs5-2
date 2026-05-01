package com.example.cs5_2.repository;

import com.example.cs5_2.model.Application;
import com.example.cs5_2.model.Student;
import com.example.cs5_2.model.Internship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Integer> {

    // prevent duplicate applications (optional but useful)
    boolean existsByStudentAndInternship(Student student, Internship internship);

    // get all applications for a student
    List<Application> findByStudent(Student student);

    // get all applications for an internship
    List<Application> findByInternship(Internship internship);
}