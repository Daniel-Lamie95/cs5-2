package com.example.cs5_2.repository;

import com.example.cs5_2.model.BuildCV;
import java.sql.*;

public class BuildCVDB {
    private String constr = "null";

    public BuildCVDB() {
        loadDriver();
        createCVTable();
    }

    private void loadDriver() {
        try {
            Class.forName("null");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void createCVTable() {
        String createTable = "CREATE TABLE IF NOT EXISTS build_cv (" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "student_id INT NOT NULL UNIQUE," +
                "name VARCHAR(255)," +
                "job_title VARCHAR(255)," +
                "email VARCHAR(255)," +
                "phone VARCHAR(30)," +
                "location VARCHAR(255)," +
                "summary TEXT," +
                "education TEXT," +
                "skills TEXT," +
                "certifications TEXT," +
                "FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE" +
                ")";

        try (Connection con = DriverManager.getConnection(constr);
             Statement stmt = con.createStatement()) {
            stmt.executeUpdate(createTable);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int insertCV(BuildCV cv) {
        try (Connection con = DriverManager.getConnection(constr)) {
            String insertCV = "INSERT INTO build_cv (student_id, name, job_title, email, phone, location, summary, education, skills, certifications) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement pstmt = con.prepareStatement(insertCV, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, cv.getStudentId());
            pstmt.setString(2, cv.getName());
            pstmt.setString(3, cv.getJobTitle());
            pstmt.setString(4, cv.getEmail());
            pstmt.setString(5, cv.getPhone());
            pstmt.setString(6, cv.getLocation());
            pstmt.setString(7, cv.getSummary());
            pstmt.setString(8, cv.getEducation());
            pstmt.setString(9, cv.getSkills());
            pstmt.setString(10, cv.getCertifications());

            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    cv.setId(generatedId);
                    return generatedId;
                }
            }
            throw new RuntimeException("Insert succeeded but no generated id was returned");
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public BuildCV getCVByStudentId(int studentId) {
        try (Connection con = DriverManager.getConnection(constr)) {
            String selectCV = "SELECT * FROM build_cv WHERE student_id = ?";
            PreparedStatement pstmt = con.prepareStatement(selectCV);
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                BuildCV cv = new BuildCV();
                cv.setId(rs.getInt("id"));
                cv.setStudentId(rs.getInt("student_id"));
                cv.setName(rs.getString("name"));
                cv.setJobTitle(rs.getString("job_title"));
                cv.setEmail(rs.getString("email"));
                cv.setPhone(rs.getString("phone"));
                cv.setLocation(rs.getString("location"));
                cv.setSummary(rs.getString("summary"));
                cv.setEducation(rs.getString("education"));
                cv.setSkills(rs.getString("skills"));
                cv.setCertifications(rs.getString("certifications"));
                return cv;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateCV(BuildCV cv) {
        try (Connection con = DriverManager.getConnection(constr)) {
            String updateCV = "UPDATE build_cv SET name = ?, job_title = ?, email = ?, phone = ?, location = ?, summary = ?, education = ?, skills = ?, certifications = ? WHERE student_id = ?";
            PreparedStatement pstmt = con.prepareStatement(updateCV);
            pstmt.setString(1, cv.getName());
            pstmt.setString(2, cv.getJobTitle());
            pstmt.setString(3, cv.getEmail());
            pstmt.setString(4, cv.getPhone());
            pstmt.setString(5, cv.getLocation());
            pstmt.setString(6, cv.getSummary());
            pstmt.setString(7, cv.getEducation());
            pstmt.setString(8, cv.getSkills());
            pstmt.setString(9, cv.getCertifications());
            pstmt.setInt(10, cv.getStudentId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteCV(int studentId) {
        try (Connection con = DriverManager.getConnection(constr)) {
            String deleteCV = "DELETE FROM build_cv WHERE student_id = ?";
            PreparedStatement pstmt = con.prepareStatement(deleteCV);
            pstmt.setInt(1, studentId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}