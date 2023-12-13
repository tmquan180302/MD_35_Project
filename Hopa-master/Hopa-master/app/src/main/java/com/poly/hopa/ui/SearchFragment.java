package com.poly.hopa.ui;

import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.poly.hopa.R;
import com.poly.hopa.adapter.ServiceAdapter;
import com.poly.hopa.api.ServiceApi;
import com.poly.hopa.models.Service;
import com.poly.hopa.ui.service.SearchActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchFragment extends Fragment{


    public ServiceAdapter serviceAdapter;
    private ProgressBar progressBar;
    private List<Service> mlistService;
    public LinearLayout imgSearch;
    private RecyclerView rcv;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rcv = view.findViewById(R.id.rcv);
        progressBar = view.findViewById(R.id.progress_service);
        imgSearch = view.findViewById(R.id.idSearch);
        mlistService = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcv.setLayoutManager(linearLayoutManager);
        serviceAdapter = new ServiceAdapter(mlistService, getContext());
        progressBar.setVisibility(View.VISIBLE);
        rcv.setAdapter(serviceAdapter);
        getAllUser();
    }
    private void getAllUser() {
        Call<List<Service>> call = ServiceApi.apiService.getServiceCall();
        call.enqueue(new Callback<List<Service>>() {
            @Override
            public void onResponse(Call<List<Service>> call, Response<List<Service>> response) {
                if (response.isSuccessful()) {

                    mlistService.clear();
                    mlistService.addAll(response.body());
                    serviceAdapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onFailure(Call<List<Service>> call, Throwable t) {
                Log.e("failed load service!!!", t.toString());
            }
        });
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(), SearchActivity.class);
                v.getContext().startActivity(intent);
            }
        });

    }

}


