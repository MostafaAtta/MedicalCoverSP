package com.atta.medicalcoversp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BloodPressureAdapter extends RecyclerView.Adapter<BloodPressureAdapter.MyViewHolder> {

    private final List<BloodPressure> bloodPressures;


    public BloodPressureAdapter(ArrayList<BloodPressure> data) {

        this.bloodPressures = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;


            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.blood_pressure_item_layout, parent, false);



        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        final BloodPressure bloodPressure = bloodPressures.get(position) ;

        holder.bloodPressure.setText("Record: " + bloodPressure.getSystolic() + "/" + bloodPressure.getDiastolic());

        holder.pulse.setText("Pulse: " + bloodPressure.getPulse());

        Timestamp timestamp = bloodPressure.getTimestamp();

        Date date = timestamp.toDate();

        String pattern = "EEE, dd MMM hh:mm a";
        SimpleDateFormat format = new SimpleDateFormat(pattern, new Locale("en", "US"));
        holder.bloodPressureDate.setText(format.format(date));

    }

    @Override
    public int getItemCount() {
        return bloodPressures.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView bloodPressure, pulse, bloodPressureDate;


        MyViewHolder(View view) {
            super(view);
            bloodPressure = view.findViewById(R.id.blood_pressure);
            pulse = view.findViewById(R.id.pulse);
            bloodPressureDate = view.findViewById(R.id.blood_pressure_date);

        }
    }


}
