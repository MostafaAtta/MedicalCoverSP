package com.atta.medicalcoversp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atta.medicalcoversp.R;
import com.atta.medicalcoversp.User;
import com.atta.medicalcoversp.Vaccine;
import com.atta.medicalcoversp.VaccineAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class VaccineFragment extends Fragment {

    View root;

    FirebaseFirestore db;

    RecyclerView vaccineRecycler;

    VaccineAdapter myAdapter;

    ArrayList<Vaccine> vaccines;


    User user;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_vaccine, container, false);

        user = VaccineFragmentArgs.fromBundle(getArguments()).getUser();

        initiateViews();

        return root;
    }


    private void initiateViews() {


        vaccineRecycler = root.findViewById(R.id.allergy_recyclerView);

        db = FirebaseFirestore.getInstance();

        getVaccine();

    }



    public void getVaccine(){
        db.collection("Users")
                .document(user.getId())
                .collection("Vaccine")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (!queryDocumentSnapshots.isEmpty()){
                        vaccines = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            Vaccine vaccine = documentSnapshot.toObject(Vaccine.class);
                            vaccine.setId(documentSnapshot.getId());
                            vaccines.add(vaccine);
                            //updateDoctorName(labTestRecord);
                        }

                        showRecycler(vaccines);
                    }


                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show());

    }


    private void showRecycler(ArrayList<Vaccine> vaccines) {
        myAdapter = new VaccineAdapter(vaccines);

        vaccineRecycler.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));

        vaccineRecycler.setAdapter(myAdapter);
    }

}