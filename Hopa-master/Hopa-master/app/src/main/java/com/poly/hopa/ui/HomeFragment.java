package com.poly.hopa.ui;

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
import com.poly.hopa.adapter.RoomHomeAdapter;
import com.poly.hopa.adapter.RoomRcmHomeAdapter;
import com.poly.hopa.adapter.ServiceHomeAdapter;
import com.poly.hopa.models.ServerResponse.ServerResRoomHome;
import com.poly.hopa.models.ServerResponse.ServerResRoomHomeApi;
import com.poly.hopa.api.ServiceApi;
import com.poly.hopa.models.Service;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private RoomHomeAdapter roomHomeAdapter;
    private ServiceHomeAdapter serviceHomeAdapter;
    private RoomRcmHomeAdapter roomRcmHomeAdapter;

    private List<ServerResRoomHome> listHigh;
    private List<ServerResRoomHome> listLow;
    private List<Service> listService;

    private RecyclerView rcvHigh;
    private RecyclerView rcvLow;
    private RecyclerView rcvService;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rcvHigh = view.findViewById(R.id.rcvRoomHome);
        rcvLow = view.findViewById(R.id.rcvRoomHomeRcm);
        rcvService = view.findViewById(R.id.rcvServiceHome);

        listHigh = new ArrayList<>();
        listLow = new ArrayList<>();
        listService = new ArrayList<>();

        LinearLayoutManager linearLayoutManagerHigh = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rcvHigh.setLayoutManager(linearLayoutManagerHigh);

        LinearLayoutManager linearLayoutManagerLow = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rcvLow.setLayoutManager(linearLayoutManagerLow);

        LinearLayoutManager linearLayoutManagerService = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rcvService.setLayoutManager(linearLayoutManagerService);

        roomHomeAdapter = new RoomHomeAdapter(listHigh, getContext());
        rcvHigh.setAdapter(roomHomeAdapter);

        roomRcmHomeAdapter = new RoomRcmHomeAdapter(listLow, getContext());
        rcvLow.setAdapter(roomRcmHomeAdapter);

        serviceHomeAdapter = new ServiceHomeAdapter(listService, getContext());
        rcvService.setAdapter(serviceHomeAdapter);

        getAll();


    }

    private void getAll() {
        ServiceApi.apiService.getHomeScreen().enqueue(new Callback<ServerResRoomHomeApi>() {
            @Override
            public void onResponse(Call<ServerResRoomHomeApi> call, Response<ServerResRoomHomeApi> response) {
                if (response.isSuccessful()) {
                    Log.d("TAG", "onResponse: " + response.body());
                    listHigh.clear();
                    listHigh.addAll(response.body().getRoomHigh());
                    roomHomeAdapter.notifyDataSetChanged();

                    listLow.clear();
                    listLow.addAll(response.body().getRoomLow());
                    roomRcmHomeAdapter.notifyDataSetChanged();

                    listService.clear();
                    listService.addAll(response.body().getService());
                    serviceHomeAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<ServerResRoomHomeApi> call, Throwable t) {
                Log.d("TAG", "onFailure: "+t);
            }
        });
    }
}
