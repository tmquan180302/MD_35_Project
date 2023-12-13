package com.poly.hopa.ui.room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.poly.hopa.R;
import com.poly.hopa.adapter.CmtAdapter;
import com.poly.hopa.adapter.ConvenienceAdapter;
import com.poly.hopa.adapter.FavoriteAdapter;
import com.poly.hopa.models.ServerResponse.ServerResFavorite;
import com.poly.hopa.api.ServiceApi;
import com.poly.hopa.models.Convenience;
import com.poly.hopa.models.Review;
import com.poly.hopa.models.Room;
import com.poly.hopa.ui.booking.PaymentActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomDetailActivity extends AppCompatActivity{
    public TextView tvDescription, tvPrice, tvType;
    public Button btnDat;
    public ImageSlider imageSlider;
    private ImageView imgBack;
    private ToggleButton btn_toggle;
    public CmtAdapter cmtAdapter;
    public ConvenienceAdapter convenienceAdapter;
    public FavoriteAdapter favoriteAdapter;
    private List<Review> mListCmt;
    private List<Convenience> mListConvenience;
    private RecyclerView rcvCmt, rcvConvenience;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_room);

        tvDescription = findViewById(R.id.tvDescriptionRoom);
        tvPrice = findViewById(R.id.tvPriceRoom);
        tvType = findViewById(R.id.tvTypeRoom);
        btnDat = findViewById(R.id.btn_datRoom);
        imageSlider = findViewById(R.id.image_slider);
        imgBack = findViewById(R.id.btnBack);
        btn_toggle = findViewById(R.id.btn_toggleRoom);
        rcvCmt = findViewById(R.id.rcvCmtroom);
        rcvConvenience = findViewById(R.id.rcvConvenience);
        mListCmt = new ArrayList<>();



        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvCmt.setLayoutManager(linearLayoutManager);
        cmtAdapter = new CmtAdapter(mListCmt, this);
        rcvCmt.setAdapter(cmtAdapter);



        mListConvenience = new ArrayList<>();
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rcvConvenience.setLayoutManager(linearLayoutManager2);
        convenienceAdapter = new ConvenienceAdapter(mListConvenience, this);
        rcvConvenience.setAdapter(convenienceAdapter);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btn_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                String message = intent.getStringExtra("idRoom");
                // Kiểm tra xem dịch vụ này đã được yêu thích chưa
                    // Nếu chưa yêu thích, thì yêu thích
                    btn_toggle.setBackgroundResource(R.drawable.icon_favorite_on);
                    addFavorite(message);



            }
        });
        getDuLieu();
        getCmt();
    }

    public void addFavorite(String id) {

        SharedPreferences sharedPreferences = this.getSharedPreferences("myPreferences", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        ServiceApi.apiService.addFavorite("Bearer " + token, id).enqueue(new Callback<ServerResFavorite>() {
            @Override
            public void onResponse(Call<ServerResFavorite> call, Response<ServerResFavorite> response) {
                if (response.isSuccessful()){
                }
            }

            @Override
            public void onFailure(Call<ServerResFavorite> call, Throwable t) {

            }
        });
    }


    private void getCmt() {
        Intent intent = getIntent();
        String messageCmt = intent.getStringExtra("idRoom");
        Call<List<Review>> callCmt = ServiceApi.apiService.getRoomReviews(messageCmt);
        callCmt.enqueue(new Callback<List<Review>>() {
            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                List<Review> reviews = response.body();
                mListCmt.clear();
                mListCmt.addAll(response.body());
                cmtAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<List<Review>> call, Throwable t) {

            }
        });
    }

    private void getDuLieu() {
        Intent intent = getIntent();
        String message = intent.getStringExtra("idRoom");
        Call<Room> roomCall = ServiceApi.apiService.getInfoRoom(message);
        roomCall.enqueue(new Callback<Room>() {
            @Override
            public void onResponse(Call<Room> call, Response<Room> response) {
                if (response.isSuccessful()) {

                    tvDescription.setText(response.body().getDescription());
                    tvType.setText(response.body().getType());
                    tvPrice.setText(String.valueOf(response.body().getPrice()) + "  VNĐ/đêm");

                    mListConvenience.clear();
                    mListConvenience.addAll(response.body().getConvenience());
                    convenienceAdapter.notifyDataSetChanged();

                    List<String> image = response.body().getImage();
                    ArrayList<SlideModel> imageList = new ArrayList<>();
                    for (String string : image) {
                        imageList.add(new SlideModel(string, ScaleTypes.CENTER_CROP));
                    }
                    imageSlider.setImageList(imageList);
                    imageSlider.stopSliding();

                    if (response.body().getAvailable() == true){
                        btnDat.setEnabled(true);
                    }else {
                        btnDat.setText("Hết phòng");
                        btnDat.setEnabled(false);
                    }


                    btnDat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(RoomDetailActivity.this, PaymentActivity.class);
                            intent.putExtra("idRoom", response.body().getRoomId());
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Room> call, Throwable t) {

            }
        });


    }

}



