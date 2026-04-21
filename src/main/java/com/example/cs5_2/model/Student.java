package com.example.cs5_2.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

public class Student extends User implements Serializable {
    private String major;
    private String university;
    private String phoneNum;
    private String location;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateOfBirth;
    private String profilePhotoContentType;
    private byte[] profilePhoto;
    private BuildCV buildCV;


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

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }


    public String getProfilePhotoContentType() {
        return profilePhotoContentType;
    }

    public void setProfilePhotoContentType(String profilePhotoContentType) {
        this.profilePhotoContentType = profilePhotoContentType;
    }

    public byte[] getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(byte[] profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public void setProfilePhoto(String profilePhotoContentType, byte[] profilePhoto) {
        this.profilePhotoContentType = profilePhotoContentType;
        this.profilePhoto = profilePhoto;
    }

    public BuildCV getBuildCV() {
        return buildCV;
    }

    public void setBuildCV(BuildCV buildCV) {
        this.buildCV = buildCV;
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
                ", phoneNum='" + phoneNum + '\'' +
                ", location='" + location + '\'' +
                ", major='" + major + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }
}
