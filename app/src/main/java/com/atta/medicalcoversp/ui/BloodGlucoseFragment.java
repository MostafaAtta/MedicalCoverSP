package com.atta.medicalcoversp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atta.medicalcoversp.BloodGlucose;
import com.atta.medicalcoversp.BloodGlucoseAdapter;
import com.atta.medicalcoversp.R;
import com.atta.medicalcoversp.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class BloodGlucoseFragment extends Fragment {

    View root;

    FirebaseFirestore db;

    RecyclerView bloodGlucoseRecycler;

    BloodGlucoseAdapter myAdapter;

    ArrayList<BloodGlucose> bloodGlucoseRecords;

    User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_blood_glucose, container, false);

        user = BloodGlucoseFragmentArgs.fromBundle(getArguments()).getUser();

        initiateViews();

        return root;
    }

    private void initiateViews() {

        bloodGlucoseRecycler = root.findViewById(R.id.blood_glucose_recyclerView);


        db = FirebaseFirestore.getInstance();

        getBloodGlucose();

    }



    public void getBloodGlucose(){
        db.collection("Users")
                .document(user.getId())
                .collection("Blood Glucose")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (!queryDocumentSnapshots.isEmpty()){
                        bloodGlucoseRecords = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            BloodGlucose bloodGlucose = documentSnapshot.toObject(BloodGlucose.class);
                            bloodGlucose.setId(documentSnapshot.getId());
                            bloodGlucoseRecords.add(bloodGlucose);
                            //updateDoctorName(labTestRecord);
                        }

                        showRecycler(bloodGlucoseRecords);
                    }


                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show());

    }


    private void showRecycler(ArrayList<BloodGlucose> bloodGlucoseRecords) {
        myAdapter = new BloodGlucoseAdapter(bloodGlucoseRecords);

        bloodGlucoseRecycler.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));

        bloodGlucoseRecycler.setAdapter(myAdapter);
    }

}