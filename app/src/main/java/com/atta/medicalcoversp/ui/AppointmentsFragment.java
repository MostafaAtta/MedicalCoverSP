package com.atta.medicalcoversp.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.atta.medicalcoversp.Appointment;
import com.atta.medicalcoversp.AppointmentsAdapter;
import com.atta.medicalcoversp.R;
import com.atta.medicalcoversp.SessionManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;

public class AppointmentsFragment extends Fragment {

    View root;

    FirebaseFirestore db;

    private static final String TAG = "AppointmentFragment";

    RecyclerView recyclerView;

    SwipeRefreshLayout swipeRefreshLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_appointment, container, false);

        recyclerView = root.findViewById(R.id.appointments);
        swipeRefreshLayout = root.findViewById(R.id.swipe_refresh);


        db = FirebaseFirestore.getInstance();
        getAppointment();
        swipeRefreshLayout.setOnRefreshListener(() -> {
            getAppointment();
            swipeRefreshLayout.setRefreshing(false);
        });


        return root;
    }

    public void getAppointment(){
        db.collection("Appointments")
                .whereEqualTo("doctorId", SessionManager.getInstance(getContext()).getDoctorId())
                .whereIn("status", Arrays.asList("new", "confirmed","New", "Confirmed"))
                .orderBy("timestamp")
                .orderBy("timeSlot")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (!queryDocumentSnapshots.isEmpty()){
                        ArrayList<Appointment> appointments = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            Log.d(TAG, "Cached document data: " + documentSnapshot.getData());
                            Appointment appointment = documentSnapshot.toObject(Appointment.class);
                            appointment.setId(documentSnapshot.getId());
                            appointments.add(appointment);
                        }

                        showSlotsRecycler(appointments);
                    }


                })
                .addOnFailureListener(e -> {

                    Log.d(TAG, "Cached document data: " + e.getMessage());
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                });

    }

    private void showSlotsRecycler(ArrayList<Appointment> appointments) {
        AppointmentsAdapter myAdapter = new AppointmentsAdapter(appointments, getActivity());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));

        recyclerView.setAdapter(myAdapter);
    }
}