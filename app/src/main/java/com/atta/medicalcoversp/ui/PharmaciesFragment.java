package com.atta.medicalcoversp.ui;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atta.medicalcoversp.PharmaciesAdapter;
import com.atta.medicalcoversp.Pharmacy;
import com.atta.medicalcoversp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PharmaciesFragment extends Fragment {


    View root;

    FirebaseFirestore db;

    RecyclerView recyclerView;

    PharmaciesAdapter myAdapter;

    FloatingActionButton add;

    SearchView searchView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_pharmacies, container, false);

        recyclerView = root.findViewById(R.id.pharmacies_recyclerView);
        searchView = root.findViewById(R.id.searchView);
        add = root.findViewById(R.id.floatingActionButton2);

        db = FirebaseFirestore.getInstance();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //myAdapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                myAdapter.getFilter().filter(s);
                return false;
            }
        });

        getPharmacies();


        add.setOnClickListener(v ->
                showDialog()
        );
        return root;
    }

    private void showDialog() {

        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.new_pharmacy_layout);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialog.findViewById(R.id.close_img).setOnClickListener(view -> dialog.dismiss());

        dialog.findViewById(R.id.add_btn).setOnClickListener(view -> {

            String name = ((EditText) dialog.findViewById(R.id.nameText))
                    .getText().toString().trim();
            String phone = ((EditText) dialog.findViewById(R.id.phoneText))
                    .getText().toString().trim();
            if (name.isEmpty()){
                Toast.makeText(getContext(), "You must enter pharmacy name", Toast.LENGTH_SHORT).show();

            }else if (phone.isEmpty()){
                Toast.makeText(getContext(), "You must enter pharmacy phone", Toast.LENGTH_SHORT).show();

            }else {

                addPharmacy(name, phone);

                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void addPharmacy(String name, String phone) {
        Map<String, Object> pharmacy = new HashMap<>();
        pharmacy.put("name", name);
        pharmacy.put("phone", phone);

        db.collection("Pharmacies").add(pharmacy)
                .addOnSuccessListener(documentReference ->
                        Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show());
    }


    private void getPharmacies() {

        db.collection("Pharmacies")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()){

                        ArrayList<Pharmacy> data = new ArrayList<>();

                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            Pharmacy pharmacy = documentSnapshot.toObject(Pharmacy.class);
                            pharmacy.setId(documentSnapshot.getId());
                            data.add(pharmacy);

                            //add(documentSnapshot.toObject(Specialty.class));
                        }

                        showRecycler(data);
                    }else {
                        Toast.makeText(getContext(), "something wrong, Please Try again later", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showRecycler(ArrayList<Pharmacy> data) {

        myAdapter = new PharmaciesAdapter(data);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        recyclerView.setAdapter(myAdapter);
    }


}