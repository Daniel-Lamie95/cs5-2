package com.example.apa_project;
import java.sql.*;

public class StudentDB {
    private String constr = "null";

    public StudentDB(){
        Connection con = null;
        try{
            // load driver
            Class.forName("null");

            con = DriverManager.getConnection(constr);
            Statement stmt = con.createStatement();

            String createtable = "CREATE TABLE IF NOT EXISTS students (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "name VARCHAR(255) NOT NULL," +
                    "email VARCHAR(255) NOT NULL UNIQUE," +
                    "password VARCHAR(255) NOT NULL," +
                    "major VARCHAR(255) NOT NULL," +
                    "date_of_birth DATE NOT NULL," +
                    "cv_file_name VARCHAR(255)," +
                    "cv_document BLOB" +
                    ")";

            stmt.executeUpdate(createtable);

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
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
    }

    public void insertStudent(){

    }





}
