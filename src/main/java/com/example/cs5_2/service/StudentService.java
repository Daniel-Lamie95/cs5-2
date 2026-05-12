package com.example.cs5_2.service;


import com.example.cs5_2.DTO.StudentRegisterDTO;
import com.example.cs5_2.model.Student;
import com.example.cs5_2.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepo;

    public StudentService(StudentRepository studentRepo) {

        this.studentRepo = studentRepo;
        //private final BuildCVRepository buildCVRepo;
    }


    public Student registerStudent(StudentRegisterDTO data) {
        if (data == null)
            throw new IllegalArgumentException("Student cannot be null");

        Student student = new Student();
        student.loadFormData(data);
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

