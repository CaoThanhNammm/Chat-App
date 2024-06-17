package com.exam.chatapp.model;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class User implements Serializable {
    private String id;
    private String phone;
    private String password;
    private String fullname;
    private LocalDate dob;
    private LocalDate createdAt;
    private String encodedImage;
    private String fcmToken;

    public User() {
    }


    public User(String id, String phone, String password, String fullname, LocalDate dob, LocalDate createdAt, String encodedImage, String fcmToken) {
        this.id = id;
        this.phone = phone;
        this.password = password;
        this.fullname = fullname;
        this.dob = dob;
        this.createdAt = createdAt;
        this.encodedImage = encodedImage;
        this.fcmToken = fcmToken;
    }

    public String getEncodedImage() {
        return encodedImage;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", fullname='" + fullname + '\'' +
                ", dob=" + dob +
                ", createdAt=" + createdAt +
                ", encodedImage='" + encodedImage + '\'' +
                ", fcmToken='" + fcmToken + '\'' +
                '}';
    }

    public void setEncodedImage(String encodedImage) {
        this.encodedImage = encodedImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
}
