package com.poly.hopa.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.poly.hopa.R;
import com.poly.hopa.models.ServerResponse.ServerResRoom;
import com.poly.hopa.ui.room.RoomDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchAdapterViewHolder> {
    private List<ServerResRoom> mListSearchRoom;
    private Context context;

    public SearchAdapter(List<ServerResRoom> mListSearchRoom, Context context) {
        this.mListSearchRoom = mListSearchRoom;
        this.context = context;
    }

    @NonNull
    @Override
    public SearchAdapter.SearchAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_search_activity, parent, false);
        return new SearchAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.SearchAdapterViewHolder holder, int position) {
        ServerResRoom serverResRoom = mListSearchRoom.get(position);

        boolean myBooleanValue = true; // Giá trị boolean bạn muốn chuyển đổi

        holder.tvType.setText(serverResRoom.getRoom().getType() + "");
        holder.tvPrice.setText(String.valueOf(serverResRoom.getRoom().getPrice()) + " VND");
        holder.tvDescription.setText(serverResRoom.getRoom().getDescription() + "");


        holder.tvRasting.setText(String.valueOf(serverResRoom.getReview().getRating()));


        holder.tvIsAvailable.setText(serverResRoom.getRoom().getAvailable() ? "Còn phòng" : "Hết phòng");
        List<String> image = serverResRoom.getRoom().getImage();
        ArrayList<SlideModel> imageList = new ArrayList<>();
        for (String string : image) {
            imageList.add(new SlideModel(string, ScaleTypes.CENTER_CROP));
        }
        holder.imgRoom.setImageList(imageList);
        holder.imgRoom.stopSliding();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RoomDetailActivity.class);
                intent.putExtra("idRoom", serverResRoom.getRoom().getRoomId());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListSearchRoom.size();
    }

    public class SearchAdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView tvType, tvPrice, tvDescription, tvIsAvailable, tvRasting;
        private ImageSlider imgRoom;

        public SearchAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            tvType = itemView.findViewById(R.id.tvType);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvRasting = itemView.findViewById(R.id.tvRasting);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvIsAvailable = itemView.findViewById(R.id.tvIsAvailable);
            imgRoom = itemView.findViewById(R.id.imgRoom);
        }
    }
}
