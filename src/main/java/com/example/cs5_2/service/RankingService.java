package com.example.cs5_2.service;
import com.example.cs5_2.model.Student;
import com.example.cs5_2.model.Company;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RankingService {

    public Map<String, Double> getUniversityRanking(List<Student> students) {

        Map<String, List<Double>> map = new HashMap<>();

        for (Student s : students) {
            map.computeIfAbsent(s.getUniversity(), k -> new ArrayList<>())
               .add(s.getPerformanceScore());
        }

        Map<String, Double> result = new HashMap<>();

        for (String uni : map.keySet()) {

            double avg = map.get(uni)
                    .stream()
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(0.0);

            result.put(uni, avg);
        }

        return result;
    }


    public Map<String, Double> getCompanyRanking(List<Company> companies) {

        Map<String, Double> result = new HashMap<>();

        for (Company c : companies) {
            result.put(c.getName(), c.getRating());
        }

        return result;
    }
}


