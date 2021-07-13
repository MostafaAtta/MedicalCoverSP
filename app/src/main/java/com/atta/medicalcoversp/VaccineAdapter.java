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

public class VaccineAdapter extends RecyclerView.Adapter<VaccineAdapter.MyViewHolder> {

    private final List<Vaccine> vaccines;


    public VaccineAdapter(ArrayList<Vaccine> data) {

        this.vaccines = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;


            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.vaccine_item_layout, parent, false);



        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        final Vaccine vaccine = vaccines.get(position) ;

        holder.vaccineTitle.setText(vaccine.getName());

        Timestamp timestamp = vaccine.getTimestamp();

        Date date = timestamp.toDate();

        String pattern = "EEE, dd MMM";
        SimpleDateFormat format = new SimpleDateFormat(pattern, new Locale("en", "US"));
        holder.vaccineDate.setText(format.format(date));

    }

    @Override
    public int getItemCount() {
        return vaccines.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView vaccineTitle, vaccineDate;


        MyViewHolder(View view) {
            super(view);
            vaccineTitle = view.findViewById(R.id.vaccineTitle);
            vaccineDate = view.findViewById(R.id.vaccineDate);

        }
    }


}
