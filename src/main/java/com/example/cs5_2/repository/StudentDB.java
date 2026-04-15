package com.example.cs5_2.repository;


import com.example.cs5_2.model.Student;

import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;

public class StudentDB implements Serializable {
    private String constr = "null";

    public StudentDB(){
        loadDriver();
        createStudentsTable();
    }

    private void loadDriver() {
        try {
            // load driver
            Class.forName("null");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void createStudentsTable() {
        String createTable = "CREATE TABLE IF NOT EXISTS students (" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "name VARCHAR(255) NOT NULL," +
                "email VARCHAR(255) NOT NULL UNIQUE," +
                "password VARCHAR(255) NOT NULL," +
                "major VARCHAR(255) NOT NULL," +
                "university VARCHAR(255) NOT NULL," +
                "date_of_birth DATE NOT NULL," +
                "cv_file_name VARCHAR(255)," +
                "cv_document BLOB" +
                ")";

        try (Connection con = DriverManager.getConnection(constr);
             Statement stmt = con.createStatement()) {
            stmt.executeUpdate(createTable);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int insertStudent(Student s){
        Connection con = null;

        try {
            con = DriverManager.getConnection(constr);
            String insertstudent = "INSERT INTO students (name, email, password, major, " +
                    "date_of_birth, cv_file_name, cv_document) VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement pstmt = con.prepareStatement(insertstudent, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, s.getName());
            pstmt.setString(2, s.getEmail());
            pstmt.setString(3, s.getPassword());
            pstmt.setString(4, s.getMajor());
            pstmt.setDate(5, new java.sql.Date(s.getDateOfBirth().getTime()));
            pstmt.setString(6, s.getCvFileName());
            pstmt.setBytes(7, s.getCvDocument());

            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    s.setId(generatedId);
                    return generatedId;
                }
            }

            throw new RuntimeException("Insert succeeded but no generated id was returned");

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    public ArrayList<Student> viewStudents(){
        Connection con = null;
        ArrayList<Student> students = new ArrayList<>();
        try{
            con = DriverManager.getConnection(constr);

            String selectStudents = "SELECT * FROM students";

            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(selectStudents);

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String password = rs.getString("password");
                String major = rs.getString("major");
                String uni = rs.getString("university");
                Date dateOfBirth = rs.getDate("date_of_birth");

                Student s = new Student(id, name, email, password, major,uni, dateOfBirth);
                students.add(s);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            if (con != null){
                try{
                    con.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return students;
    }

    public void addOrUpdateStudentCv(int studentId, String cvFileName, byte[] cvDocument) {
        if (cvDocument == null || cvDocument.length == 0) {
            throw new IllegalArgumentException("CV document cannot be empty");
        }

        String updateCv = "UPDATE students SET cv_file_name = ?, cv_document = ? WHERE id = ?";

        try (Connection con = DriverManager.getConnection(constr);
             PreparedStatement pstmt = con.prepareStatement(updateCv)) {

            pstmt.setString(1, cvFileName);
            pstmt.setBytes(2, cvDocument);
            pstmt.setInt(3, studentId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new IllegalArgumentException("No student found with ID: " + studentId);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Student getStudentByEmail(String email){
        Student student = null;
        Connection con = null;
        try{
            con =DriverManager.getConnection(constr);
            String selectStudents = "SELECT * FROM students WHERE email = ?";
            PreparedStatement pstmt = con.prepareStatement(selectStudents);
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String password = rs.getString("password");
                String major = rs.getString("major");
                String uni = rs.getString("university");
                Date dateOfBirth = rs.getDate("date_of_birth");

                student = new Student(id,name,email,password,major,uni,dateOfBirth);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            if (con != null){
                try {
                    con.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return student;
    }

}
