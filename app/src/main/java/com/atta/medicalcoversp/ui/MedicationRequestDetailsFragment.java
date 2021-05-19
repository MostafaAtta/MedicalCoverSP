package com.atta.medicalcoversp.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.atta.medicalcoversp.MedicationRequest;
import com.atta.medicalcoversp.Prescription;
import com.atta.medicalcoversp.R;
import com.atta.medicalcoversp.SessionManager;
import com.atta.medicalcoversp.User;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MedicationRequestDetailsFragment extends Fragment {

    View root;

    TextView pharmacyName, dateTv, statusTv, patientNameTv, ageTv, genderTv,
            diagnosisTv, medicationsTv;

    Button approveBtn, rejectBtn, finishBtn;

    ImageView callImg;

    MedicationRequest medicationRequest;

    Prescription prescription;

    FirebaseFirestore db;

    User patient;

    String phoneNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_medication_request_details, container, false);

        medicationRequest = MedicationRequestDetailsFragmentArgs.fromBundle(getArguments()).getRequest();

        db = FirebaseFirestore.getInstance();

        initiateViews();

        return root;
    }


    private void initiateViews(){
        medicationsTv = root.findViewById(R.id.medications_tv);
        diagnosisTv = root.findViewById(R.id.diagnosis_tv);
        pharmacyName = root.findViewById(R.id.pharmacyNameTv);
        dateTv = root.findViewById(R.id.date_tv);
        statusTv = root.findViewById(R.id.status_tv);
        patientNameTv = root.findViewById(R.id.patient_name);
        ageTv = root.findViewById(R.id.age_tv);
        genderTv = root.findViewById(R.id.gender_tv);

        approveBtn = root.findViewById(R.id.approve_btn);
        approveBtn.setOnClickListener(v -> {
            updateStatus("Approved");
        });
        rejectBtn = root.findViewById(R.id.reject_btn);
        rejectBtn.setOnClickListener(v -> {
            updateStatus("Rejected");
        });

        finishBtn = root.findViewById(R.id.finish_btn);
        if (SessionManager.getInstance(getContext()).getType() == 2 &&
                medicationRequest.getStatus().equalsIgnoreCase("Approved")){
            finishBtn.setVisibility(View.VISIBLE);
        }
        finishBtn.setOnClickListener(v -> {
            updateStatus("Finished");
        });

        callImg = root.findViewById(R.id.call_imag);

        getUserDetails();

        if (!medicationRequest.getStatus().equalsIgnoreCase("pending approval")){

            rejectBtn.setVisibility(View.GONE);
            approveBtn.setVisibility(View.GONE);
        }

        if (SessionManager.getInstance(getContext()).getType() != 0){
            rejectBtn.setVisibility(View.GONE);
            approveBtn.setVisibility(View.GONE);
        }

        pharmacyName.setText(medicationRequest.getPharmacyName());

        String pattern = "EEE, dd MMM";
        SimpleDateFormat format = new SimpleDateFormat(pattern, new Locale("en", "US"));
        dateTv.setText(format.format(medicationRequest.getTimestamp().toDate()));

        statusTv.setText(medicationRequest.getStatus());

        switch (medicationRequest.getStatus()){
            case "pending approval":
                statusTv.setTextColor(getActivity().getResources().getColor(R.color.blue));
                break;
            case "Approved":
            case "Finished":
                statusTv.setTextColor(getActivity().getResources().getColor(R.color.green));
                break;
            case "rejected":
            case "Canceled":
                statusTv.setTextColor(getActivity().getResources().getColor(R.color.red));
                break;
            default:
                statusTv.setTextColor(getActivity().getResources().getColor(R.color.black));
        }
        getPrescription();
    }

    private void getUserDetails() {
        db.collection("Users")
                .document(medicationRequest.getPatientId())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    patient = documentSnapshot.toObject(User.class);
                    patient.setId(documentSnapshot.getId());
                    patientNameTv.setText(patient.getFullName());

                    phoneNumber = patient.getPhone();
                    callImg.setOnClickListener(v -> {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + phoneNumber));
                        startActivity(intent);
                    });

                    genderTv.setText(patient.getGender());
                    calculateAge();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "An Error", Toast.LENGTH_SHORT).show();
                });
    }

    private void calculateAge() {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        Date date = sdf.parse(patient.getDateOfBirth(), new ParsePosition(0));

        dob.setTime(date);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        String patientAge = String.valueOf(age);

        ageTv.setText(patientAge + " Years");
    }

    private void getPrescription() {
        db.collection("Prescriptions")
                .document(medicationRequest.getPrescriptionId())
                .get()
                .addOnSuccessListener(documentSnapshot -> {

                    if (documentSnapshot != null){

                        prescription = documentSnapshot.toObject(Prescription.class);
                        prescription.setId(documentSnapshot.getId());

                        diagnosisTv.setText(prescription.getDiagnosis());

                        for (int i = 0; i < prescription.getMedications().size(); i++) {
                            if (i == 0){

                                medicationsTv.setText(prescription.getMedications().get(i).toString());
                            }else {
                                medicationsTv.append("\n" + prescription.getMedications().get(i).toString());
                            }


                        }

                    }else {
                        Toast.makeText(getContext(), "No prescription add to this visit", Toast.LENGTH_SHORT).show();
                    }

                })
                .addOnFailureListener(e -> {

                });
    }


    private void updateStatus(String status) {

        Map<String, Object> req = new HashMap<>();
        req.put("status", status);
        db.collection("Medications Requests")
                .document(medicationRequest.getId())
                .update(req)
                .addOnSuccessListener(aVoid -> {
                    statusTv.setText(status);
                    approveBtn.setVisibility(View.GONE);
                    rejectBtn.setVisibility(View.GONE);
                    finishBtn.setVisibility(View.GONE);
                }).addOnFailureListener(e -> {

                });
    }



}