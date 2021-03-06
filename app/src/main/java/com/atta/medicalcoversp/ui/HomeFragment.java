package com.atta.medicalcoversp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atta.medicalcoversp.MedicationRequest;
import com.atta.medicalcoversp.MedicationRequestsAdapter;
import com.atta.medicalcoversp.R;
import com.atta.medicalcoversp.SessionManager;
import com.atta.medicalcoversp.TestRequest;
import com.atta.medicalcoversp.TestRequestsAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {

    View root;
    int userType;

    FirebaseFirestore db;

    private static final String TAG = "HomeFragment";

    TextView filterTxt, labRequestsTv, medicationRequestsTv, radiologyRequestsTv;
    ImageView filterImg;
    RecyclerView labRequestsRecycler, medicationRequestsRecycler, radiologyRequestsRecycler;
    SwitchCompat switchCompat;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_home, container, false);

        initiateViews();

        return root;
    }

    private void initiateViews() {
        medicationRequestsRecycler = root.findViewById(R.id.medication_requests_recyclerView);
        labRequestsRecycler = root.findViewById(R.id.labs_requests_recyclerView);
        radiologyRequestsRecycler = root.findViewById(R.id.radiology_requests_recyclerView);
        labRequestsTv = root.findViewById(R.id.labs_requests_tv);
        medicationRequestsTv = root.findViewById(R.id.medication_requests_tv);
        radiologyRequestsTv = root.findViewById(R.id.radiology_requests_tv);

        filterImg = root.findViewById(R.id.filterImg);
        filterTxt = root.findViewById(R.id.filterTxt);

        db = FirebaseFirestore.getInstance();

        userType = SessionManager.getInstance(getContext()).getType();

        switchCompat = root.findViewById(R.id.switch1);
        switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                switchCompat.setText(getString(R.string.new_requests));
            }else {
                switchCompat.setText(getString(R.string.old_requests));
            }
            getRequests(isChecked);
        });

        getRequests(true);

        filterImg.setOnClickListener(v -> showFilterPopup());
    }

    private void getRequests(boolean newRequests) {
        switch (userType){
            case 0:
                if (newRequests) {
                    showAllRequests();
                }else {
                    showAllOldRequests();
                }
                break;

            case 2:
                filterImg.setVisibility(View.GONE);
                filterTxt.setVisibility(View.GONE);
                labRequestsRecycler.setVisibility(View.GONE);
                labRequestsTv.setVisibility(View.GONE);
                radiologyRequestsRecycler.setVisibility(View.GONE);
                radiologyRequestsTv.setVisibility(View.GONE);

                if (newRequests) {
                    showPharmacyRequests();
                }else {
                    showPharmacyOldRequests();
                }
                break;
            case 3:

                filterImg.setVisibility(View.GONE);
                filterTxt.setVisibility(View.GONE);
                medicationRequestsRecycler.setVisibility(View.GONE);
                medicationRequestsTv.setVisibility(View.GONE);
                radiologyRequestsRecycler.setVisibility(View.GONE);
                radiologyRequestsTv.setVisibility(View.GONE);

                if (newRequests) {
                    showLabRequests();
                }else {
                    showLabOldRequests();
                }
                break;

            case 4:

                filterImg.setVisibility(View.GONE);
                filterTxt.setVisibility(View.GONE);
                labRequestsRecycler.setVisibility(View.GONE);
                labRequestsTv.setVisibility(View.GONE);
                medicationRequestsRecycler.setVisibility(View.GONE);
                medicationRequestsTv.setVisibility(View.GONE);

                if (newRequests) {
                    showRadiologyRequests();
                }else {
                    showRadiologyOldRequests();
                }
                break;
        }
    }


    private void showFilterPopup() {

    }

    private void showAllRequests() {
        db.collection("Test Requests")
                .whereEqualTo("status", "pending approval")
                .orderBy("timestamp")
                .whereEqualTo("type", "Laboratory")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (!queryDocumentSnapshots.isEmpty()){
                        ArrayList<TestRequest> testRequests = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            TestRequest testRequest = documentSnapshot.toObject(TestRequest.class);
                            testRequest.setId(documentSnapshot.getId());
                            testRequests.add(testRequest);
                            //update(testRequest);
                        }

                        showLabRecycler(testRequests);
                    }else {

                        labRequestsRecycler.setVisibility(View.GONE);
                        labRequestsTv.setVisibility(View.GONE);
                    }
                })
                .addOnFailureListener(e -> {

                });

        db.collection("Test Requests")
                .whereEqualTo("status", "pending approval")
                .orderBy("timestamp")
                .whereEqualTo("type", "Radiology")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (!queryDocumentSnapshots.isEmpty()){
                        ArrayList<TestRequest> testRequests = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            TestRequest testRequest = documentSnapshot.toObject(TestRequest.class);
                            testRequest.setId(documentSnapshot.getId());
                            testRequests.add(testRequest);
                            //updateDoctorName(labTestRecord);
                        }

                        showRadiologyRecycler(testRequests);
                    }else {

                        radiologyRequestsRecycler.setVisibility(View.GONE);
                        radiologyRequestsTv.setVisibility(View.GONE);
                    }
                })
                .addOnFailureListener(e -> {

                });

        db.collection("Medications Requests")
                .whereEqualTo("status", "pending approval")
                .orderBy("timestamp")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (!queryDocumentSnapshots.isEmpty()){
                        ArrayList<MedicationRequest> medicationRequests = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            MedicationRequest medicationRequest = documentSnapshot.toObject(MedicationRequest.class);
                            medicationRequest.setId(documentSnapshot.getId());
                            medicationRequests.add(medicationRequest);
                            //update(medicationRequest);
                        }

                        showPharmacyRecycler(medicationRequests);
                    }else {

                        medicationRequestsRecycler.setVisibility(View.GONE);
                        medicationRequestsTv.setVisibility(View.GONE);
                    }
                })
                .addOnFailureListener(e -> {

                });
    }

    private void showAllOldRequests() {
        db.collection("Test Requests")
                //.whereNotEqualTo("status", "pending approval")
                .orderBy("timestamp")
                .whereEqualTo("type", "Laboratory")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (!queryDocumentSnapshots.isEmpty()){
                        ArrayList<TestRequest> testRequests = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            TestRequest testRequest = documentSnapshot.toObject(TestRequest.class);
                            if (!testRequest.getStatus().equalsIgnoreCase("pending approval")){

                                testRequest.setId(documentSnapshot.getId());
                                testRequests.add(testRequest);
                            }
                            //update(testRequest);
                        }

                        showLabRecycler(testRequests);
                    }else {

                        labRequestsRecycler.setVisibility(View.GONE);
                        labRequestsTv.setVisibility(View.GONE);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
                });

        db.collection("Test Requests")
                //.whereNotEqualTo("status", "pending approval")
                .orderBy("timestamp")
                .whereEqualTo("type", "Radiology")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (!queryDocumentSnapshots.isEmpty()){
                        ArrayList<TestRequest> testRequests = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            TestRequest testRequest = documentSnapshot.toObject(TestRequest.class);
                            if (!testRequest.getStatus().equalsIgnoreCase("pending approval")){

                                testRequest.setId(documentSnapshot.getId());
                                testRequests.add(testRequest);
                            }
                            //updateDoctorName(labTestRecord);
                        }

                        showRadiologyRecycler(testRequests);
                    }else {

                        radiologyRequestsRecycler.setVisibility(View.GONE);
                        radiologyRequestsTv.setVisibility(View.GONE);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();

                });

        db.collection("Medications Requests")
                //.whereNotEqualTo("status", "pending approval")
                .orderBy("timestamp")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (!queryDocumentSnapshots.isEmpty()){
                        ArrayList<MedicationRequest> medicationRequests = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            MedicationRequest medicationRequest = documentSnapshot.toObject(MedicationRequest.class);
                            if (!medicationRequest.getStatus().equalsIgnoreCase("pending approval")) {

                                medicationRequest.setId(documentSnapshot.getId());
                                medicationRequests.add(medicationRequest);
                            }
                            //update(medicationRequest);
                        }

                        showPharmacyRecycler(medicationRequests);
                    }else {

                        medicationRequestsRecycler.setVisibility(View.GONE);
                        medicationRequestsTv.setVisibility(View.GONE);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();

                });
    }

    private void update(MedicationRequest medicationRequest) {

        Map<String, Object> req = new HashMap<>();
        req.put("patientName", "Mostafa Atta");
        req.put("pharmacyName", "Seif Pharmacy");
        db.collection("Medications Requests")
                .document(medicationRequest.getId())
                .update(req)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
    }

    private void showLabRequests() {
        db.collection("Test Requests")
                .whereEqualTo("centerId", SessionManager.getInstance(getContext()).getCenterId())
                //.whereNotEqualTo("status", "Finished")
                .orderBy("timestamp")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (!queryDocumentSnapshots.isEmpty()){
                        ArrayList<TestRequest> testRequests = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            TestRequest testRequest = documentSnapshot.toObject(TestRequest.class);
                            if (!testRequest.getStatus().equalsIgnoreCase("Finished") &&
                                    !testRequest.getStatus().equalsIgnoreCase("canceled") ){
                                testRequest.setId(documentSnapshot.getId());
                                testRequests.add(testRequest);
                            }
                            //updateDoctorName(labTestRecord);
                        }

                        showLabRecycler(testRequests);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                });
    }

    private void showLabOldRequests() {
        db.collection("Test Requests")
                .whereEqualTo("centerId", SessionManager.getInstance(getContext()).getCenterId())
                //.whereNotEqualTo("status", "Finished")
                .orderBy("timestamp")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (!queryDocumentSnapshots.isEmpty()){
                        ArrayList<TestRequest> testRequests = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            TestRequest testRequest = documentSnapshot.toObject(TestRequest.class);
                            if (testRequest.getStatus().equalsIgnoreCase("Finished") ||
                                    testRequest.getStatus().equalsIgnoreCase("canceled") ){
                                testRequest.setId(documentSnapshot.getId());
                                testRequests.add(testRequest);
                            }
                            //updateDoctorName(labTestRecord);
                        }

                        showLabRecycler(testRequests);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                });
    }

    private void showRadiologyRequests() {
        db.collection("Test Requests")
                .whereEqualTo("centerId", SessionManager.getInstance(getContext()).getCenterId())
                //.whereNotEqualTo("status", "Finished")
                .orderBy("timestamp")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (!queryDocumentSnapshots.isEmpty()){
                        ArrayList<TestRequest> testRequests = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            TestRequest testRequest = documentSnapshot.toObject(TestRequest.class);
                            if (!testRequest.getStatus().equalsIgnoreCase("Finished") &&
                                    !testRequest.getStatus().equalsIgnoreCase("canceled") ) {
                                testRequest.setId(documentSnapshot.getId());
                                testRequests.add(testRequest);
                            }
                            //updateDoctorName(labTestRecord);
                        }

                        showRadiologyRecycler(testRequests);
                    }
                })
                .addOnFailureListener(e -> {

                });
    }

    private void showRadiologyOldRequests() {
        db.collection("Test Requests")
                .whereEqualTo("centerId", SessionManager.getInstance(getContext()).getCenterId())
                //.whereNotEqualTo("status", "Finished")
                .orderBy("timestamp")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    if (!queryDocumentSnapshots.isEmpty()){
                        ArrayList<TestRequest> testRequests = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            TestRequest testRequest = documentSnapshot.toObject(TestRequest.class);
                            if (testRequest.getStatus().equalsIgnoreCase("Finished") &&
                                    testRequest.getStatus().equalsIgnoreCase("canceled") ) {
                                testRequest.setId(documentSnapshot.getId());
                                testRequests.add(testRequest);
                            }
                            //updateDoctorName(labTestRecord);
                        }

                        showRadiologyRecycler(testRequests);
                    }
                })
                .addOnFailureListener(e -> {

                });
    }

    private void showPharmacyRequests() {
        db.collection("Medications Requests")
                .whereEqualTo("pharmacyId", SessionManager.getInstance(getContext()).getPharmacyId())
                .orderBy("timestamp")
                //.whereNotEqualTo("status", "Finished")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()){
                        ArrayList<MedicationRequest> medicationRequests = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            MedicationRequest medicationRequest = documentSnapshot.toObject(MedicationRequest.class);

                            if (!medicationRequest.getStatus().equalsIgnoreCase("Finished") &&
                                    !medicationRequest.getStatus().equalsIgnoreCase("canceled") ) {
                                medicationRequest.setId(documentSnapshot.getId());
                                medicationRequests.add(medicationRequest);
                            }
                            //updateDoctorName(labTestRecord);
                        }

                        showPharmacyRecycler(medicationRequests);
                    }
                })
                .addOnFailureListener(e -> {

                });
    }
    private void showPharmacyOldRequests() {
        db.collection("Medications Requests")
                .whereEqualTo("pharmacyId", SessionManager.getInstance(getContext()).getPharmacyId())
                .orderBy("timestamp")
                //.whereNotEqualTo("status", "Finished")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()){
                        ArrayList<MedicationRequest> medicationRequests = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            MedicationRequest medicationRequest = documentSnapshot.toObject(MedicationRequest.class);

                            if (medicationRequest.getStatus().equalsIgnoreCase("Finished") &&
                                    medicationRequest.getStatus().equalsIgnoreCase("canceled") ) {
                                medicationRequest.setId(documentSnapshot.getId());
                                medicationRequests.add(medicationRequest);
                            }
                            //updateDoctorName(labTestRecord);
                        }

                        showPharmacyRecycler(medicationRequests);
                    }
                })
                .addOnFailureListener(e -> {

                });
    }


    private void showPharmacyRecycler(ArrayList<MedicationRequest> medicationRequests) {
        medicationRequestsRecycler.setVisibility(View.VISIBLE);
        medicationRequestsTv.setVisibility(View.VISIBLE);
        MedicationRequestsAdapter myAdapter = new MedicationRequestsAdapter(medicationRequests, getActivity());

        medicationRequestsRecycler.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));

        medicationRequestsRecycler.setAdapter(myAdapter);
    }


    private void showLabRecycler(ArrayList<TestRequest> testRequests) {
        labRequestsRecycler.setVisibility(View.VISIBLE);
        labRequestsTv.setVisibility(View.VISIBLE);
        TestRequestsAdapter myAdapter = new TestRequestsAdapter(testRequests, getActivity());

        labRequestsRecycler.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));

        labRequestsRecycler.setAdapter(myAdapter);
    }

    private void showRadiologyRecycler(ArrayList<TestRequest> testRequests) {

        radiologyRequestsRecycler.setVisibility(View.VISIBLE);
        radiologyRequestsTv.setVisibility(View.VISIBLE);

        TestRequestsAdapter myAdapter = new TestRequestsAdapter(testRequests, getActivity());

        radiologyRequestsRecycler.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));

        radiologyRequestsRecycler.setAdapter(myAdapter);
    }
}