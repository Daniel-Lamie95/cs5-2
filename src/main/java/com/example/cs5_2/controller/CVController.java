package com.example.cs5_2.controller;

import com.example.cs5_2.model.BuildCV;
import com.example.cs5_2.service.CVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@SessionAttributes("userCV")
public class CVController {

    @Autowired
    private CVService cvService;

   
    @ModelAttribute("userCV")
    public BuildCV userCV() {
        BuildCV cv = new BuildCV();
        
        // Beginners usually have 2-3 main things to list (Clubs, Volunteering, Projects)
        cv.getExperiences().add(new BuildCV.ExperienceEntry());
        cv.getExperiences().add(new BuildCV.ExperienceEntry());
        cv.getExperiences().add(new BuildCV.ExperienceEntry());
        
        return cv;
    }

 
    @GetMapping("/build")
    public String showForm() {
        return "cv"; 
    }

    
   // Handles the PDF generation and download
     
    @PostMapping("/download")
    public ResponseEntity<byte[]> downloadPdf(@ModelAttribute("userCV") BuildCV cv) {
      
        byte[] pdfBytes = cvService.generatePdf(cv);

        // Standard headers to tell the browser this is a downloadable PDF file
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename("My_Professional_CV.pdf")
                .build());

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}