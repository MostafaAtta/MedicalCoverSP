package com.atta.medicalcoversp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.atta.medicalcoversp.LoginActivity;
import com.atta.medicalcoversp.R;
import com.atta.medicalcoversp.SessionManager;
import com.atta.medicalcoversp.User;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class SettingsFragment extends Fragment {

    View root;
    ImageView logout;

    FirebaseFirestore db;

    CardView profileCard;

    TextView nameTxt, typeTxt;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_settings, container, false);

        db = FirebaseFirestore.getInstance();

        logout = root.findViewById(R.id.logoutImg);

        logout.setOnClickListener(view -> {

            checkToken(SessionManager.getInstance(getContext()).getToken());
        });


        profileCard = root.findViewById(R.id.profileCard);
        profileCard.setOnClickListener(view -> Navigation.findNavController(view)
                .navigate(SettingsFragmentDirections.actionNavigationSettingsToProfileFragment()));
        nameTxt = root.findViewById(R.id.usernameTxt);
        nameTxt.setText(SessionManager.getInstance(getContext()).getUsername());

        typeTxt = root.findViewById(R.id.typeTxt);
        int userType = SessionManager.getInstance(getContext()).getType();
        switch (userType){
            case 0:

                typeTxt.setText("Admin");
                break;
            case 1:

                typeTxt.setText("Doctor");

                break;
            case 2:

                typeTxt.setText("Pharmacy");

                break;
            case 3:

                typeTxt.setText("Lab");

                break;

            case 4:

                typeTxt.setText("Radiology");

                break;
        }


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