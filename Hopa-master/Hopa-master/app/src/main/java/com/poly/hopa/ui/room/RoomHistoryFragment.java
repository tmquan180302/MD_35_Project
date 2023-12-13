package com.poly.hopa.ui.room;

import android.content.Context;
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
import com.poly.hopa.adapter.HistoryRoomAdapter;
import com.poly.hopa.api.ServiceApi;
import com.poly.hopa.models.Booking;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RoomHistoryFragment extends Fragment {

    private HistoryRoomAdapter adapter;

    private List<Booking> bookingList;
    private RecyclerView recyclerView;

    private boolean dataLoaded = false;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rcvHistoryRoom);
        bookingList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new HistoryRoomAdapter(bookingList, getContext());
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (!dataLoaded) {
            getAllRoomHistory();
            dataLoaded = true;
        }
    }

    public void getAllRoomHistory() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("myPreferences", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        ServiceApi.apiService.getBookingRoomUser("Bearer " + token).enqueue(new Callback<List<Booking>>() {
            @Override
            public void onResponse(Call<List<Booking>> call, Response<List<Booking>> response) {
                if (response.isSuccessful()) {
                    Log.d("TAG", "onResponse: "+response.body());
                    bookingList.clear();
                    bookingList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Booking>> call, Throwable t) {

            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_room_history, container, false);
    }
}