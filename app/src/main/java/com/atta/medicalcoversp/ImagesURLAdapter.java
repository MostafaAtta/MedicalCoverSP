package com.atta.medicalcoversp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.atta.medicalcoversp.ui.LabTestDetailsFragment;
import com.atta.medicalcoversp.ui.RadiologyDetailsFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImagesURLAdapter extends RecyclerView.Adapter<ImagesURLAdapter.MyViewHolder> {

    private Context mContext;
    private List<String> images;
    private RadiologyDetailsFragment radiologyDetailsFragment;
    private LabTestDetailsFragment labTestDetailsFragment;

    public ImagesURLAdapter(Context mContext, List<String> images,
                            RadiologyDetailsFragment radiologyDetailsFragment,
                            LabTestDetailsFragment labTestDetailsFragment) {
        this.mContext = mContext;
        this.images = images;
        this.radiologyDetailsFragment = radiologyDetailsFragment;
        this.labTestDetailsFragment = labTestDetailsFragment;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.images_list_item, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {

        final String image = images.get(i) ;

        Picasso.get()
                .load(image)
                .resize(120, 120)
                .centerCrop()
                .into(holder.imageView);

        if (labTestDetailsFragment != null) {
            holder.imageView.setOnClickListener(v -> {
                labTestDetailsFragment.zoomImageFromThumb(image);
            });
        }else if (radiologyDetailsFragment != null){
            holder.imageView.setOnClickListener(v -> {
                radiologyDetailsFragment.zoomImageFromThumb(image);
            });
        }

        holder.closeImg.setVisibility(View.GONE);

    }


    @Override
    public int getItemCount() {
        return images.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView, closeImg;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.images_recycler_item);
            closeImg = itemView.findViewById(R.id.close_img);
        }
    }
}
