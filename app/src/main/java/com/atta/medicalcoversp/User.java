package com.atta.medicalcoversp;

public class User {

    private String fullName, email, phone, doctorId;

    private int type;


    public User() {
    }

    public User(String fullName, String email, String phone, int type) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.type = type;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public int getType() {
        return type;
    }

    public String getDoctorId() {
        return doctorId;
    }
}
