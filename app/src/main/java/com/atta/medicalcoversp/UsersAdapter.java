package com.atta.medicalcoversp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MyViewHolder> {

    private final List<User> users;


    public UsersAdapter(ArrayList<User> data) {

        this.users = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;


            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.user_item_layout, parent, false);



        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        final User user = users.get(position) ;

        holder.userName.setText(user.getFullName());

        switch (user.getType()){
            case 1:

                holder.type.setText("Doctor");
                break;
            case 2:

                holder.type.setText("Pharmacy");
                break;
            case 3:

                holder.type.setText("Laboratory");
                break;
            case 4:
                holder.type.setText("Radiology");
                break;
        }


        holder.itemView.setOnClickListener(v -> {
            /*Navigation.findNavController(v)
                    .navigate(CompaniesFragmentDirections
                            .actionCompaniesFragmentToMembersFragment(user.getId()));*/
        });

    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView userName, type;


        MyViewHolder(View view) {
            super(view);
            userName = view.findViewById(R.id.userName);
            type = view.findViewById(R.id.type);

        }
    }


}
