package com.atta.medicalcoversp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FamilyHistoryAdapter extends RecyclerView.Adapter<FamilyHistoryAdapter.MyViewHolder> {

    private final List<FamilyHistory> familyHistories;


    public FamilyHistoryAdapter(ArrayList<FamilyHistory> data) {

        this.familyHistories = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;


            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.family_history_item_layout, parent, false);



        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        final FamilyHistory familyHistory = familyHistories.get(position) ;

        holder.familyMember.setText(familyHistory.getMember());
        holder.description.setText(familyHistory.getDescription());

    }

    @Override
    public int getItemCount() {
        return familyHistories.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView familyMember, description;

        MyViewHolder(View view) {
            super(view);
            familyMember = view.findViewById(R.id.family_member);
            description = view.findViewById(R.id.description);

        }
    }


}
