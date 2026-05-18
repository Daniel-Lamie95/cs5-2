package com.example.cs5_2.service;

import com.example.cs5_2.model.SimpleRanking;
import com.example.cs5_2.model.Student;
import com.example.cs5_2.model.Company;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RankingService {

    public List<SimpleRanking> getUniversityRanking(List<Student> students) {
     
        return students.stream()
                .filter(s -> s.getUniversity() != null) // Avoid null errors
                .collect(Collectors.groupingBy(
                        Student::getUniversity,
                        Collectors.averagingDouble(Student::getPerformanceScore)
                ))
                .entrySet().stream()
                .map(entry -> new SimpleRanking(entry.getKey(), entry.getValue()))
                .sorted((r1, r2) -> Double.compare(r2.getValue(), r1.getValue())) // Sort highest to lowest
                .collect(Collectors.toList());
    }

    public List<SimpleRanking> getCompanyRanking(List<Company> companies) {
        
        return companies.stream()
                .map(c -> new SimpleRanking(c.getName(), c.getRating()))
                .sorted((r1, r2) -> Double.compare(r2.getValue(), r1.getValue())) // Sort highest to lowest
                .collect(Collectors.toList());
    }
}