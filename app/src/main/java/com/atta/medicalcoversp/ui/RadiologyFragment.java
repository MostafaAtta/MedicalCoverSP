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
import com.atta.medicalcoversp.RadiologyRecord;
import com.atta.medicalcoversp.RadiologyRecordsAdapter;
import com.atta.medicalcoversp.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class RadiologyFragment extends Fragment {

    View root;

    FirebaseFirestore db;

    RecyclerView radiologyRecycler;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_radiology, container, false);

        user = RadiologyFragmentArgs.fromBundle(getArguments()).getUser();

        initiateViews();

        return root;
    }

    private void initiateViews() {

        radiologyRecycler = root.findViewById(R.id.radiologyRecycler);

        db = FirebaseFirestore.getInstance();

        getRadiology();
    }


    public void getRadiology(){
        db.collection("Radiology Records")
                .whereEqualTo("userId", user.getId())
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (!queryDocumentSnapshots.isEmpty()){
                        ArrayList<RadiologyRecord> radiologyRecords = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            RadiologyRecord labTestRecord = documentSnapshot.toObject(RadiologyRecord.class);
                            labTestRecord.setId(documentSnapshot.getId());
                            radiologyRecords.add(labTestRecord);
                            //updateDoctorName(labTestRecord);
                        }

                        showRecycler(radiologyRecords);
                    }


                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show());

    }


    private void showRecycler(ArrayList<RadiologyRecord> radiologyRecords) {
        RadiologyRecordsAdapter myAdapter = new RadiologyRecordsAdapter(radiologyRecords, getActivity());

        radiologyRecycler.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));

        radiologyRecycler.setAdapter(myAdapter);
    }
}