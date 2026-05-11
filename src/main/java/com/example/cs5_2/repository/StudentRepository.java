package com.example.cs5_2.repository;

import com.example.cs5_2.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query("SELECT s FROM Student s WHERE LOWER(TRIM(s.email)) = LOWER(TRIM(:email))")
    Student findByEmail(@Param("email") String email);
}
