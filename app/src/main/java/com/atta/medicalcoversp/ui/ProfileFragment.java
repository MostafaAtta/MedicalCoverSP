package com.atta.medicalcoversp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.atta.medicalcoversp.R;
import com.atta.medicalcoversp.SessionManager;
import com.atta.medicalcoversp.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {

    View root;

    Button updateBtn;

    TextInputEditText emailText, nameText, phoneText;

    String fullName, phone;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private static final String TAG = "ProfileFragment";

    User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_profile, container, false);

        initiateViews();

        return root;
    }


    private void initiateViews() {

        db = FirebaseFirestore.getInstance();


        updateBtn = root.findViewById(R.id.updateBtn);
        updateBtn.setOnClickListener(v ->
            updateProfile()
        );

        emailText = root.findViewById(R.id.registerEmail);
        emailText.setEnabled(false);
        emailText.setFocusable(false);
        nameText = root.findViewById(R.id.registerName);
        phoneText = root.findViewById(R.id.registerPhone);


        getUser();
    }

    public boolean verify(){
        boolean valid = true;


        if (fullName.isEmpty() || fullName.equals("")){
            nameText.setError("Enter your Name");
            valid = false;
        }

        if (phone.isEmpty() || phone.equals("")){
            phoneText.setError("Enter your Phone");
            valid = false;
        } else if(phone.length() < 11){
            phoneText.setError("Enter a valid phone number");
            valid = false;
        }

        return valid;
    }

    private void updateProfile() {

        phone = phoneText.getText().toString().trim();
        fullName = nameText.getText().toString().trim();

        if(!verify()){
            return;
        }
        Map<String, Object> updatedUser = new HashMap<>();
        updatedUser.put("fullName", fullName);
        updatedUser.put("phone", phone);

        db.collection("Users")
                .document(SessionManager.getInstance(getContext()).getUserId())
                .update(updatedUser)
                .addOnSuccessListener(aVoid ->
                    Toast.makeText(getContext(), "Profile Updated", Toast.LENGTH_SHORT).show()
                ).addOnFailureListener(e ->
                    Toast.makeText(getContext(), "Something wrong , try again later", Toast.LENGTH_SHORT).show()

        );
    }

    private void getUser(){
        db.collection("Users")
                .document(SessionManager.getInstance(getContext()).getUserId())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    user = documentSnapshot.toObject(User.class);
                    user.setId(documentSnapshot.getId());
                    showUserDetails();

                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Something wrong , try again later", Toast.LENGTH_SHORT).show()
                );
    }

    private void showUserDetails() {
        nameText.setText(user.getFullName());
        emailText.setText(user.getEmail());
        phoneText.setText(user.getPhone());

    }


}