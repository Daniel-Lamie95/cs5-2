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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

            // Initialize with default Education levels
            List<BuildCV.EducationEntry> defaultEdu = new ArrayList<>();
            List<String> levels = Arrays.asList("High School", "Bachelor's Degree", "Master's Degree", "Doctorate");
            
            for (String level : levels) {
                BuildCV.EducationEntry entry = new BuildCV.EducationEntry();
                entry.setDegree(level);
                defaultEdu.add(entry);
            }
            cv.setEducationList(defaultEdu);

            // Initialize with one empty experience
            cv.setExperiences(new ArrayList<>());
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
    public ResponseEntity<byte[]> downloadPdf(@ModelAttribute("userCV") BuildCV cv, HttpSession session) {
        Student student = (Student) session.getAttribute("user");
        if (student == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        cv.setStudent(student);
        cvRepository.save(cv);

        byte[] pdfBytes = cvService.generatePdf(cv);
        String filename = (cv.getName() != null && !cv.getName().isEmpty()) 
                          ? cv.getName().replace(" ", "_") + "_CV.pdf" 
                          : "CV.pdf";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment().filename(filename).build());

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}