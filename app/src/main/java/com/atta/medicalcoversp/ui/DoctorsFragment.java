package com.atta.medicalcoversp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atta.medicalcoversp.Doctor;
import com.atta.medicalcoversp.DoctorsAdapter;
import com.atta.medicalcoversp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DoctorsFragment extends Fragment {

    View root;

    FirebaseFirestore db;

    RecyclerView recyclerView;

    FloatingActionButton add;

    DoctorsAdapter myAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_doctors, container, false);

        recyclerView = root.findViewById(R.id.doctors_recyclerView);
        add = root.findViewById(R.id.floatingActionButton);

        db = FirebaseFirestore.getInstance();

        getDoctors(Arrays.asList("Internal", "External", "Internal and External"));

        add.setOnClickListener(v ->
                        Navigation.findNavController(v).navigate(DoctorsFragmentDirections.actionDoctorsFragmentToNewDoctorFragment())
                );
        return root;
    }


    public void getDoctors(List<String> types){

        db.collection("Doctors")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()){

                            ArrayList<Doctor> data = new ArrayList<>();

                            for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                                Doctor doctor = documentSnapshot.toObject(Doctor.class);
                                doctor.setId(documentSnapshot.getId());
                                data.add(doctor);

                                //add(documentSnapshot.toObject(Doctor.class));
                            }

                            showRecycler(data);
                        }else {
                            Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
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

    private void showRecycler(ArrayList<Doctor> data) {

        myAdapter = new DoctorsAdapter(data, getActivity(), this);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        recyclerView.setAdapter(myAdapter);

    }


}