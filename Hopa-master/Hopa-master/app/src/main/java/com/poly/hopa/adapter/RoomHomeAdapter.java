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

public class RoomHomeAdapter extends RecyclerView.Adapter<RoomHomeAdapter.RoomHomeViewHolder> {

    private List<ServerResRoomHome> mListRoomHome;
    private Context context;

    public RoomHomeAdapter(List<ServerResRoomHome> mListRoomHome, Context context) {
        this.mListRoomHome = mListRoomHome;
        this.context = context;
    }

    @NonNull
    @Override
    public RoomHomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room_home, parent, false);
        return new RoomHomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomHomeViewHolder holder, int position) {
        ServerResRoomHome serverResRoomHome = mListRoomHome.get(position);

        holder.tvTypeRoomHome.setText(serverResRoomHome.getRoom().getType());
        holder.tvDesRoomHome.setText(serverResRoomHome.getRoom().getDescription());
        holder.tvRatingRoomHome.setText(String.valueOf(serverResRoomHome.getReview().getRating()));


        List<String> image = serverResRoomHome.getRoom().getImage();
        Glide.with(context).load(image.get(0)).into(holder.imgRoomHome);

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

    public class RoomHomeViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTypeRoomHome,tvDesRoomHome,tvRatingRoomHome;
        private ImageView imgRoomHome;
        public RoomHomeViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTypeRoomHome = itemView.findViewById(R.id.tvTypeRoomHome);
            tvDesRoomHome = itemView.findViewById(R.id.tvDescriptionRoomHome);
            tvRatingRoomHome = itemView.findViewById(R.id.tvRatingRoomHome);

            imgRoomHome = itemView.findViewById(R.id.imgRoomHome);

        }
    }
}
