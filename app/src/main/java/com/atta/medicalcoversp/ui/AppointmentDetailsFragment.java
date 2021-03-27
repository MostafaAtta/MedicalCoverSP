package com.atta.medicalcoversp.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.atta.medicalcoversp.Appointment;
import com.atta.medicalcoversp.R;
import com.atta.medicalcoversp.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AppointmentDetailsFragment extends Fragment implements View.OnClickListener {

    View root;

    Appointment appointment;

    TextView clinicName, dateTv, timeTv, statusTv, patientNameTv, ageTv, genderTv;

    Button confirmBtn, cancelBtn, addPrescription;

    boolean confirmed;

    String phoneNumber;

    FirebaseFirestore db;

    User patient;

    ImageView callImg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_appointment_details, container, false);

        appointment = AppointmentDetailsFragmentArgs.fromBundle(getArguments()).getAppointment();

        db = FirebaseFirestore.getInstance();
        initiateViews();

        return root;
    }

    private void initiateViews(){
        clinicName = root.findViewById(R.id.clinic_name_tv);
        dateTv = root.findViewById(R.id.date_tv);
        timeTv = root.findViewById(R.id.time_tv);
        statusTv = root.findViewById(R.id.status_tv);
        patientNameTv = root.findViewById(R.id.patient_name);
        ageTv = root.findViewById(R.id.age_tv);
        genderTv = root.findViewById(R.id.gender_tv);

        confirmBtn = root.findViewById(R.id.confirm_btn);
        confirmBtn.setOnClickListener(this);
        cancelBtn = root.findViewById(R.id.cancel_btn);

        callImg = root.findViewById(R.id.call_imag);

        addPrescription = root.findViewById(R.id.add_prescription);
        addPrescription.setOnClickListener(this);
        getUserDetails();

        if (appointment.getStatus().equalsIgnoreCase("confirmed")){
            confirmed = true;
            confirmBtn.setText("Finish");
        }else if (appointment.getStatus().equalsIgnoreCase("canceled") ||
                appointment.getStatus().equalsIgnoreCase("finished")){
            confirmed = false;
            confirmBtn.setVisibility(View.GONE);
            cancelBtn.setVisibility(View.GONE);
        }else {
            confirmed = false;
            confirmBtn.setVisibility(View.VISIBLE);
            cancelBtn.setVisibility(View.VISIBLE);
        }

        clinicName.setText(appointment.getClinicName());
        dateTv.setText(appointment.getDate());
        String timeSlot = appointment.getTimeSlot();
        String convertedTimeSlot;
        int hours = Integer.parseInt(Arrays.asList(timeSlot.split(":")).get(0));
        String min = Arrays.asList(timeSlot.split(":")).get(1);
        if (hours <= 12 && hours > 0){
            convertedTimeSlot = timeSlot + " AM";
        }else {
            hours -= 12;
            if (hours < 10){

                convertedTimeSlot = "0" + hours + ":" + min + " PM";
            }else {

                convertedTimeSlot = hours + ":" + min + " PM";
            }
        }

        timeTv.setText(convertedTimeSlot);
        statusTv.setText(appointment.getStatus());

        switch (appointment.getStatus()){
            case "new":
                statusTv.setTextColor(R.color.blue);
                break;
            case "confirmed":
            case "Finished":
                statusTv.setTextColor(R.color.green);
                break;
            case "canceled":
                statusTv.setTextColor(R.color.red);
                break;
            default:
                statusTv.setTextColor(R.color.black);
        }

    }

    private void getUserDetails() {
        db.collection("Users")
                .document(appointment.getUserId())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    patient = documentSnapshot.toObject(User.class);
                    patientNameTv.setText(patient.getFullName());

                    phoneNumber = patient.getPhone();
                    callImg.setOnClickListener(this::onClick);

                    genderTv.setText(patient.getGender());
                    calculateAge();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "An Error", Toast.LENGTH_SHORT).show();
                });
    }

    private void calculateAge() {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        Date date = sdf.parse(patient.getDateOfBirth(), new ParsePosition(0));

        dob.setTime(date);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        String patientAge = String.valueOf(age);

        ageTv.setText(patientAge + " Years");
    }

    @Override
    public void onClick(View view) {
        if (view == confirmBtn){
            if (!confirmed){
                Map<String, String> map = new HashMap<>();
                map.put("status", "Confirmed");
                db.collection("Appointments").document(appointment.getId())
                        .set(map, SetOptions.merge())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getContext(), "status updated", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Error, try again", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }else if (view == callImg){
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(intent);
        }else if (view == addPrescription){
            Navigation.findNavController(view)
                    .navigate(AppointmentDetailsFragmentDirections.actionNavigationAppointmentDetailsToNavigationNewPrescription(appointment, patient));

        }
    }
}