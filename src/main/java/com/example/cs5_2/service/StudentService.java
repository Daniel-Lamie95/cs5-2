package com.example.cs5_2.service;

import com.example.cs5_2.model.Student;
import com.example.cs5_2.repository.StudentDB;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

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
            try{
                StudentDB studentDB = new StudentDB();
                studentDB.insertStudent(student);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return student;
    }

    public List<Student> getAllStudents(){
        try {
            StudentDB studentDB = new StudentDB();
            return studentDB.viewStudents();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Student getStudentByEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        try {
            StudentDB studentDB = new StudentDB();
            return studentDB.getStudentByEmail(email);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Student updateStudent(String email, Student updated) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (updated == null) {
            throw new IllegalArgumentException("Updated student cannot be null");
        }
        try {
            StudentDB studentDB = new StudentDB();
            return studentDB.updateStudent(email, updated);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteStudent(String email) {
        StudentDB studentDB = new StudentDB();
        studentDB.deleteStudent(email);

    }


    public void uploadProfilePhoto(int studentId, String contentType, byte[] photoBytes) {
        if (photoBytes == null || photoBytes.length == 0) {
            throw new IllegalArgumentException("Profile photo cannot be empty");
        }
        StudentDB studentDB = new StudentDB();
        studentDB.uploadProfilePhoto(studentId, contentType, photoBytes);
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
