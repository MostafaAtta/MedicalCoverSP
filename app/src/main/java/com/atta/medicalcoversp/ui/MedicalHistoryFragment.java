package com.atta.medicalcoversp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.atta.medicalcoversp.R;
import com.atta.medicalcoversp.User;

public class MedicalHistoryFragment extends Fragment {

    View root;

    User user;

    TextView nameTxt;

    CardView labTestCard, radiologyCard, allergyCard, surgeryCard,
            vaccineCard, bloodPressureCard, bloodGlucoseCard, familyHistoryCard, requestsCard;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_medical_history, container, false);

        user = MedicalHistoryFragmentArgs.fromBundle(getArguments()).getUser();

        initiateViews();

        return root;
    }


    private void initiateViews(){

        labTestCard = root.findViewById(R.id.labTestsCard);
        labTestCard.setOnClickListener(view -> Navigation.findNavController(view)
                .navigate(MedicalHistoryFragmentDirections.actionMedicalHistoryFragmentToLabTestsFragment(user)));

        radiologyCard = root.findViewById(R.id.radiologyCard);
        radiologyCard.setOnClickListener(view -> Navigation.findNavController(view)
                .navigate(MedicalHistoryFragmentDirections.actionMedicalHistoryFragmentToRadiologyFragment(user)));

        allergyCard = root.findViewById(R.id.allergyCard);
        allergyCard.setOnClickListener(view -> Navigation.findNavController(view)
                .navigate(MedicalHistoryFragmentDirections.actionMedicalHistoryFragmentToAllergyFragment(user)));


        surgeryCard = root.findViewById(R.id.surgeryCard);
        surgeryCard.setOnClickListener(view -> Navigation.findNavController(view)
                .navigate(MedicalHistoryFragmentDirections.actionMedicalHistoryFragmentToSurgeryFragment(user)));


        vaccineCard = root.findViewById(R.id.vaccineCard);
        vaccineCard.setOnClickListener(view -> Navigation.findNavController(view)
                .navigate(MedicalHistoryFragmentDirections.actionMedicalHistoryFragmentToVaccineFragment(user)));


        bloodPressureCard = root.findViewById(R.id.bloodPressureCard);
        bloodPressureCard.setOnClickListener(view -> Navigation.findNavController(view)
                .navigate(MedicalHistoryFragmentDirections.actionMedicalHistoryFragmentToBloodPressureFragment(user)));


        bloodGlucoseCard = root.findViewById(R.id.bloodGlucoseCard);
        bloodGlucoseCard.setOnClickListener(view -> Navigation.findNavController(view)
                .navigate(MedicalHistoryFragmentDirections.actionMedicalHistoryFragmentToBloodGlucoseFragment(user)));


        familyHistoryCard = root.findViewById(R.id.familyHistoryCard);
        familyHistoryCard.setOnClickListener(view -> Navigation.findNavController(view)
                .navigate(MedicalHistoryFragmentDirections.actionMedicalHistoryFragmentToFamilyHistoryFragment(user)));



        nameTxt = root.findViewById(R.id.usernameTxt);
        nameTxt.setText(user.getFullName());

    }
}