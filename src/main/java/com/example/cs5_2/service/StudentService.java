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
        return null;
    }

    public void deleteStudent(String email) {

    }

    public void uploadCv(int studentId, String fileName, byte[] cvBytes) {

    }

}
