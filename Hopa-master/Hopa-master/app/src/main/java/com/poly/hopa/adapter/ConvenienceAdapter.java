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
import com.poly.hopa.models.Convenience;
import com.poly.hopa.models.Review;

import java.util.List;

public class ConvenienceAdapter extends RecyclerView.Adapter<ConvenienceAdapter.ConvenienceAdapterViewHolder>{
    private List<Convenience> mListConvenience;
    private Context context;

    public ConvenienceAdapter(List<Convenience> mListConvenience, Context context) {
        this.mListConvenience = mListConvenience;
        this.context = context;
    }

    @NonNull
    @Override
    public ConvenienceAdapter.ConvenienceAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_convenience, parent, false);
        return new ConvenienceAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConvenienceAdapter.ConvenienceAdapterViewHolder holder, int position) {
        Convenience convenience=mListConvenience.get(position);
        holder.tvConvenience.setText(convenience.getNameConvenience());
        Glide.with(context)
                .load(convenience.getImageConvenience())
                .into(holder.imgConvenience);
    }

    @Override
    public int getItemCount() {
        return mListConvenience.size();
    }

    public class ConvenienceAdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView tvConvenience;
        private ImageView imgConvenience;
        public ConvenienceAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            tvConvenience=itemView.findViewById(R.id.tvConvenience);
            imgConvenience=itemView.findViewById(R.id.imgConvenience);
        }
    }
}
