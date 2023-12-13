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
import com.poly.hopa.ui.service.ServiceDetailActivity;
import com.poly.hopa.R;
import com.poly.hopa.models.Service;

import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceAdapterViewHolder> {
    private List<Service> mListService;
    private Context context;


    public ServiceAdapter(List<Service> mListService, Context context) {
        this.mListService = mListService;
        this.context = context;
    }

    @NonNull
    @Override
    public ServiceAdapter.ServiceAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_search, parent, false);
        return new ServiceAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceAdapter.ServiceAdapterViewHolder holder, int position) {
        Service service = mListService.get(position);
        holder.name.setText(service.getName() + "");
        List<String> image = service.getImage();
        Glide.with(context).load(image.get(0)).into(holder.img);
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
        return mListService.size();
    }


    public class ServiceAdapterViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private ImageView img;
        private RecyclerView rcv;

        public ServiceAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            img = itemView.findViewById(R.id.img);
            rcv = itemView.findViewById(R.id.rcv);

        }
    }

}
