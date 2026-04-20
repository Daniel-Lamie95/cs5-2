package com.example.cs5_2.repository;


import com.example.cs5_2.model.Student;

import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;

public class StudentDB implements Serializable {
    private String constr = "null";

    public StudentDB() {
        loadDriver();
        createStudentsTable();
        ensurePhoneNumColumn();
        ensureLocationColumn();
        ensureProfilePhotoColumns();
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
                "phone_num VARCHAR(30)," +
                "location VARCHAR(255)," +
                "date_of_birth DATE NOT NULL," +
                "cv_file_name VARCHAR(255)," +
                "cv_document BLOB," +
                "profile_photo_content_type VARCHAR(100)," +
                "profile_photo BLOB" +
                ")";

        try (Connection con = DriverManager.getConnection(constr);
             Statement stmt = con.createStatement()) {
            stmt.executeUpdate(createTable);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void ensureLocationColumn() {
        try (Connection con = DriverManager.getConnection(constr)) {
            if (!hasColumn(con, "students", "location")) {
                try (Statement stmt = con.createStatement()) {
                    stmt.executeUpdate("ALTER TABLE students ADD COLUMN location VARCHAR(255)");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void ensurePhoneNumColumn() {
        try (Connection con = DriverManager.getConnection(constr)) {
            if (!hasColumn(con, "students", "phone_num")) {
                try (Statement stmt = con.createStatement()) {
                    stmt.executeUpdate("ALTER TABLE students ADD COLUMN phone_num VARCHAR(30)");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void ensureProfilePhotoColumns() {
        try (Connection con = DriverManager.getConnection(constr)) {
            if (!hasColumn(con, "students", "profile_photo_content_type")) {
                try (Statement stmt = con.createStatement()) {
                    stmt.executeUpdate("ALTER TABLE students ADD COLUMN profile_photo_content_type VARCHAR(100)");
                }
            }
            if (!hasColumn(con, "students", "profile_photo")) {
                try (Statement stmt = con.createStatement()) {
                    stmt.executeUpdate("ALTER TABLE students ADD COLUMN profile_photo BLOB");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean hasColumn(Connection con, String tableName, String columnName) throws SQLException {
        DatabaseMetaData metaData = con.getMetaData();
        try (ResultSet rs = metaData.getColumns(null, null, tableName, columnName)) {
            if (rs.next()) {
                return true;
            }
        }
        try (ResultSet rs = metaData.getColumns(null, null, tableName.toUpperCase(), columnName.toUpperCase())) {
            return rs.next();
        }
    }

    public int insertStudent(Student s) {
        Connection con = null;

        try {
            con = DriverManager.getConnection(constr);
            String insertstudent = "INSERT INTO students (name, email, password, university, major, " +
                    "phone_num, location, date_of_birth, cv_file_name, cv_document, profile_photo_content_type, profile_photo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement pstmt = con.prepareStatement(insertstudent, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, s.getName());
            pstmt.setString(2, s.getEmail());
            pstmt.setString(3, s.getPassword());
            pstmt.setString(4, s.getUniversity());
            pstmt.setString(5, s.getMajor());
            pstmt.setString(6, s.getPhoneNum());
            pstmt.setString(7, s.getLocation());
            pstmt.setDate(8, new java.sql.Date(s.getDateOfBirth().getTime()));
            pstmt.setString(9, s.getCvFileName());
            pstmt.setBytes(10, s.getCvDocument());
            pstmt.setString(11, s.getProfilePhotoContentType());
            pstmt.setBytes(12, s.getProfilePhoto());

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

    public ArrayList<Student> viewStudents() {
        Connection con = null;
        ArrayList<Student> students = new ArrayList<>();
        try {
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
                String phoneNum = rs.getString("phone_num");
                String location = rs.getString("location");
                Date dateOfBirth = rs.getDate("date_of_birth");

                Student s = new Student(id, name, email, password, major, uni, dateOfBirth);
                s.setPhoneNum(phoneNum);
                s.setLocation(location);
                s.setProfilePhotoContentType(rs.getString("profile_photo_content_type"));
                s.setProfilePhoto(rs.getBytes("profile_photo"));
                students.add(s);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (con != null) {
                try {
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

    public Student getStudentByEmail(String email) {
        Student student = null;
        Connection con = null;
        try {
            con = DriverManager.getConnection(constr);
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
                String phoneNum = rs.getString("phone_num");
                String location = rs.getString("location");
                Date dateOfBirth = rs.getDate("date_of_birth");

                student = new Student(id, name, email, password, major, uni, dateOfBirth);
                student.setPhoneNum(phoneNum);
                student.setLocation(location);
                student.setProfilePhotoContentType(rs.getString("profile_photo_content_type"));
                student.setProfilePhoto(rs.getBytes("profile_photo"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return student;
    }

    public Student updateStudent(String email, Student updated) {
        Connection con = null;
        Student student = null;
        try {
            con = DriverManager.getConnection(constr);
            student = getStudentByEmail(email);
            student.setId(updated.getId());
            student.setName(updated.getName());
            student.setEmail(updated.getEmail());
            student.setPassword(updated.getPassword());
            student.setMajor(updated.getMajor());
            student.setUniversity(updated.getUniversity());
            student.setPhoneNum(updated.getPhoneNum());
            student.setLocation(updated.getLocation());
            student.setDateOfBirth(updated.getDateOfBirth());
            student.setProfilePhotoContentType(updated.getProfilePhotoContentType());
            student.setProfilePhoto(updated.getProfilePhoto());

            String update = "UPDATE students SET name = ?, email = ?, password = ?, major = ?, university = ?, phone_num = ?, location = ?, date_of_birth = ?, profile_photo_content_type = ?, profile_photo = ? WHERE email = ?";
            PreparedStatement pstmt = con.prepareStatement(update);
            pstmt.setString(1, updated.getName());
            pstmt.setString(2, updated.getEmail());
            pstmt.setString(3, updated.getPassword());
            pstmt.setString(4, updated.getMajor());
            pstmt.setString(5, updated.getUniversity());
            pstmt.setString(6, updated.getPhoneNum());
            pstmt.setString(7, updated.getLocation());
            pstmt.setDate(8, new java.sql.Date(updated.getDateOfBirth().getTime()));
            pstmt.setString(9, updated.getProfilePhotoContentType());
            pstmt.setBytes(10, updated.getProfilePhoto());
            pstmt.setString(11, email);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return student;
    }


    public void deleteStudent(String email) {
        Connection con = null;
        try {
            con = DriverManager.getConnection(constr);
            String update = "DELETE FROM students WHERE email = ?";
            PreparedStatement pstmt = con.prepareStatement(update);
            pstmt.setString(1, email);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    public void uploadcv(int studentemail, String fileName, byte[] cvBytes) {
        Connection con = null;
        try {
            con = DriverManager.getConnection(constr);
            String updateCv = "UPDATE students SET cv_file_name = ?, cv_document = ? WHERE id = ?";
            PreparedStatement pstmt = con.prepareStatement(updateCv);
            pstmt.setString(1, fileName);
            pstmt.setBytes(2, cvBytes);
            pstmt.setInt(3, studentemail);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void uploadProfilePhoto(int studentId, String contentType, byte[] photoBytes) {
        Connection con = null;
        try {
            con = DriverManager.getConnection(constr);
            String updatePhoto = "UPDATE students SET profile_photo_content_type = ?, profile_photo = ? WHERE id = ?";
            PreparedStatement pstmt = con.prepareStatement(updatePhoto);
            pstmt.setString(1, contentType);
            pstmt.setBytes(2, photoBytes);
            pstmt.setInt(3, studentId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public byte[] getProfilePhoto(int studentId) {
        Connection con = null;
        try {
            con = DriverManager.getConnection(constr);
            String query = "SELECT profile_photo FROM students WHERE id = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getBytes("profile_photo");
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public String getProfilePhotoContentType(int studentId) {
        Connection con = null;
        try {
            con = DriverManager.getConnection(constr);
            String query = "SELECT profile_photo_content_type FROM students WHERE id = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("profile_photo_content_type");
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}