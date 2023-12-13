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
import com.poly.hopa.models.Booking;
import com.poly.hopa.ui.review.ReviewActivity;

import java.util.List;

public class HistoryServiceAdapter extends RecyclerView.Adapter<HistoryServiceAdapter.HistoryServiceAdapterViewHolder> {

    private List<Booking> bookingList;
    private Context context;

    public HistoryServiceAdapter(List<Booking> bookingList, Context context) {
        this.bookingList = bookingList;
        this.context = context;
    }

    @NonNull
    @Override
    public HistoryServiceAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_service, parent, false);
        return new HistoryServiceAdapter.HistoryServiceAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryServiceAdapterViewHolder holder, int position) {
        Booking booking = bookingList.get(position);

        List<String> image = booking.getService().getImage();
        Glide.with(context).load(image.get(0)).into(holder.imgService);

        holder.tvNameService.setText(booking.getService().getName());
        holder.tvPrice.setText(String.valueOf(booking.getService().getPrice()));
        holder.tvTotalPrice.setText(String.valueOf(booking.getTotalPrice()));
        holder.tvDay.setText(booking.getCheckInDate() +" - " +booking.getCheckOutDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(), ReviewActivity.class);
                intent.putExtra("idService", booking.getService().getServiceID());
                intent.putExtra("imageService", booking.getService().getImage().get(0));
                v.getContext().startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public static class HistoryServiceAdapterViewHolder extends RecyclerView.ViewHolder {

        ImageView imgService;
        TextView tvNameService, tvPrice, tvTotalPrice, tvDay;
        public HistoryServiceAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            imgService = itemView.findViewById(R.id.imgServiceHistory);
            tvNameService = itemView.findViewById(R.id.tvNameServiceHistory );
            tvPrice = itemView.findViewById(R.id.tvPriceServiceHistory);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalServicePriceHistory);
            tvDay = itemView.findViewById(R.id.tvNightServiceHistory);


        }
    }
}
