package com.example.cs5_2.service;

import com.example.cs5_2.model.BuildCV;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import com.lowagie.text.pdf.draw.LineSeparator;

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

            // Header Section
            document.add(new Paragraph(cv.getName().toUpperCase(), nameFont));
            document.add(new Paragraph(cv.getJobTitle(), FontFactory.getFont(FontFactory.HELVETICA, 14, java.awt.Color.GRAY)));
            document.add(new Paragraph(cv.getEmail() + " | " + cv.getLocation()));
            document.add(new Paragraph(" "));
            document.add(new LineSeparator());
            document.add(new Paragraph(" "));

            // Main Table for 2-column layout
            PdfPTable table = new PdfPTable(new float[]{2, 1}); 
            table.setWidthPercentage(100);

            // --- LEFT COLUMN (Experience) ---
            PdfPCell leftCell = new PdfPCell();
            leftCell.setBorder(Rectangle.NO_BORDER);
            leftCell.addElement(new Paragraph("EXPERIENCE", sectionHeader));
            leftCell.addElement(new Paragraph(" "));

            for (BuildCV.ExperienceEntry exp : cv.getExperiences()) {
                if (exp.getTitle() != null && !exp.getTitle().isEmpty()) {
                    leftCell.addElement(new Paragraph(exp.getTitle(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11)));
                    leftCell.addElement(new Paragraph(exp.getOrganization(), FontFactory.getFont(FontFactory.HELVETICA, 10)));
                    leftCell.addElement(new Paragraph(exp.getDescription(), FontFactory.getFont(FontFactory.HELVETICA, 9)));
                    leftCell.addElement(new Paragraph(" "));
                }
            }
            table.addCell(leftCell);

           
            PdfPCell rightCell = new PdfPCell();
            rightCell.setBorder(Rectangle.NO_BORDER);
            rightCell.setPaddingLeft(15);
            
            rightCell.addElement(new Paragraph("EDUCATION", sectionHeader));
            rightCell.addElement(new Paragraph(cv.getEducation(), FontFactory.getFont(FontFactory.HELVETICA, 10)));
            rightCell.addElement(new Paragraph(" "));

            rightCell.addElement(new Paragraph("SKILLS", sectionHeader));
            rightCell.addElement(new Paragraph(cv.getSkills(), FontFactory.getFont(FontFactory.HELVETICA, 10)));
            rightCell.addElement(new Paragraph(" "));

            rightCell.addElement(new Paragraph("CERTIFICATIONS", sectionHeader));
            rightCell.addElement(new Paragraph(cv.getCertifications(), FontFactory.getFont(FontFactory.HELVETICA, 10)));

            table.addCell(rightCell);
            document.add(table);
            document.close();
        } catch (Exception e) { e.printStackTrace(); }
        return out.toByteArray();
    }
}