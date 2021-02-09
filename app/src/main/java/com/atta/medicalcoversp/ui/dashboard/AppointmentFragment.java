package com.atta.medicalcoversp.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.atta.medicalcoversp.R;
import com.atta.medicalcoversp.SessionManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class AppointmentFragment extends Fragment {

    View root;

    FirebaseFirestore db;

    private static final String TAG = "AppointmentFragment";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        db = FirebaseFirestore.getInstance();
        getAppointment();
        return root;
    }

    public void getAppointment(){
        db.collection("Appointments")
                .whereEqualTo("doctorId", SessionManager.getInstance(getContext()).getDoctorId())
                .orderBy("date")
                .orderBy("timeSlot")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()){
                            for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                                Log.d(TAG, "Cached document data: " + documentSnapshot.getData());


                            }

                            //showSlotsRecycler(morningSlotsRecyclerView, morningSlots);
                        }


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }
}