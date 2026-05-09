package com.example.cs5_2.service;

import com.example.cs5_2.allvalidations.StudentValidation;
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
        // Validate student using StudentValidation
        StudentValidation.validateRegister(student);

        // Trim email before saving to database
        student.setEmail(student.getEmail().trim());
        studentRepo.save(student);
        return student;
    }
   

    public List<Student> getAllStudents() {
        return studentRepo.findAll();
    }

    public Student getStudentByEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        // Trim whitespace from email input
        email = email.trim();
        return studentRepo.findByEmail(email);
    }

    public Student updateStudent(String email, Student updated) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        // Trim whitespace from email input
        email = email.trim();

        // Validate updated student using StudentValidation
        StudentValidation.validateUpdate(updated);

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
        // Trim whitespace from email input
        email = email.trim();
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
        // Trim whitespace from email input
        email = email.trim();
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

