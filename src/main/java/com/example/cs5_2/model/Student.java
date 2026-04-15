package com.example.cs5_2.model;

import java.io.Serializable;
import java.util.Date;

public class Student extends User implements Serializable {
    private String major;
    private String university;
    private Date dateOfBirth;
    private String cvFileName;
    private byte[] cvDocument;

    public Student() {
    }

    public Student(int id, String name, String email, String password, String major,String university, Date dateOfBirth) {
        super(id, name, email, password);
        this.university = university;
        this.major = major;
        this.dateOfBirth = dateOfBirth;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getCvFileName() {
        return cvFileName;
    }

    public void setCvFileName(String cvFileName) {
        this.cvFileName = cvFileName;
    }

    public byte[] getCvDocument() {
        return cvDocument;
    }

    public void setCvDocument(byte[] cvDocument) {
        this.cvDocument = cvDocument;
    }

    // Convenience method to set both CV fields in one call.
    public void setCv(String cvFileName, byte[] cvDocument) {
        this.cvFileName = cvFileName;
        this.cvDocument = cvDocument;
    }

    //public Internship browseinternships(Internship internship){}
    //public void applytointernship(Internship internship){}
    //public void trackapplication(Internship insternship){}


    @Override
    public String toString() {
        return "Student{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", major='" + major + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }
}
