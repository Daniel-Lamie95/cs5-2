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
        } else {
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
        } else return studentRepo.findByEmail(email);
    }

    public Student updateStudent(String email, Student updated) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (updated == null) {
            throw new IllegalArgumentException("Updated student cannot be null");
        }

        Student existingStudent = studentRepo.findByEmail(email);
        if (existingStudent == null) {
            throw new IllegalArgumentException("Student with email " + email + " not found");
        }
        // Update allowed fields
        existingStudent.setName(updated.getName());

        // Only update password if provided (not empty)
        if (updated.getPassword() != null && !updated.getPassword().isEmpty()) {
            if (updated.getPassword().length() < 8) {
                throw new IllegalArgumentException("Password length must be at least 8 characters");
            }
            existingStudent.setPassword(updated.getPassword());
        }

        existingStudent.setMajor(updated.getMajor());
        existingStudent.setUniversity(updated.getUniversity());
        existingStudent.setPhoneNum(updated.getPhoneNum());
        existingStudent.setLocation(updated.getLocation());
        existingStudent.setDateOfBirth(updated.getDateOfBirth());
        existingStudent.setBuildCV(updated.getBuildCV());

        return studentRepo.save(existingStudent);
    }

    public void deleteStudent(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
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
        Student student = studentRepo.findByEmail(email);
        if (student == null) {
            throw new IllegalArgumentException("Student with email " + email + " not found");
        }
        if (!student.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid password");
        }
        return student;
    }

    public void uploadProfilePhoto(Long studentId, String contentType, byte[] photoBytes) {
        if (photoBytes == null || photoBytes.length == 0) {
            throw new IllegalArgumentException("Profile photo cannot be empty");
        }
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Uploaded file must be an image");
        }

        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student with id " + studentId + " not found"));

        student.setProfilePhoto(contentType, photoBytes);
        studentRepo.save(student);
    }

    public byte[] getProfilePhoto(Long studentId) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student with id " + studentId + " not found"));
        return student.getProfilePhoto();
    }

    public String getProfilePhotoContentType(Long studentId) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student with id " + studentId + " not found"));
        return student.getProfilePhotoContentType();
    }

    //public Application applyToInternship(Student student, Internship internship){}

}