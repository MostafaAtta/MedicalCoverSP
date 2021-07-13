package com.atta.medicalcoversp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewAccountFragment extends Fragment {

    View root;

    Button registerBtn;

    TextInputEditText emailText, nameText, phoneText;

    String email, fullName, type, phone, doctorId, pharmacyId, centerId;

    int userType;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private static final String TAG = "NewAccountFragment";

    ArrayList<Doctor> doctors;
    ArrayList<Pharmacy> pharmacies;
    ArrayList<TestCenter> testCenters;

    ArrayList<String> serviceProviders;

    ArrayAdapter<String> typesAdapter, serviceProviderAdapter;

    Spinner typesSpinner, serviceProviderSpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_new_account, container, false);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        initiateViews();
        return root;
    }


    private void initiateViews() {
        db = FirebaseFirestore.getInstance();

        typesSpinner = root.findViewById(R.id.type_spinner);
        serviceProviderSpinner = root.findViewById(R.id.provider_spinner);

        registerBtn = root.findViewById(R.id.button_register);
        registerBtn.setOnClickListener(v -> register());

        emailText = root.findViewById(R.id.registerEmail);
        nameText = root.findViewById(R.id.registerName);
        phoneText = root.findViewById(R.id.registerPhone);

        ArrayList<String> types = new ArrayList<>();
        types.add("Doctor");
        types.add("Pharmacy");
        types.add("Laboratory");
        types.add("Radiology");

        typesAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_list_item_1,
                types);

        typesSpinner.setAdapter(typesAdapter);

        typesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type = types.get(i);

                serviceProviders = new ArrayList<>();
                switch (type){
                    case "Doctor":

                        userType = 1;
                        getDoctors();
                        break;
                    case "Pharmacy":

                        userType = 2;
                        getPharmacies();
                        break;
                    case "Laboratory":

                        userType = 3;
                        getLabs();
                        break;
                    case "Radiology":

                        userType = 4;
                        getRadioCenters();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                type = types.get(0);
            }
        });

    }

    private void getRadioCenters() {
        serviceProviders.clear();
        db.collection("Services")
                .whereEqualTo("type", "Radiology")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()){

                        testCenters = new ArrayList<>();

                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            TestCenter testCenter = documentSnapshot.toObject(TestCenter.class);
                            testCenter.setId(documentSnapshot.getId());
                            testCenters.add(testCenter);
                            serviceProviders.add(testCenter.getName());

                            //add(documentSnapshot.toObject(Doctor.class));
                        }

                        showProvidersSpinner();
                    }else {
                        Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show());
    }

    private void getLabs() {
        serviceProviders.clear();
        db.collection("Services")
                .whereEqualTo("type", "Laboratory")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()){

                        testCenters = new ArrayList<>();

                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            TestCenter testCenter = documentSnapshot.toObject(TestCenter.class);
                            testCenter.setId(documentSnapshot.getId());
                            testCenters.add(testCenter);
                            serviceProviders.add(testCenter.getName());

                            //add(documentSnapshot.toObject(Doctor.class));
                        }

                        showProvidersSpinner();
                    }else {
                        Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show());
    }

    private void getPharmacies() {
        serviceProviders.clear();
        db.collection("Pharmacies")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()){

                        pharmacies = new ArrayList<>();

                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            Pharmacy pharmacy = documentSnapshot.toObject(Pharmacy.class);
                            pharmacy.setId(documentSnapshot.getId());
                            pharmacies.add(pharmacy);
                            serviceProviders.add(pharmacy.getName());

                            //add(documentSnapshot.toObject(Doctor.class));
                        }

                        showProvidersSpinner();
                    }else {
                        Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show());
    }

    private void getDoctors() {
        serviceProviders.clear();
        db.collection("Doctors")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()){

                        doctors = new ArrayList<>();

                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            Doctor doctor = documentSnapshot.toObject(Doctor.class);
                            doctor.setId(documentSnapshot.getId());
                            doctors.add(doctor);
                            serviceProviders.add(doctor.getName());

                            //add(documentSnapshot.toObject(Doctor.class));
                        }

                        showProvidersSpinner();
                    }else {
                        Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show());
    }

    private void showProvidersSpinner() {

        serviceProviderAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_list_item_1,
                serviceProviders);

        serviceProviderSpinner.setAdapter(serviceProviderAdapter);

        serviceProviderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (type){
                    case "Doctor":
                        doctorId = doctors.get(i).getId();
                        break;
                    case "Pharmacy":
                        pharmacyId = pharmacies.get(i).getId();
                        break;
                    case "Laboratory":
                    case "Radiology":
                        centerId = testCenters.get(i).getId();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                doctorId = doctors.get(0).getId();
            }
        });
    }

    private void register(){
        email = emailText.getText().toString().trim();
        phone = phoneText.getText().toString().trim();
        fullName = nameText.getText().toString().trim();

        if(!verify()){
            return;
        }
        checkUser();
    }


    public boolean verify(){
        boolean valid = true;

        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (email.isEmpty() || email.equals("")){
            emailText.setError("Enter your Email");
            valid = false;
        }else if (!email.matches(emailPattern)){
            emailText.setError("Enter a valid Email");
            valid = false;
        }

        if (fullName.isEmpty() || fullName.equals("")){
            nameText.setError("Enter your Name");
            valid = false;
        }

        if (phone.isEmpty() || phone.equals("")){
            phoneText.setError("Enter your Phone");
            valid = false;
        } else if(phone.length() < 11){
            phoneText.setError("Enter a valid phone number");
            valid = false;
        }

        return valid;
    }

    private void checkUser(){
        db.collection("Users").whereEqualTo("email", email).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()){
                            createUser();
                        }else {
                            Toast.makeText(getContext(), "Email already exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                });

    }


    private void createUser(){
        Map<String, Object> user = new HashMap<>();
        user.put("fullName", fullName);
        user.put("email", email);
        user.put("phone", phone);
        user.put("type", userType);
        user.put("newAccount", true);
        switch (type){
            case "Doctor":
                user.put("doctorId", doctorId);
                break;
            case "Pharmacy":
                user.put("pharmacyId", pharmacyId);
                break;
            case "Laboratory":
            case "Radiology":
                user.put("centerId", centerId);
                break;
        }

        db.collection("Users").add(user)
                .addOnSuccessListener(documentReference ->
                        Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show());

    }

}