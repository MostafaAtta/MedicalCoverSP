package com.atta.medicalcoversp;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

import java.io.Serializable;

public class TestRequest implements Parcelable {

    private String id, appointmentId, centerId, centerName, patientId, patientName, prescriptionId,
            status, type, doctorId, doctorName;

    private Timestamp timestamp;

    public TestRequest(){

    }

    protected TestRequest(Parcel in) {
        id = in.readString();
        appointmentId = in.readString();
        centerId = in.readString();
        centerName = in.readString();
        patientId = in.readString();
        patientName = in.readString();
        prescriptionId = in.readString();
        status = in.readString();
        type = in.readString();
        doctorId = in.readString();
        doctorName = in.readString();
        timestamp = in.readParcelable(Timestamp.class.getClassLoader());
    }

    public static final Creator<TestRequest> CREATOR = new Creator<TestRequest>() {
        @Override
        public TestRequest createFromParcel(Parcel in) {
            return new TestRequest(in);
        }

        @Override
        public TestRequest[] newArray(int size) {
            return new TestRequest[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public String getCenterId() {
        return centerId;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getPrescriptionId() {
        return prescriptionId;
    }

    public String getStatus() {
        return status;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getType() {
        return type;
    }

    public String getCenterName() {
        return centerName;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(appointmentId);
        dest.writeString(centerId);
        dest.writeString(centerName);
        dest.writeString(patientId);
        dest.writeString(patientName);
        dest.writeString(prescriptionId);
        dest.writeString(status);
        dest.writeString(type);
        dest.writeString(doctorId);
        dest.writeString(doctorName);
        dest.writeParcelable(timestamp, flags);
    }
}
