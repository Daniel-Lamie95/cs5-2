package com.example.cs5_2.service;

import com.example.cs5_2.model.BuildCV;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.draw.LineSeparator;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;

@Service
public class CVService {
    public byte[] generatePdf(BuildCV cv) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 36, 36, 36, 36);
        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font nameFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22);
            Font sectionHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Font subHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10);

            // Header
            document.add(new Paragraph(cv.getName().toUpperCase(), nameFont));
            document.add(new Paragraph(cv.getJobTitle(), FontFactory.getFont(FontFactory.HELVETICA, 14, java.awt.Color.GRAY)));
            document.add(new Paragraph(cv.getEmail() + " | " + cv.getLocation(), normalFont));
            document.add(new Paragraph(" "));
            document.add(new LineSeparator());
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(new float[]{2, 1});
            table.setWidthPercentage(100);

            // --- LEFT: EXPERIENCE ---
            PdfPCell left = new PdfPCell();
            left.setBorder(Rectangle.NO_BORDER);
            left.addElement(new Paragraph("EXPERIENCE", sectionHeader));
            left.addElement(new Paragraph(" "));
            for (BuildCV.ExperienceEntry exp : cv.getExperiences()) {
                if (exp.getTitle() != null && !exp.getTitle().isEmpty()) {
                    left.addElement(new Paragraph(exp.getTitle(), subHeader));
                    left.addElement(new Paragraph(exp.getOrganization(), normalFont));
                    left.addElement(new Paragraph(exp.getDescription(), FontFactory.getFont(FontFactory.HELVETICA, 9)));
                    left.addElement(new Paragraph(" "));
                }
            }
            table.addCell(left);

            // --- RIGHT: EDUCATION & SKILLS ---
            PdfPCell right = new PdfPCell();
            right.setBorder(Rectangle.NO_BORDER);
            right.setPaddingLeft(15);
            right.addElement(new Paragraph("EDUCATION", sectionHeader));
            right.addElement(new Paragraph(" "));
            
            for (BuildCV.EducationEntry edu : cv.getEducationList()) {
              
                if (edu.getDetail() != null && !edu.getDetail().isEmpty()) {
                    right.addElement(new Paragraph(edu.getDegree(), subHeader));
                    right.addElement(new Paragraph(edu.getDetail(), normalFont));
                    right.addElement(new Paragraph(" "));
                }
            }

            right.addElement(new Paragraph("SKILLS", sectionHeader));
            right.addElement(new Paragraph(cv.getSkills(), normalFont));
            right.addElement(new Paragraph(" "));

            right.addElement(new Paragraph("CERTIFICATIONS", sectionHeader));
            right.addElement(new Paragraph(cv.getCertifications(), normalFont));

            table.addCell(right);
            document.add(table);
            document.close();
        } catch (Exception e) { e.printStackTrace(); }
        return out.toByteArray();
    }
}