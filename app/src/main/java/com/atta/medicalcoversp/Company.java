package com.atta.medicalcoversp;

import java.io.Serializable;

public class Company implements Serializable {

    private String id, policyHolder, policyNumber;

    public Company(String id, String policyHolder, String policyNumber) {
        this.id = id;
        this.policyHolder = policyHolder;
        this.policyNumber = policyNumber;
    }

    public String getId() {
        return id;
    }

    public String getPolicyHolder() {
        return policyHolder;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setId(String id) {
        this.id = id;
    }
}
