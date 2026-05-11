package com.example.cs5_2.service;


import com.example.cs5_2.model.Application;
import com.example.cs5_2.model.Internship;
import com.example.cs5_2.model.Student;
import com.example.cs5_2.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepo;

    public StudentService(StudentRepository studentRepo) {
        this.studentRepo = studentRepo;
    }
    //private final BuildCVRepository buildCVRepo;

    public Student registerStudent(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        } else if (student.getEmail() == null || student.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        } else if (student.getPassword() == null || student.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        } else if (student.getPassword().length() < 8) {
            throw new IllegalArgumentException("Password length must be at least 8 characters");
        } else if (student.getName() == null || student.getName().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        } else if (student.getName().length() < 3) {
            throw new IllegalArgumentException("Name must be at least 3 chars");
        } else if (student.getMajor() == null || student.getMajor().isEmpty()) {
            throw new IllegalArgumentException("Major is required");
        } else if (student.getUniversity() == null || student.getUniversity().isEmpty()) {
            throw new IllegalArgumentException("University is required");
        } else {
            // Normalize email before saving to database
            student.setEmail(student.getEmail().trim().toLowerCase());
            studentRepo.save(student);
            return student;
        }
    }
   

    public List<Student> getAllStudents() {
        return studentRepo.findAll();
    }

    public Student getStudentByEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        // Normalize email input for lookup
        email = email.trim().toLowerCase();
        return studentRepo.findByEmail(email);
    }

    public Student updateStudent(String email, Student updated) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }

        email = email.trim().toLowerCase();

        if (updated == null) {
            throw new IllegalArgumentException("Student is required");
        }
        if (updated.getName() == null || updated.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (updated.getMajor() == null || updated.getMajor().trim().isEmpty()) {
            throw new IllegalArgumentException("Major is required");
        }
        if (updated.getUniversity() == null || updated.getUniversity().trim().isEmpty()) {
            throw new IllegalArgumentException("University is required");
        }

        Student existingStudent = studentRepo.findByEmail(email);
        if (existingStudent == null) {
            throw new IllegalArgumentException("Student with email " + email + " not found");
        }
        // Update allowed fields
        existingStudent.setName(updated.getName());
        existingStudent.setMajor(updated.getMajor());
        existingStudent.setUniversity(updated.getUniversity());
        existingStudent.setPhoneNum(updated.getPhoneNum());
        existingStudent.setLocation(updated.getLocation());
        existingStudent.setDateOfBirth(updated.getDateOfBirth());
        existingStudent.setBuildCV(updated.getBuildCV());
        existingStudent.setProfilePhotoPath(updated.getProfilePhotoPath());

        return studentRepo.save(existingStudent);
    }
   

    public void deleteStudent(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        // Normalize email input for lookup
        email = email.trim().toLowerCase();
        Student existingStudent = studentRepo.findByEmail(email);
        if (existingStudent == null) {
            throw new IllegalArgumentException("Student with email " + email + " not found");
        }
        studentRepo.delete(existingStudent);

    }

    public Student loginStudent(String email, String password) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }
        // Normalize email input for lookup
        email = email.trim().toLowerCase();
        Student student = studentRepo.findByEmail(email);
        if (student == null) {
            throw new IllegalArgumentException("Student with email " + email + " not found");
        }
        if (!student.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid password");
        }
        return student;
    }


    //public Application applyToInternship(Student student, Internship internship){}

}

