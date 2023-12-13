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
import com.poly.hopa.models.Favorite;
import com.poly.hopa.ui.room.RoomDetailActivity;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteAdapterViewHolder> {
    private List<Favorite> mListFavorite;
    private Context context;
    private CallBack callBack;

    public FavoriteAdapter(List<Favorite> mListFavorite, Context context, CallBack callBack) {
        this.mListFavorite = mListFavorite;
        this.context = context;
        this.callBack = callBack;
    }


    @NonNull
    @Override
    public FavoriteAdapter.FavoriteAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_favorite, parent, false);
        return new FavoriteAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.FavoriteAdapterViewHolder holder, int position) {
        Favorite favorite = mListFavorite.get(position);
        holder.tvType.setText(favorite.getRoom().getType());
        holder.tvPrice.setText(String.valueOf(favorite.getRoom().getPrice()) + " VNĐ");
        List<String> image = favorite.getRoom().getImage();
        Glide.with(context).load(image.get(0)).into(holder.imgFavorite);


        holder.imgFavoriteOn.setOnClickListener(v -> {
            callBack.onFavoriteDelete(mListFavorite.get(position).getFavoriteId());
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RoomDetailActivity.class);
                intent.putExtra("idRoom", favorite.getRoom().getRoomId());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListFavorite.size();
    }

    public class FavoriteAdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView tvType, tvPrice;
        private ImageView imgFavorite;
        private ImageView imgFavoriteOn;

        public FavoriteAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            tvType = itemView.findViewById(R.id.tvType);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            imgFavorite = itemView.findViewById(R.id.imgFavorite);
            imgFavoriteOn = itemView.findViewById(R.id.imgFavoriteOn);

        }
    }

    public interface CallBack {
        void onFavoriteDelete(String favoriteId);
    }
}
