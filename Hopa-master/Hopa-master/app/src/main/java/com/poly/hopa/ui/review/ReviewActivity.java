package com.poly.hopa.ui.review;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.bumptech.glide.Glide;
import com.poly.hopa.R;
import com.poly.hopa.models.ServerRequest.ServerReqReview;
import com.poly.hopa.api.ServiceApi;
import com.poly.hopa.models.Review;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewActivity extends AppCompatActivity {
    private EditText edtCmt;
    private RatingBar ratingBar;
    private Toolbar toolbar;

    private ImageView imageReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        Button btnReview = findViewById(R.id.btnReview);
        edtCmt = findViewById(R.id.edtCmt);
        ratingBar = findViewById(R.id.ratingBar);
        imageReview = findViewById(R.id.imgReview);
        toolbar = findViewById(R.id.toolbarReview);

        setSupportActionBar(toolbar);

        toolbar.setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        String idRoom = getIntent().getStringExtra("idRoom");
        String imageRoom = getIntent().getStringExtra("imageRoom");
        String idService = getIntent().getStringExtra("idService");
        String imageService = getIntent().getStringExtra("imageService");

        if (imageRoom != null) {
            Glide.with(this).load(imageRoom).into(imageReview);
        } else if (imageService != null) {
            Glide.with(this).load(imageService).into(imageReview);
        }

        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (idRoom != null) {
                    addRoomReview();
                } else if (idService != null) {
                    addServiceReview();
                }
                onBackPressed();
            }
        });

    }

    private void addRoomReview() {

        float rating = ratingBar.getRating();
        String reviewText = edtCmt.getText().toString();
        String idRoom = getIntent().getStringExtra("idRoom");


        ServerReqReview review = new ServerReqReview();
        review.setComment(reviewText);
        review.setRating(rating);
        review.setRoom(idRoom);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("myPreferences", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        ServiceApi.apiService.addReview("Bearer " + token, review).enqueue(new Callback<Review>() {
            @Override
            public void onResponse(Call<Review> call, Response<Review> response) {
                if (response.isSuccessful()) {

                } else {
                    // Handle error response
                }
            }


            @Override
            public void onFailure(Call<Review> call, Throwable t) {

            }
        });
    }

    private void addServiceReview() {

        float rating = ratingBar.getRating();
        String reviewText = edtCmt.getText().toString();
        String idService = getIntent().getStringExtra("idService");


        ServerReqReview review = new ServerReqReview();
        review.setComment(reviewText);
        review.setRating(rating);
        review.setService(idService);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("myPreferences", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        ServiceApi.apiService.addReview("Bearer " + token, review).enqueue(new Callback<Review>() {
            @Override
            public void onResponse(Call<Review> call, Response<Review> response) {
                if (response.isSuccessful()) {

                } else {
                    // Handle error response
                }
            }


            @Override
            public void onFailure(Call<Review> call, Throwable t) {

            }
        });
    }
}