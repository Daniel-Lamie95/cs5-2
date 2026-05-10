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
public class CVController {

    @Autowired
    private CVService cvService;

    @Autowired
    private BuildCVRepository cvRepository;

    @ModelAttribute("userCV")
    public BuildCV userCV(HttpSession session) {

        Student student = (Student) session.getAttribute("user");

        if (student != null) {

            BuildCV existingCV = cvRepository.findByStudent(student);

            if (existingCV != null) {
                return existingCV;
            }
        }

        BuildCV cv = new BuildCV();

        if (student != null) {
            cv.setStudent(student);
            cv.setName(student.getName());
            cv.setEmail(student.getEmail());
        }

        return cv;
    }

    @GetMapping("/build")
    public String showForm(HttpSession session) {

      //  if (session.getAttribute("user") == null) {
       //     return "redirect:/login";
      //  }

        return "cv";
    }

    @PostMapping("/download")
    public ResponseEntity<byte[]> downloadPdf(
            @ModelAttribute("userCV") BuildCV cv,
            HttpSession session) {

        System.out.println("DOWNLOAD HIT");

        Student student = (Student) session.getAttribute("user");

        if (student == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        cv.setStudent(student);

        cvRepository.save(cv);

        byte[] pdfBytes = cvService.generatePdf(cv);

        String filename = "CV.pdf";

        if (cv.getName() != null && !cv.getName().isEmpty()) {
            filename = cv.getName().replace(" ", "_") + "_CV.pdf";
        }

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_PDF);

        headers.setContentDisposition(
                ContentDisposition.attachment()
                        .filename(filename)
                        .build()
        );

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}