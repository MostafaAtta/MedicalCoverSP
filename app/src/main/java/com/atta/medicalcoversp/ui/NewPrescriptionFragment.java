package com.atta.medicalcoversp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.atta.medicalcoversp.Appointment;
import com.atta.medicalcoversp.R;
import com.atta.medicalcoversp.Specialty;
import com.atta.medicalcoversp.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class NewPrescriptionFragment extends Fragment {

    TextView patientNameTv, policyNoTv, membershipNoTv, companyNameTv;

    EditText diagnosisText;

    View root;

    User patient;

    Appointment appointment;

    ArrayList<String> specialties, diagnosisList;

    Spinner specialtiesSpinner, diagnosisSpinner;

    FirebaseFirestore db;

    String diagnosis;

    ImageView addMedicationImg, addLabImg;

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
        patientNameTv = root.findViewById(R.id.patient_name);
        policyNoTv = root.findViewById(R.id.policy_no);
        membershipNoTv = root.findViewById(R.id.membership_no);
        companyNameTv = root.findViewById(R.id.company_name);

        diagnosisText = root.findViewById(R.id.diagnosisText);
        specialtiesSpinner = root.findViewById(R.id.speciality_spinner);
        diagnosisSpinner = root.findViewById(R.id.diagnosis_spinner);
        addMedicationImg = root.findViewById(R.id.addMedication);
        addLabImg = root.findViewById(R.id.addLab);

        addMedicationImg.setOnClickListener(view -> showMedicationDialog());
        addLabImg.setOnClickListener(view -> showLabDialog());

        patientNameTv.setText(patient.getFullName());
        policyNoTv.setText(patient.getPolicyNumber());
        membershipNoTv.setText(patient.getMembershipNumber());
        companyNameTv.setText(patient.getPolicyHolder());

        getSpecialties();
    }

    private void showLabDialog() {

    }

    private void showMedicationDialog() {

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
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

}