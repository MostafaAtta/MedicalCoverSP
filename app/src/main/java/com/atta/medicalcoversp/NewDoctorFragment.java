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

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewDoctorFragment extends Fragment {

    View root;

    FirebaseFirestore db;

    ArrayList<String> specialties, selectedSpecialties;

    MultiSpinner specialtiesSpinner;

    Spinner typesSpinner;
    ArrayAdapter<String> typesAdapter;

    String type, name;

    TextInputEditText  nameText;

    Button createBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_new_doctor, container, false);

        initiateViews();

        getSpecialties();

        return root;
    }


    private void initiateViews(){

        db = FirebaseFirestore.getInstance();

        specialtiesSpinner = root.findViewById(R.id.specialties_spinner);
        typesSpinner = root.findViewById(R.id.type_spinner);
        nameText = root.findViewById(R.id.registerName);
        createBtn = root.findViewById(R.id.button_create);


        ArrayList<String> types = new ArrayList<>();
        types.add("Internal");
        types.add("External");
        types.add("Internal and External");

        typesAdapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_list_item_1,
                types);

        typesSpinner.setAdapter(typesAdapter);

        typesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type = types.get(i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                type = types.get(0);
            }
        });

        createBtn.setOnClickListener(v -> createDoctor());

    }

    private void createDoctor() {
        name = nameText.getText().toString().trim();

        if(!verify()){
            return;
        }
        Map<String, Object> doctor = new HashMap<>();
        doctor.put("name", name);
        doctor.put("type", type);
        doctor.put("specialities", selectedSpecialties);

        db.collection("Doctors").add(doctor)
                .addOnSuccessListener(documentReference ->
                        Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show());
    }

    private void getSpecialties() {
        db.collection("Specialties")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()){

                        specialties = new ArrayList<>();

                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            Specialty specialty = documentSnapshot.toObject(Specialty.class);
                            specialties.add(specialty.getName());

                            //add(documentSnapshot.toObject(Doctor.class));
                        }

                        showSpecialties();
                    }else {
                        Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show());
    }

    private void showSpecialties() {

        selectedSpecialties = new ArrayList<>();

        specialtiesSpinner.setItems(specialties, getString(R.string.none), selected -> {
            for (int i = 0; i < selected.length; i++) {
                if (selected[i]){
                    selectedSpecialties.add(specialties.get(i));
                }
            }
        });
    }


    public boolean verify(){
        boolean valid = true;

        if (name.isEmpty()){
            nameText.setError("Enter your Name");
            valid = false;
        }

        if (selectedSpecialties.isEmpty()){
            Toast.makeText(getContext(), "Select specialties", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }

}