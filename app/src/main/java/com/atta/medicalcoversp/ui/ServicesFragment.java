package com.atta.medicalcoversp.ui;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atta.medicalcoversp.R;
import com.atta.medicalcoversp.ServicesAdapter;
import com.atta.medicalcoversp.TestCenter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServicesFragment extends Fragment {

    View root;

    FirebaseFirestore db;

    RecyclerView recyclerView;

    ServicesAdapter myAdapter;

    FloatingActionButton add;

    SearchView searchView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_services, container, false);


        recyclerView = root.findViewById(R.id.lab_radiology_recyclerView);
        searchView = root.findViewById(R.id.searchView);
        add = root.findViewById(R.id.floatingActionButton3);

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
        db = FirebaseFirestore.getInstance();

        getCenters();

        add.setOnClickListener(v ->
                showDialog()
        );
        return root;
    }

    private void showDialog() {

        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.new_service_layout);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialog.findViewById(R.id.close_img).setOnClickListener(view -> dialog.dismiss());

        dialog.findViewById(R.id.add_btn).setOnClickListener(view -> {

            String name = ((EditText) dialog.findViewById(R.id.nameText))
                    .getText().toString().trim();
            String phone = ((EditText) dialog.findViewById(R.id.phoneText))
                    .getText().toString().trim();

            Spinner typesSpinner = dialog.findViewById(R.id.types_spinner);

            ArrayList<String> types = new ArrayList<>();
            types.add("Radiology");
            types.add("Laboratory");

            ArrayAdapter<String> typesAdapter = new ArrayAdapter<>(
                    getContext(),
                    android.R.layout.simple_list_item_1,
                    types);

            typesSpinner.setAdapter(typesAdapter);

            final String[] type = new String[1];

            typesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    type[0] = types.get(i);

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    type[0] = types.get(0);
                }
            });

            if (name.isEmpty()){
                Toast.makeText(getContext(), "You must enter center name", Toast.LENGTH_SHORT).show();

            }else if (phone.isEmpty()){
                Toast.makeText(getContext(), "You must enter center phone", Toast.LENGTH_SHORT).show();

            }else {

                addService(name, phone, type[0]);

                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void addService(String name, String phone, String type) {
        Map<String, Object> pharmacy = new HashMap<>();
        pharmacy.put("name", name);
        pharmacy.put("phone", phone);
        pharmacy.put("type", type);

        db.collection("Services").add(pharmacy)
                .addOnSuccessListener(documentReference ->
                        Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show());
    }
    private void getCenters() {

        db.collection("Services")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()){

                        ArrayList<TestCenter> data = new ArrayList<>();

                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            TestCenter testCenter = documentSnapshot.toObject(TestCenter.class);
                            testCenter.setId(documentSnapshot.getId());
                            data.add(testCenter);

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

    private void showRecycler(ArrayList<TestCenter> data) {

        myAdapter = new ServicesAdapter(data);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        recyclerView.setAdapter(myAdapter);
    }
}