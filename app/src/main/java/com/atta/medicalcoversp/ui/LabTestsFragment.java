package com.atta.medicalcoversp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atta.medicalcoversp.LabTestRecord;
import com.atta.medicalcoversp.LabTestRecordsAdapter;
import com.atta.medicalcoversp.R;
import com.atta.medicalcoversp.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class LabTestsFragment extends Fragment {

    View root;

    FirebaseFirestore db;

    RecyclerView labTestRecycler;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_lab_tests, container, false);

        user = LabTestsFragmentArgs.fromBundle(getArguments()).getUser();

        initiateViews();

        return root;
    }

    private void initiateViews() {

        labTestRecycler = root.findViewById(R.id.labTestRecycler);

        db = FirebaseFirestore.getInstance();

        getLabTests();
    }


    public void getLabTests(){
        db.collection("LabTest Records")
                .whereEqualTo("userId", user.getId())
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (!queryDocumentSnapshots.isEmpty()){
                        ArrayList<LabTestRecord> labTestRecords = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            LabTestRecord labTestRecord = documentSnapshot.toObject(LabTestRecord.class);
                            labTestRecord.setId(documentSnapshot.getId());
                            labTestRecords.add(labTestRecord);
                            //updateDoctorName(labTestRecord);
                        }

                        showRecycler(labTestRecords);
                    }


                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }


    private void showRecycler(ArrayList<LabTestRecord> labTestRecords) {
        LabTestRecordsAdapter myAdapter = new LabTestRecordsAdapter(labTestRecords, getActivity());

        labTestRecycler.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));

        labTestRecycler.setAdapter(myAdapter);
    }

}