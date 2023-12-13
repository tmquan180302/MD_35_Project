package com.poly.hopa.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.poly.hopa.R;
import com.poly.hopa.models.Service;
import com.poly.hopa.ui.service.ServiceDetailActivity;

import java.util.List;

public class ServiceHomeAdapter extends RecyclerView.Adapter<ServiceHomeAdapter.ServiceHomeViewHolder> {
    private List<Service> mListServiceHome;
    private Context context;

    public ServiceHomeAdapter(List<Service> mListServiceHome, Context context) {
        this.mListServiceHome = mListServiceHome;
        this.context = context;
    }

    @NonNull
    @Override
    public ServiceHomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service_home, parent, false);
        return new ServiceHomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceHomeViewHolder holder, int position) {
        Service service = mListServiceHome.get(position);

        holder.tvNameServiceHome.setText(service.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ServiceDetailActivity.class);
                intent.putExtra("idService", service.getServiceID());
                v.getContext().startActivity(intent);

            }
        });
        List<String> image = service.getImage();
        Glide.with(context).load(image.get(4)).into(holder.imgServiceHome);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ServiceDetailActivity.class);
                intent.putExtra("idService", service.getServiceID());
                v.getContext().startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mListServiceHome.size();
    }

    public class ServiceHomeViewHolder extends RecyclerView.ViewHolder {

        private TextView tvNameServiceHome;
        private ImageView imgServiceHome;

        public ServiceHomeViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNameServiceHome = itemView.findViewById(R.id.tvNameServiceHome);

            imgServiceHome = itemView.findViewById(R.id.imgIconServiceHome);

        }
    }
}
