package com.atta.medicalcoversp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atta.medicalcoversp.FamilyHistory;
import com.atta.medicalcoversp.FamilyHistoryAdapter;
import com.atta.medicalcoversp.R;
import com.atta.medicalcoversp.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class FamilyHistoryFragment extends Fragment {

    View root;

    FirebaseFirestore db;

    RecyclerView familyHistoryRecycler;

    FamilyHistoryAdapter myAdapter;

    ArrayList<FamilyHistory> familyHistories;

    User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_family_history, container, false);

        user = FamilyHistoryFragmentArgs.fromBundle(getArguments()).getUser();

        initiateViews();

        return root;
    }



    private void initiateViews() {

        familyHistoryRecycler = root.findViewById(R.id.family_history_recyclerView);

        db = FirebaseFirestore.getInstance();

        getFamilyHistories();
    }

    public void getFamilyHistories(){
        db.collection("Users")
                .document(user.getId())
                .collection("Family Member")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (!queryDocumentSnapshots.isEmpty()){
                        familyHistories = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            FamilyHistory familyHistory = documentSnapshot.toObject(FamilyHistory.class);
                            familyHistory.setId(documentSnapshot.getId());
                            familyHistories.add(familyHistory);
                            //updateDoctorName(labTestRecord);
                        }

                        showRecycler(familyHistories);
                    }


                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show());

    }


    private void showRecycler(ArrayList<FamilyHistory> familyHistories) {
        myAdapter = new FamilyHistoryAdapter(familyHistories);

        familyHistoryRecycler.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));

        familyHistoryRecycler.setAdapter(myAdapter);
    }

    public void deleteAllergy(int i) {

        familyHistories.remove(i);
    }
}