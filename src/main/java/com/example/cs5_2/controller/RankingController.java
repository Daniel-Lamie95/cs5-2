package com.example.cs5_2.controller;

import com.example.cs5_2.service.RankingService;
import com.example.cs5_2.service.StudentService;
import com.example.cs5_2.service.CompanyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ranking")
public class RankingController {

    private final RankingService rankingService;
    private final StudentService studentService;
    private final CompanyService companyService;

    public RankingController(RankingService rankingService,
                             StudentService studentService,
                             CompanyService companyService) {
        this.rankingService = rankingService;
        this.studentService = studentService;
        this.companyService = companyService;
    }

    @GetMapping("/universities")
    public String universities(Model model) {

        model.addAttribute(
                "ranking",
                rankingService.getUniversityRanking(
                        studentService.getAllStudents()
                )
        );

        return "university-ranking";
    }

    @GetMapping("/companies")
    public String companies(Model model) {

        model.addAttribute(
                "ranking",
                rankingService.getCompanyRanking(
                        companyService.getAllCompanies()
                )
        );

        return "company-ranking";
    }
}