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

public class BloodGlucoseAdapter extends RecyclerView.Adapter<BloodGlucoseAdapter.MyViewHolder> {

    private final List<BloodGlucose> bloodGlucoseRecords;


    public BloodGlucoseAdapter(ArrayList<BloodGlucose> data) {

        this.bloodGlucoseRecords = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;


            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.blood_glucose_item_layout, parent, false);



        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        final BloodGlucose bloodGlucose = bloodGlucoseRecords.get(position) ;

        holder.bloodGlucoseResult.setText("Record: " + bloodGlucose.getResult());

        holder.type.setText("Type: " + bloodGlucose.getType());

        Timestamp timestamp = bloodGlucose.getTimestamp();

        Date date = timestamp.toDate();

        String pattern = "EEE, dd MMM hh:mm a";
        SimpleDateFormat format = new SimpleDateFormat(pattern, new Locale("en", "US"));
        holder.bloodPressureDate.setText(format.format(date));

    }

    @Override
    public int getItemCount() {
        return bloodGlucoseRecords.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView type, bloodGlucoseResult, bloodPressureDate;

        MyViewHolder(View view) {
            super(view);
            type = view.findViewById(R.id.type);
            bloodPressureDate = view.findViewById(R.id.blood_glucose_date);
            bloodGlucoseResult = view.findViewById(R.id.blood_glucose);

        }
    }


}
