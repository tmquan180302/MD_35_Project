package com.poly.hopa.ui.service;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.poly.hopa.R;
import com.poly.hopa.adapter.CmtAdapter;
import com.poly.hopa.api.ServiceApi;
import com.poly.hopa.models.Review;
import com.poly.hopa.models.Service;
import com.poly.hopa.ui.booking.PaymentActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceDetailActivity extends AppCompatActivity {
    public CmtAdapter cmtAdapter;
    private List<Review> mlistCmt;
    private TextView description, price, name;

    private Button btnDat;
    private ImageSlider imageSlider;
    private ImageView imgBack;
    private RecyclerView rcvCmt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_service);
        description = findViewById(R.id.tv_Description);
        price = findViewById(R.id.tv_price);
        name = findViewById(R.id.tv_Name);
        imageSlider = findViewById(R.id.image_slider);

        imgBack = findViewById(R.id.imgBack);
        btnDat = findViewById(R.id.btn_dat);
        rcvCmt = findViewById(R.id.rcvCmt);
        mlistCmt = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvCmt.setLayoutManager(linearLayoutManager);
        cmtAdapter = new CmtAdapter(mlistCmt, this);
        rcvCmt.setAdapter(cmtAdapter);


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        getDuLieu();
        getCmt();
    }

    private void getCmt() {
        Intent intent = getIntent();
        String idService = intent.getStringExtra("idService");
        Call<List<Review>> callCmt = ServiceApi.apiService.getServiceReviews(idService);
        callCmt.enqueue(new Callback<List<Review>>() {
            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                if (response.isSuccessful()) {
                    List<Review> reviews = response.body();
                    mlistCmt.clear();
                    mlistCmt.addAll(response.body());
                    cmtAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<List<Review>> call, Throwable t) {

            }
        });
    }

    private void getDuLieu() {
        Intent intent = getIntent();
        String message = intent.getStringExtra("idService");
        Call<Service> serviceCall = ServiceApi.apiService.getInfoService(message);
        serviceCall.enqueue(new Callback<Service>() {
            @Override
            public void onResponse(Call<Service> call, Response<Service> response) {
                if (response.isSuccessful()) {
                    description.setText(response.body().getDescription());
                    name.setText(response.body().getName());
                    price.setText("Deposit:   " + String.valueOf(response.body().getPrice()) + " VNĐ");
                    List<String> image = response.body().getImage();
                    ArrayList<SlideModel> imageList = new ArrayList<>();
                    for (int i = 0; i < image.size() - 1; i++) {
                        imageList.add(new SlideModel(image.get(i), ScaleTypes.CENTER_CROP));
                    }
                    imageSlider.setImageList(imageList);
                    imageSlider.stopSliding();

                    btnDat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(ServiceDetailActivity.this, PaymentActivity.class);
                            intent.putExtra("idService", response.body().getServiceID());
                            startActivity(intent);
                        }
                    });
                }

            }

            @Override
            public void onFailure(Call<Service> call, Throwable t) {
                Toast.makeText(ServiceDetailActivity.this, "Lỗi rồi!!!", Toast.LENGTH_SHORT).show();
            }
        });

    }


}



