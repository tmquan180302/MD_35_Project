package com.poly.hopa.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.poly.hopa.R;
import com.poly.hopa.adapter.FavoriteAdapter;
import com.poly.hopa.api.ServiceApi;
import com.poly.hopa.models.Favorite;
import com.poly.hopa.ui.user.LoginActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FavoriteFragment extends Fragment implements FavoriteAdapter.CallBack {
    public FavoriteAdapter favoriteAdapter;
    private List<Favorite> mListFavorite;
    private RecyclerView rcvFavrorite;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rcvFavrorite = view.findViewById(R.id.rcvFavorite);
        mListFavorite = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcvFavrorite.setLayoutManager(linearLayoutManager);
        favoriteAdapter = new FavoriteAdapter(mListFavorite, getContext(), this);
        rcvFavrorite.setAdapter(favoriteAdapter);

        getAllFavorite();
    }


    public void getAllFavorite() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("myPreferences", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        ServiceApi.apiService.getAllFavorite("Bearer " + token).enqueue(new Callback<List<Favorite>>() {
            @Override
            public void onResponse(Call<List<Favorite>> call, Response<List<Favorite>> response) {
                if (response.isSuccessful()) {
                    Log.d("TAG", "onResponse: " + response.body());
                    mListFavorite.clear();
                    mListFavorite.addAll(response.body());
                    favoriteAdapter.notifyDataSetChanged();
                } else {
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
           }
            }

            @Override
            public void onFailure(Call<List<Favorite>> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t);
            }
        });

    }

    public void deleteFavorite(String favoriteId) {
        ServiceApi.apiService.deleteFavorite(favoriteId).enqueue(new Callback<Favorite>() {
            @Override
            public void onResponse(Call<Favorite> call, Response<Favorite> response) {
                if (response.isSuccessful()) {
                    getAllFavorite();
                }

            }

            @Override
            public void onFailure(Call<Favorite> call, Throwable t) {

            }
        });

    }

    @Override
    public void onFavoriteDelete(String favoriteId) {
        deleteFavorite(favoriteId);
    }
}