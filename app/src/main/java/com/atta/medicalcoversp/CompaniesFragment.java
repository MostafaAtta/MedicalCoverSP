package com.atta.medicalcoversp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class CompaniesFragment extends Fragment {

    View root;
    ImageView logout;

    FirebaseFirestore db;

    ArrayList<Company> companies;


    RecyclerView companiesRecycler;

    CompaniesAdapter myAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_companies, container, false);

        db = FirebaseFirestore.getInstance();

        getCompanies();

        return root;
    }

    private void getCompanies() {
        db.collection("Insurance Details")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    companies = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                        Company company = documentSnapshot.toObject(Company.class);
                        company.setId(documentSnapshot.getId());
                        companies.add(company);
                        //updateDoctorName(labTestRecord);
                    }

                    showRecycler(companies);
                })
                .addOnFailureListener(e -> {

                });
    }


    private void showRecycler(ArrayList<Company> companies) {
        myAdapter = new CompaniesAdapter(companies);

        companiesRecycler.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));

        companiesRecycler.setAdapter(myAdapter);
    }

}