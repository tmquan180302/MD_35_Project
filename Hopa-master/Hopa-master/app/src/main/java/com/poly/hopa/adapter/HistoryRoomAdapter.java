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

public class HistoryRoomAdapter extends RecyclerView.Adapter<HistoryRoomAdapter.HistoryRoomAdapterViewHolder> {

    private List<Booking> bookingList;
    private Context context;

    public HistoryRoomAdapter(List<Booking> bookingList, Context context) {
        this.bookingList = bookingList;
        this.context = context;
    }

    @NonNull
    @Override
    public HistoryRoomAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_room, parent, false);
        return new HistoryRoomAdapter.HistoryRoomAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryRoomAdapterViewHolder holder, int position) {
        Booking booking = bookingList.get(position);

        List<String> image = booking.getRoom().getImage();
        Glide.with(context).load(image.get(0)).into(holder.imgRoom);

        holder.tvNameRoom.setText(booking.getRoom().getType());
        holder.tvPrice.setText(String.valueOf(booking.getRoom().getPrice())+ " VNĐ");
        holder.tvTotalPrice.setText("Total: " +String.valueOf(booking.getTotalPrice()));
        holder.tvDay.setText(booking.getCheckInDate() +" - " +booking.getCheckOutDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(),ReviewActivity.class);
                intent.putExtra("idRoom", booking.getRoom().getRoomId());
                intent.putExtra("imageRoom", booking.getRoom().getImage().get(0));
                v.getContext().startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public static class HistoryRoomAdapterViewHolder extends RecyclerView.ViewHolder {

        ImageView imgRoom;
        TextView tvNameRoom, tvPrice, tvTotalPrice, tvDay;
        public HistoryRoomAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            imgRoom = itemView.findViewById(R.id.imgRoomHistory);
            tvNameRoom = itemView.findViewById(R.id.tvTypeRoomHistory );
            tvPrice = itemView.findViewById(R.id.tvPriceRoomHistory);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalRoomPriceHistory);
            tvDay = itemView.findViewById(R.id.tvNightRoomHistory);


        }
    }
}
