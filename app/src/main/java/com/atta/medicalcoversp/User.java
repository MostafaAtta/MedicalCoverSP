package com.atta.medicalcoversp;

import java.io.Serializable;

public class User implements Serializable {

    private String fullName, email, phone, doctorId, dateOfBirth, gender, membershipNumber,
            policyHolder, policyNumber;

    private int type;

    public User() {
    }

    public User(String fullName, String email, String phone, String doctorId, String dateOfBirth, String gender, int type) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.doctorId = doctorId;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.type = type;
    }

    public String getMembershipNumber() {
        return membershipNumber;
    }

    public String getPolicyHolder() {
        return policyHolder;
    }

    public String getPolicyNumber() {
        return policyNumber;
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

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getGender() {
        return gender;
    }
}
