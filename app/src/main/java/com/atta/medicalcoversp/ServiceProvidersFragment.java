package com.atta.medicalcoversp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;

public class ServiceProvidersFragment extends Fragment {

    View root;

    FirebaseFirestore db;

    ArrayList<User> users;

    FloatingActionButton addBtn;

    RecyclerView usersRecycler;

    UsersAdapter myAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_service_providers, container, false);

        db = FirebaseFirestore.getInstance();

        initiateViews();

        return root;
    }


    private void initiateViews() {

        addBtn = root.findViewById(R.id.createUSerActionButton);

        addBtn.setOnClickListener(view ->
                Navigation.findNavController(view)
                .navigate(ServiceProvidersFragmentDirections
                        .actionServiceProvidersFragmentToNewAccountFragment())
        );

        usersRecycler = root.findViewById(R.id.usersRecycler);

        getUsers();
    }

    private void getUsers() {
        db.collection("Users")
                .whereNotIn("type", Arrays.asList(0 , 5))
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    users = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                        User user = documentSnapshot.toObject(User.class);
                        user.setId(documentSnapshot.getId());
                        users.add(user);
                        //updateDoctorName(labTestRecord);
                    }

                    showRecycler(users);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }


    private void showRecycler(ArrayList<User> users) {
        myAdapter = new UsersAdapter(users);

        usersRecycler.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));

        usersRecycler.setAdapter(myAdapter);
    }
}