package com.atta.medicalcoversp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ClinicsAdapter extends RecyclerView.Adapter<ClinicsAdapter.MyViewHolder> {

    private List<Clinic> clinics;
    private Doctor doctor;

    public ClinicsAdapter(ArrayList<Clinic> data, Doctor doctor) {

        this.clinics = data;
        this.doctor = doctor;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.clinic_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        final Clinic clinic = clinics.get(position);

        final String name = clinic.getName();
        int availableAfter = clinic.getAvailableAfter();

        holder.clinicName.setText(name);

        switch (availableAfter){
            case 0:
                holder.availableAt.setText("Available Today");
                break;
            case 1:
                holder.availableAt.setText("Available Tomorrow");
                break;
            default:
                Calendar c = Calendar.getInstance();
                c.add(Calendar.DATE, availableAfter);
                Date date = c.getTime();
                String pattern = "EEE, dd MMM";
                SimpleDateFormat format = new SimpleDateFormat(pattern, new Locale("en", "US"));
                holder.availableAt.setText("Available " + format.format(date));
        }




        holder.itemView.setOnClickListener(view ->{
            /*Navigation.findNavController(view)
                    .navigate(DoctorsFragmentDirections.actionDoctorsFragmentToBookingFragment(clinic, doctor));
        */
        });





    }

    @Override
    public int getItemCount() {
        return clinics.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView clinicName, availableAt;

        MyViewHolder(View view) {
            super(view);
            clinicName = view.findViewById(R.id.name_tv);
            availableAt = view.findViewById(R.id.available_tv);

        }
    }


}
