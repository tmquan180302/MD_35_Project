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
import com.poly.hopa.models.ServerResponse.ServerResRoomHome;
import com.poly.hopa.ui.room.RoomDetailActivity;

import java.util.List;

public class RoomRcmHomeAdapter extends RecyclerView.Adapter<RoomRcmHomeAdapter.RoomRcmHomeViewHolder> {
    private List<ServerResRoomHome> mListRoomHome;
    private Context context;

    public RoomRcmHomeAdapter(List<ServerResRoomHome> mListRoomHome, Context context) {
        this.mListRoomHome = mListRoomHome;
        this.context = context;
    }

    @NonNull
    @Override
    public RoomRcmHomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room_recomened_home, parent, false);
        return new RoomRcmHomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomRcmHomeViewHolder holder, int position) {
        ServerResRoomHome serverResRoomHome = mListRoomHome.get(position);

        holder.tvRcmRating.setText(String.valueOf(serverResRoomHome.getReview().getRating()));
        holder.tvRcmRoom.setText(serverResRoomHome.getRoom().getType());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RoomDetailActivity.class);
                intent.putExtra("idRoom", serverResRoomHome.getRoom().getRoomId());
                v.getContext().startActivity(intent);
            }
        });
        List<String> image = serverResRoomHome.getRoom().getImage();
        Glide.with(context).load(image.get(0)).into(holder.imgRcmHome);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RoomDetailActivity.class);
                intent.putExtra("idRoom", serverResRoomHome.getRoom().getRoomId());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListRoomHome.size();
    }

    public class RoomRcmHomeViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgRcmHome;
        private TextView tvRcmRating, tvRcmRoom;

        public RoomRcmHomeViewHolder(@NonNull View itemView) {
            super(itemView);

            imgRcmHome = itemView.findViewById(R.id.imgRoomRcmHome);
            tvRcmRating = itemView.findViewById(R.id.tvRatingRoomRcmHome);
            tvRcmRoom = itemView.findViewById(R.id.tvTypeRoomRcmHome);
        }
    }
}
