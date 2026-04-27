package com.example.cs5_2.controller;

import com.example.cs5_2.model.BuildCV;
import com.example.cs5_2.model.Student;
import com.example.cs5_2.repository.BuildCVRepository;
import com.example.cs5_2.service.CVService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@SessionAttributes("userCV")
public class CVController {

    @Autowired
    private CVService cvService;

    @Autowired
    private BuildCVRepository cvRepository;

    @ModelAttribute("userCV")
    public BuildCV userCV(HttpSession session) {
        Student student = (Student) session.getAttribute("user");
        BuildCV cv = null;

        if (student != null) {
            cv = cvRepository.findByStudent(student);
        }

        if (cv == null) {
            cv = new BuildCV();
            if (student != null) {
                cv.setStudent(student); 
                cv.setName(student.getName());
                cv.setEmail(student.getEmail());
            }
            cv.getExperiences().add(new BuildCV.ExperienceEntry());
            cv.getExperiences().add(new BuildCV.ExperienceEntry());
        }
        return cv;
    }

    @GetMapping("/build")
    public String showForm(HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        return "cv";
    }

    @PostMapping("/download")
    public ResponseEntity<byte[]> downloadPdf(
            @ModelAttribute("userCV") BuildCV cv, 
            @RequestParam(value = "eduYears", required = false) String[] eduYears,
            HttpSession session) {
        
        Student student = (Student) session.getAttribute("user");
        if (student == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (eduYears != null) {
            for (int i = 0; i < eduYears.length; i++) {
                if (i < cv.getEducationList().size() && !eduYears[i].isEmpty()) {
                    String schoolName = cv.getEducationList().get(i).getDetail();
                    if (schoolName != null && !schoolName.isEmpty()) {
                        cv.getEducationList().get(i).setDetail(schoolName + " (" + eduYears[i] + ")");
                    }
                }
            }
        }

        cv.setStudent(student);
        cvRepository.save(cv); 

        byte[] pdfBytes = cvService.generatePdf(cv);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename(cv.getName().replace(" ", "_") + "_CV.pdf")
                .build());

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}