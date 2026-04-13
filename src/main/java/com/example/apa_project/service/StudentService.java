package com.example.apa_project.service;

import com.example.apa_project.model.StudentProfile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class StudentService {
    private final List<StudentProfile> students = new ArrayList<>();

    public StudentService() {
        students.add(new StudentProfile("Demo Student", "demo@student.com", "Computer Science"));
    }

    public List<StudentProfile> getAllStudents() {
        return Collections.unmodifiableList(students);
    }

    public void addStudent(StudentProfile studentProfile) {
        if (studentProfile == null) {
            throw new IllegalArgumentException("Student profile cannot be null");
        }
        students.add(studentProfile);
    }
}

