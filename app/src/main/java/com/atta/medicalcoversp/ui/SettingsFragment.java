package com.atta.medicalcoversp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.atta.medicalcoversp.LoginActivity;
import com.atta.medicalcoversp.R;
import com.atta.medicalcoversp.SessionManager;
import com.atta.medicalcoversp.User;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class SettingsFragment extends Fragment {

    View root;
    Button logout;

    FirebaseFirestore db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_notifications, container, false);

        db = FirebaseFirestore.getInstance();

        logout = root.findViewById(R.id.logout);

        logout.setOnClickListener(view -> {

            checkToken(SessionManager.getInstance(getContext()).getToken());
        });
        return root;
    }

    private void updateTokens(ArrayList<String> tokens) {

        db.collection("Users")
                .document(SessionManager.getInstance(getContext()).getUserId())
                .update("tokens", tokens)
                .addOnSuccessListener(aVoid -> {

                    SessionManager.getInstance(getContext()).logout();
                    Intent intent = new Intent(getContext(), LoginActivity.class);

                    // Closing all the Activities
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    // Add new Flag to start new Activity
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getActivity().startActivity(intent);
                });
    }


    public void checkToken(String token){
        db.collection("Users")
                .document(SessionManager.getInstance(getContext()).getUserId())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    User user = documentSnapshot.toObject(User.class);
                    ArrayList<String> tokens = user.getTokens();
                    if (tokens != null) {
                        if (tokens.contains(token)) {

                            tokens.remove(token);

                            updateTokens(tokens);
                        }
                    }
                });
    }
}