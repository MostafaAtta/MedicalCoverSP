package com.atta.medicalcoversp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CompaniesAdapter extends RecyclerView.Adapter<CompaniesAdapter.MyViewHolder> {

    private final List<Company> companies;


    public CompaniesAdapter(ArrayList<Company> data) {

        this.companies = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;


            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.company_item_layout, parent, false);



        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        final Company company = companies.get(position) ;

        holder.companyName.setText(company.getPolicyHolder());

        holder.policyName.setText(company.getPolicyNumber());

        holder.itemView.setOnClickListener(v -> {
            Navigation.findNavController(v)
                    .navigate(CompaniesFragmentDirections
                            .actionCompaniesFragmentToMembersFragment(company.getId()));
        });

    }

    @Override
    public int getItemCount() {
        return companies.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView companyName, policyName;


        MyViewHolder(View view) {
            super(view);
            companyName = view.findViewById(R.id.company_name);
            policyName = view.findViewById(R.id.policy_no);

        }
    }


}
