package com.atta.medicalcoversp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atta.medicalcoversp.Allergy;
import com.atta.medicalcoversp.AllergyAdapter;
import com.atta.medicalcoversp.R;
import com.atta.medicalcoversp.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class AllergyFragment extends Fragment {

    View root;

    FirebaseFirestore db;

    RecyclerView allergyRecycler;

    AllergyAdapter myAdapter;

    ArrayList<Allergy> allergies;

    User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_allergy, container, false);


        user = AllergyFragmentArgs.fromBundle(getArguments()).getUser();

        initiateViews();

        return root;
    }


    private void initiateViews() {

        allergyRecycler = root.findViewById(R.id.allergy_recyclerView);

        db = FirebaseFirestore.getInstance();

        getAllergy();
    }


    public void getAllergy(){
        db.collection("Users")
                .document(user.getId())
                .collection("Allergy")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (!queryDocumentSnapshots.isEmpty()){
                        allergies = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            String allergy = documentSnapshot.getString("allergy");
                            allergies.add(new Allergy(documentSnapshot.getId(), allergy));
                        }

                        showRecycler(allergies);
                    }


                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show());

    }


    private void showRecycler(ArrayList<Allergy> allergies) {
        myAdapter = new AllergyAdapter(allergies);

        allergyRecycler.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));

        allergyRecycler.setAdapter(myAdapter);
    }

}