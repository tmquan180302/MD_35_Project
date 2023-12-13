package com.poly.hopa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.poly.hopa.R;
import com.poly.hopa.models.Review;
import com.poly.hopa.models.Service;

import java.util.List;

public class CmtAdapter extends RecyclerView.Adapter<CmtAdapter.CmtAdapterViewHolder> {
    private List<Review> mListCmt;
    private Context context;

    public CmtAdapter(List<Review> mListCmt, Context context) {
        this.mListCmt = mListCmt;
        this.context = context;
    }

    @NonNull
    @Override
    public CmtAdapter.CmtAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_cmt, parent, false);
        return new CmtAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CmtAdapter.CmtAdapterViewHolder holder, int position) {

        Review review=mListCmt.get(position);
        holder.tvName.setText("user: "+review.getUser().getFullName());
        holder.tvCmt.setText(review.getComment());
        holder.tvRasting.setText(String.valueOf(review.getRating()));



    }

    @Override
    public int getItemCount() {
        return mListCmt.size();
    }

    public class CmtAdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName,tvCmt,tvRasting;


        public CmtAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName=itemView.findViewById(R.id.tvName);
            tvCmt=itemView.findViewById(R.id.tvCmt);
            tvRasting=itemView.findViewById(R.id.tvRasting);



        }
    }
}
