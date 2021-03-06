package com.atta.medicalcoversp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CompaniesFragment extends Fragment {

    View root;

    FirebaseFirestore db;

    ArrayList<Company> companies;

    EditText companyName, policyNumber;

    Button addBtn;

    RecyclerView companiesRecycler;

    CompaniesAdapter myAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_companies, container, false);

        db = FirebaseFirestore.getInstance();

        initiateViews();

        return root;
    }

    private void initiateViews() {

        companyName = root.findViewById(R.id.companyName);
        policyNumber = root.findViewById(R.id.policyNumber);
        addBtn = root.findViewById(R.id.add_company_btn);

        addBtn.setOnClickListener(view ->
                addCompany(companyName.getText().toString().trim(),
                        policyNumber.getText().toString().trim())
        );

        companiesRecycler = root.findViewById(R.id.companiesRecycler);

        getCompanies();
    }

    private void addCompany(String name, String policyNo) {

        Map<String, Object> company = new HashMap<>();
        company.put("policyHolder", name);
        company.put("policyNumber", policyNo);

        db.collection("Insurance Details")
                .document()
                .set(company)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
                    if (companies != null)
                        companies.clear();
                    companyName.setText("");
                    policyNumber.setText("");
                    getCompanies();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show()
                );

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
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }


    private void showRecycler(ArrayList<Company> companies) {
        myAdapter = new CompaniesAdapter(companies);

        companiesRecycler.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));

        companiesRecycler.setAdapter(myAdapter);
    }

}