package com.atta.medicalcoversp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.MyViewHolder> {

    private final List<String> members;


    public MembersAdapter(ArrayList<String> data) {

        this.members = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;


            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.member_item_layout, parent, false);



        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        final String member = members.get(position) ;

        holder.membershipNumber.setText(member);

    }

    @Override
    public int getItemCount() {
        return members.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView membershipNumber;


        MyViewHolder(View view) {
            super(view);
            membershipNumber = view.findViewById(R.id.member);

        }
    }


}
