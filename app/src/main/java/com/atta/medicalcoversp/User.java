package com.atta.medicalcoversp;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {

    private String id, fullName, email, phone, doctorId, pharmacyId, centerId, dateOfBirth, gender, membershipNumber,
            policyHolder, policyNumber, bloodType, city;

    private ArrayList<String> chronicDiseases;

    private ArrayList<String> tokens;

    private boolean isPregnant, isChronicDiseases;

    private int type;

    public User() {
    }

    public User(String fullName, String email, String phone, String city, String dateOfBirth,
                String gender, String membershipNumber, String policyHolder, String policyNumber,
                String bloodType, ArrayList<String> chronicDiseases, boolean isPregnant,
                boolean isChronicDiseases, int type, ArrayList<String> tokens) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.membershipNumber = membershipNumber;
        this.policyHolder = policyHolder;
        this.policyNumber = policyNumber;
        this.bloodType = bloodType;
        this.chronicDiseases = chronicDiseases;
        this.isPregnant = isPregnant;
        this.isChronicDiseases = isChronicDiseases;
        this.type = type;
        this.city = city;
        this.tokens = tokens;
    }


    public User(String fullName, String email, String phone, String doctorId, String dateOfBirth,
                String gender, int type) {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPharmacyId() {
        return pharmacyId;
    }

    public String getCenterId() {
        return centerId;
    }

    public String getBloodType() {
        return bloodType;
    }

    public ArrayList<String> getChronicDiseases() {
        return chronicDiseases;
    }

    public boolean getIsPregnant() {
        return isPregnant;
    }

    public boolean getIsChronicDiseases() {
        return isChronicDiseases;
    }

    public ArrayList<String> getTokens() {
        return tokens;
    }

    public void setTokens(ArrayList<String> tokens) {
        this.tokens = tokens;
    }
}
