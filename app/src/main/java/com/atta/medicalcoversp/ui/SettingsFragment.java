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

public class SettingsFragment extends Fragment {

    View root;
    Button logout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_notifications, container, false);

        logout = root.findViewById(R.id.logout);

        logout.setOnClickListener(view -> {

            SessionManager.getInstance(getContext()).logout();
            Intent intent = new Intent(getContext(), LoginActivity.class);

            // Closing all the Activities
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().startActivity(intent);
        });
        return root;
    }
}