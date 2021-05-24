package com.atta.medicalcoversp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atta.medicalcoversp.BloodPressure;
import com.atta.medicalcoversp.BloodPressureAdapter;
import com.atta.medicalcoversp.R;
import com.atta.medicalcoversp.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;


public class BloodPressureFragment extends Fragment {

    View root;

    FirebaseFirestore db;

    RecyclerView bloodPressureRecycler;

    BloodPressureAdapter myAdapter;

    ArrayList<BloodPressure> bloodPressures;

    User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_blood_pressure, container, false);

        user = MedicalHistoryFragmentArgs.fromBundle(getArguments()).getUser();

        initiateViews();

        return root;
    }


    private void initiateViews() {

        bloodPressureRecycler = root.findViewById(R.id.blood_pressure_recyclerView);

        db = FirebaseFirestore.getInstance();

        getBloodPressure();

    }

    public void getBloodPressure(){
        db.collection("Users")
                .document(user.getId())
                .collection("Blood Pressure")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (!queryDocumentSnapshots.isEmpty()){
                        bloodPressures = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            BloodPressure bloodPressure = documentSnapshot.toObject(BloodPressure.class);
                            bloodPressure.setId(documentSnapshot.getId());
                            bloodPressures.add(bloodPressure);
                            //updateDoctorName(labTestRecord);
                        }

                        showRecycler(bloodPressures);
                    }


                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show());

    }


    private void showRecycler(ArrayList<BloodPressure> bloodPressures) {
        myAdapter = new BloodPressureAdapter(bloodPressures);

        bloodPressureRecycler.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));

        bloodPressureRecycler.setAdapter(myAdapter);
    }

    public void deleteAllergy(int i) {

        bloodPressures.remove(i);
    }
}