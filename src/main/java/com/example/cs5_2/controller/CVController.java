package com.example.cs5_2.controller;

import com.example.cs5_2.model.BuildCV;
import com.example.cs5_2.model.Student;
import com.example.cs5_2.repository.BuildCVRepository;
import com.example.cs5_2.service.CVService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
        if (student == null) return new BuildCV();

        BuildCV cv = cvRepository.findByStudent(student);
        if (cv == null) {
            cv = new BuildCV();
            cv.setStudent(student);
            cv.setName(student.getName());
            cv.setEmail(student.getEmail());

            List<BuildCV.EducationEntry> defaultEdu = new ArrayList<>();
            List<String> levels = Arrays.asList("High School", "Bachelor's Degree", "Master's Degree", "Doctorate");
            for (String level : levels) {
                BuildCV.EducationEntry entry = new BuildCV.EducationEntry();
                entry.setDegree(level);
                defaultEdu.add(entry);
            }
            cv.setEducationList(defaultEdu);
            cv.setExperiences(new ArrayList<>(List.of(new BuildCV.ExperienceEntry())));
        }
        return cv;
    }

    @GetMapping("/build")
    public String showForm(HttpSession session) {
        if (session.getAttribute("user") == null) return "redirect:/login";
        return "cv";
    }

    @PostMapping("/save")
    public String saveCV(@ModelAttribute("userCV") BuildCV formData, 
                         HttpSession session, 
                         org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        
        Student student = (Student) session.getAttribute("user");
        if (student == null) return "redirect:/login";

        // validation
        try {
            com.example.cs5_2.allvalidations.CVValidation.validate(formData);
        } catch (IllegalArgumentException e) {
            // send specific error message back to the UI
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            //keep the data the user already typed so they don't have to restart
            redirectAttributes.addFlashAttribute("userCV", formData);
            return "redirect:/build";
        }

        
        BuildCV existingCV = cvRepository.findByStudent(student);

        if (existingCV != null) {
            // Update existing managed entity
            existingCV.setName(formData.getName());
            existingCV.setJobTitle(formData.getJobTitle());
            existingCV.setEmail(formData.getEmail());
            existingCV.setLocation(formData.getLocation());
            existingCV.setSkills(formData.getSkills());
            existingCV.setCertifications(formData.getCertifications());
            
            // Re-sync collections (clearing and adding prevents database ID conflicts)
            existingCV.getEducationList().clear();
            existingCV.getEducationList().addAll(formData.getEducationList());
            existingCV.getExperiences().clear();
            existingCV.getExperiences().addAll(formData.getExperiences());

            cvRepository.save(existingCV);
        } else {
            // Create new record
            formData.setStudent(student);
            cvRepository.save(formData);
        }

        return "redirect:/view-cv";
    }

    @GetMapping("/view-cv")
    public String viewCV(HttpSession session, Model model) {
        Student student = (Student) session.getAttribute("user");
        if (student == null) return "redirect:/login";

        BuildCV cv = cvRepository.findByStudent(student);
        if (cv == null) return "redirect:/build";

        model.addAttribute("cv", cv);
        return "cv-view";
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadPdf(HttpSession session) {
        Student student = (Student) session.getAttribute("user");
        if (student == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        BuildCV cv = cvRepository.findByStudent(student);
        byte[] pdfBytes = cvService.generatePdf(cv);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment().filename("CV.pdf").build());
        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}