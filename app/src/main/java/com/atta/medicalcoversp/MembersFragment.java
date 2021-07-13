package com.atta.medicalcoversp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MembersFragment extends Fragment {

    View root;

    FirebaseFirestore db;

    ArrayList<String> members;

    EditText membershipNumber;

    Button addBtn;

    RecyclerView membersRecycler;

    MembersAdapter myAdapter;

    String companyId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_members, container, false);

        db = FirebaseFirestore.getInstance();

        companyId = MembersFragmentArgs.fromBundle(getArguments()).getCompanyId();

        initiateViews();

        return root;
    }


    private void initiateViews() {

        membershipNumber = root.findViewById(R.id.membershipNumber);
        addBtn = root.findViewById(R.id.add_btn);

        addBtn.setOnClickListener(view ->
                addMember(membershipNumber.getText().toString().trim())
        );

        membersRecycler = root.findViewById(R.id.membersRecycler);

        getMembers();
    }

    private void addMember(String memberNo) {

        Map<String, Object> member = new HashMap<>();
        member.put("membershipNumber", memberNo);

        db.collection("Insurance Details")
                .document(companyId)
                .collection("members")
                .document()
                .set(member)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
                    if (members != null)
                        members.clear();
                    membershipNumber.setText("");
                    getMembers();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show()
                );

    }

    private void getMembers() {
        db.collection("Insurance Details")
                .document(companyId)
                .collection("members")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    members = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                        String member = documentSnapshot.getData().get("membershipNumber").toString();
                        members.add(member);
                        //updateDoctorName(labTestRecord);
                    }

                    showRecycler(members);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }


    private void showRecycler(ArrayList<String> members) {
        myAdapter = new MembersAdapter(members);

        membersRecycler.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));

        membersRecycler.setAdapter(myAdapter);
    }
}