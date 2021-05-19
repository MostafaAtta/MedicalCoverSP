package com.atta.medicalcoversp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.atta.medicalcoversp.ui.AppointmentsFragmentDirections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AppointmentsAdapter extends RecyclerView.Adapter<AppointmentsAdapter.MyViewHolder> {

    private List<Appointment> appointments;
    private Activity activity;

    public AppointmentsAdapter(ArrayList<Appointment> data, Activity activity) {

        this.appointments = data;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.appointment_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        final Appointment appointment = appointments.get(position);

        final String clinicName = appointment.getClinicName();
        String date = appointment.getDate();
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
        String status = appointment.getStatus();

        holder.clinicName.setText(clinicName);
        holder.dateTv.setText(date);
        holder.timeTv.setText(convertedTimeSlot);
        holder.statusTv.setText(status);

        switch (status){
            case "new":
                holder.statusTv.setTextColor(activity.getResources().getColor(R.color.blue));
                break;
            case "confirmed":
            case "Finished":
                holder.statusTv.setTextColor(activity.getResources().getColor(R.color.green));
                break;
            case "canceled":
                holder.statusTv.setTextColor(activity.getResources().getColor(R.color.red));
                break;
            default:
                holder.statusTv.setTextColor(activity.getResources().getColor(R.color.black));
        }




        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Navigation.findNavController(activity, R.id.nav_host_fragment)
                        .navigate(AppointmentsFragmentDirections.actionNavigationAppointmentToNavigationAppointmentDetails(appointment));
            }
        });



    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView clinicName, dateTv, timeTv, statusTv;

        MyViewHolder(View view) {
            super(view);
            clinicName = view.findViewById(R.id.patient_name_tv);
            dateTv = view.findViewById(R.id.date_tv);
            timeTv = view.findViewById(R.id.time_tv);
            statusTv = view.findViewById(R.id.status_tv);

        }
    }


}
