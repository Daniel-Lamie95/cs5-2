package com.example.cs5_2.service;
import com.example.cs5_2.model.SimpleRanking;
import com.example.cs5_2.model.Student;
import com.example.cs5_2.model.Company;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RankingService {

    public List<SimpleRanking> getUniversityRanking(List<Student> students) {

        List<SimpleRanking> result = new ArrayList<>();

        List<String> checkedUniversities = new ArrayList<>();

        for (Student s : students) {

            String uni = s.getUniversity();

            // If the university has already been considered, we'll skip it.
            if (checkedUniversities.contains(uni)) {
                continue;
            }

            double sum = 0;
            int count = 0;

            // We gather all students from the same university
            for (Student s2 : students) {

                if (s2.getUniversity() != null &&
                        s2.getUniversity().equals(uni)) {

                    sum += s2.getPerformanceScore();
                    count++;
                }
            }

            double avg = 0;

            if (count != 0) {
                avg = sum / count;
            }

            result.add(new SimpleRanking(uni, avg));
            checkedUniversities.add(uni);
        }

        return result;
    }

    public List<SimpleRanking> getCompanyRanking(List<Company> companies) {

        List<SimpleRanking> result = new ArrayList<>();

        for (Company c : companies) {

            result.add(new SimpleRanking(
                    c.getName(),
                    c.getRating()
            ));
        }

        return result;
    }
}