package com.atta.medicalcoversp.ui;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.atta.medicalcoversp.Appointment;
import com.atta.medicalcoversp.Medication;
import com.atta.medicalcoversp.Prescription;
import com.atta.medicalcoversp.R;
import com.atta.medicalcoversp.Specialty;
import com.atta.medicalcoversp.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NewPrescriptionFragment extends Fragment {

    TextView patientNameTv, policyNoTv, membershipNoTv, companyNameTv, medicationsTv, labRadioServicesTv;

    EditText diagnosisText;

    View root;

    User patient;

    Appointment appointment;

    ArrayList<String> specialties, diagnosisList, medicalTests;

    ArrayList<Medication> medications;

    Spinner specialtiesSpinner, diagnosisSpinner;

    FirebaseFirestore db;

    String diagnosis;

    ImageView addMedicationImg, addMedicalTestsImg;

    Button confirmBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_new_prescription, container, false);

        patient = NewPrescriptionFragmentArgs.fromBundle(getArguments()).getUser();

        appointment = NewPrescriptionFragmentArgs.fromBundle(getArguments()).getAppointment();

        db = FirebaseFirestore.getInstance();

        initiateViews();

        return root;
    }


    private void initiateViews() {
        specialties = new ArrayList<>();
        diagnosisList = new ArrayList<>();
        medications = new ArrayList<>();
        medicalTests = new ArrayList<>();
        patientNameTv = root.findViewById(R.id.patient_name);
        policyNoTv = root.findViewById(R.id.policy_no);
        membershipNoTv = root.findViewById(R.id.membership_no);
        companyNameTv = root.findViewById(R.id.company_name);
        medicationsTv = root.findViewById(R.id.medications);
        labRadioServicesTv= root.findViewById(R.id.labRadioServices);

        diagnosisText = root.findViewById(R.id.diagnosisText);
        specialtiesSpinner = root.findViewById(R.id.speciality_spinner);
        diagnosisSpinner = root.findViewById(R.id.diagnosis_spinner);
        addMedicationImg = root.findViewById(R.id.addMedication);
        addMedicalTestsImg = root.findViewById(R.id.addLab);
        confirmBtn = root.findViewById(R.id.confirmBtn);
        confirmBtn.setOnClickListener(view -> addPrescription());

        addMedicationImg.setOnClickListener(view -> showMedicationDialog());
        addMedicalTestsImg.setOnClickListener(view -> showMedicalTestsDialog());

        patientNameTv.setText(patient.getFullName());
        policyNoTv.setText(patient.getPolicyNumber());
        membershipNoTv.setText(patient.getMembershipNumber());
        companyNameTv.setText(patient.getPolicyHolder());

        getSpecialties();
    }

    private void addPrescription() {
        Timestamp creationDate = new Timestamp(new Date());
        if (diagnosis.equalsIgnoreCase("not mentioned")){
            diagnosis = diagnosisText.getText().toString().trim();
            if (diagnosis.isEmpty()){
                Toast.makeText(getContext(), "You must enter a diagnosis", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Map<String, Object> prescription = new HashMap<>();

        prescription.put("appointmentId", appointment.getId());
        prescription.put("patientName", patient.getFullName());
        prescription.put("patientId", patient.getId());
        prescription.put("policyNumber", patient.getPolicyNumber());
        prescription.put("membershipNumber", patient.getMembershipNumber());
        prescription.put("policyHolder", patient.getPolicyHolder());
        prescription.put("diagnosis", diagnosis);
        prescription.put("creationDate", creationDate);
        prescription.put("medications", medications);
        prescription.put("medicalTests", medicalTests);

        db.collection("Prescriptions").add(prescription)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getContext(), "Prescriptions added", Toast.LENGTH_SHORT).show();
                        finishVisit();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "something wrong", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void finishVisit(){

        Map<String, String> map = new HashMap<>();
        map.put("status", "Finished");
        db.collection("Appointments").document(appointment.getId())
                .set(map, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Navigation.findNavController(getActivity(), R.id.nav_host_fragment)
                                .navigate(NewPrescriptionFragmentDirections.actionNavigationNewPrescriptionToNavigationAppointment());

                        Toast.makeText(getContext(), "status updated", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error, try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }



    private void showMedicalTestsDialog() {

        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.new_medical_test_dialog);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialog.findViewById(R.id.okBtn).setOnClickListener(view -> {

            String testName = ((TextInputEditText) dialog.findViewById(R.id.medicalTestNameTxt))
                    .getText().toString().trim();
            if (testName.isEmpty()){
                Toast.makeText(getContext(), "You must enter test details", Toast.LENGTH_SHORT).show();

            }else {

                medicalTests.add(testName);

                if (medicalTests.size() == 1) {
                    labRadioServicesTv.setText(testName);
                } else {
                    labRadioServicesTv.append("\n" + testName);
                }

                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.cancelBtn).setOnClickListener(view -> dialog.dismiss()
        );
        dialog.show();
    }

    private void showMedicationDialog() {

        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.new_medication_dialog);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialog.findViewById(R.id.okBtn).setOnClickListener(view -> {

            String medicationName = ((TextInputEditText) dialog.findViewById(R.id.medicationNameTxt))
                    .getText().toString().trim();
            String medicationDose = ((TextInputEditText) dialog.findViewById(R.id.doseTxt))
                    .getText().toString().trim();
            String medicationDuration = ((TextInputEditText) dialog.findViewById(R.id.durationTxt))
                    .getText().toString().trim();

            if (medicationName.isEmpty() || medicationDose.isEmpty() || medicationDuration.isEmpty()){
                Toast.makeText(getContext(), "You must enter all medication details", Toast.LENGTH_SHORT).show();

            }else {

                Medication medication = new Medication(medicationName, medicationDose, medicationDuration);
                medications.add(medication);

                if (medications.size() == 1){
                    medicationsTv.setText(medication.toString());
                }else {
                    medicationsTv.append("\n" + medication.toString());
                }

                dialog.dismiss();
            }

        });
        dialog.findViewById(R.id.cancelBtn).setOnClickListener(view -> dialog.dismiss()
        );
        dialog.show();

    }

    private void getSpecialties() {
        db.collection("Specialties").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()){
                        for (DocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            Specialty specialty = documentSnapshot.toObject(Specialty.class);
                            specialties.add(specialty.getName());
                        }
                        showSpecialtiesSpinner();
                    }else {
                        Toast.makeText(getContext(), "Can't load specialties", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(
                        e -> Toast.makeText(getContext(), "Can't load specialties", Toast.LENGTH_SHORT).show()
                );
    }

    private void showSpecialtiesSpinner() {
        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_list_item_1,
                specialties);

        specialtiesSpinner.setAdapter(myAdapter);

        specialtiesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getDiagnosis(specialties.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void getDiagnosis(String specialty) {
        diagnosisList.clear();
        db.collection("Diagnosis")
                .whereEqualTo("specialty", specialty)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()){
                        for (DocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            String d = (String) documentSnapshot.getData().get("name");
                            diagnosisList.add(d);
                        }
                        showDiagnosisSpinner();
                    }else {
                        Toast.makeText(getContext(), "Can't load diagnosis", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(
                        e -> Toast.makeText(getContext(), "Can't load diagnosis", Toast.LENGTH_SHORT).show()
                );
    }


    private void showDiagnosisSpinner() {
        diagnosisList.add("not mentioned");
        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_list_item_1,
                diagnosisList);

        diagnosisSpinner.setAdapter(myAdapter);

        diagnosisSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                diagnosis = diagnosisList.get(i);

                if (diagnosis.equalsIgnoreCase("not mentioned")){
                    diagnosisText.setVisibility(View.VISIBLE);
                }else {
                    diagnosisText.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

}