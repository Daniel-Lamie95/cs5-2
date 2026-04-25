package com.example.cs5_2.service;

import com.example.cs5_2.model.BuildCV;
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

    public Student registerStudent(Student student){
        if (student == null){
            throw new IllegalArgumentException("Student cannot be null");
        }else if (student.getEmail() == null || student.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }else if (student.getPassword() == null || student.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        } else if (student.getPassword().length() < 8) {
            throw new IllegalArgumentException("Password length must be at least 8 characters");
        }else{
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
        }else return studentRepo.findByEmail(email);
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
        existingStudent.setPassword(updated.getPassword());
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


    public void uploadProfilePhoto(int studentId, String contentType, byte[] photoBytes) {
        if (contentType == null || contentType.isEmpty()) {
            throw new IllegalArgumentException("Content type is required");
        }
        if (photoBytes == null || photoBytes.length == 0) {
            throw new IllegalArgumentException("Photo bytes cannot be null or empty");
        }

    }

    public byte[] getProfilePhoto(int studentId) {
        StudentDB studentDB = new StudentDB();
        return studentDB.getProfilePhoto(studentId);
    }

    public String getProfilePhotoContentType(int studentId) {
        StudentDB studentDB = new StudentDB();
        return studentDB.getProfilePhotoContentType(studentId);
    }

    public Student loginStudent(String email, String password) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }
        try {
            StudentDB studentDB = new StudentDB();
            Student student = studentDB.getStudentByEmail(email);

            if (student == null) {
                return null; // Invalid email
            }

            // Verify password matches
            if (!student.getPassword().equals(password)) {
                return null; // Invalid password
            }

            return student; // Successful login
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    //public Internship browseinternships(Internship internship){}
    //public void applytointernship(Internship internship){}
    //public void trackapplication(Internship insternship){}
}
