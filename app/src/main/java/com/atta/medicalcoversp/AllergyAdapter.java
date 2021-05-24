package com.atta.medicalcoversp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AllergyAdapter extends RecyclerView.Adapter<AllergyAdapter.MyViewHolder> {

    private final List<Allergy> allergies;


    public AllergyAdapter(ArrayList<Allergy> data) {

        this.allergies = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;


            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.allergy_item_layout, parent, false);



        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        final Allergy allergy = allergies.get(position) ;

        holder.allergyTitle.setText(allergy.getName());



    }

    @Override
    public int getItemCount() {
        return allergies.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView allergyTitle;


        MyViewHolder(View view) {
            super(view);
            allergyTitle = view.findViewById(R.id.allergy_title);

        }
    }


}
