package com.poly.hopa.ui.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.poly.hopa.R;
import com.poly.hopa.models.ServerRequest.ServerReqChangeInfo;
import com.poly.hopa.api.ServiceApi;
import com.poly.hopa.models.User;
import com.vdx.animatedtoast.AnimatedToast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInfoActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button btn_update_info;
    private EditText ed_info_username, ed_info_email, ed_info_phone;

    ImageView imgBackUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        ed_info_username = findViewById(R.id.ed_info_username);
        ed_info_email = findViewById(R.id.ed_info_email);
        ed_info_phone = findViewById(R.id.ed_info_phone);
        btn_update_info = findViewById(R.id.btn_update_info);
        imgBackUserInfo = findViewById(R.id.imgBackUserInfo);

        imgBackUserInfo.setOnClickListener(v -> onBackPressed());


        btn_update_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeUserInfo();
            }


        });
        getUserInfo();
    }

    private void changeUserInfo() {
        String newUsername = ed_info_username.getText().toString().trim();
        String newEmail = ed_info_email.getText().toString().trim();
        String newPhoneNumber = ed_info_phone.getText().toString().trim();

        if (newUsername.isEmpty()) {
            ed_info_username.setError("Không để trống");
            return;
        }
        if (newEmail.isEmpty()) {
            ed_info_email.setError("Không để trống");
            return;
        }
        if (newPhoneNumber.isEmpty()) {
            ed_info_phone.setError("Không để trống");
            return;
        }

        ServerReqChangeInfo updatedUser = new ServerReqChangeInfo();
        updatedUser.setFullName(newUsername);
        updatedUser.setEmail(newEmail);
        updatedUser.setPhoneNumber(Integer.parseInt(newPhoneNumber));

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("myPreferences", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        Call<User> call = ServiceApi.apiService.changeUserInfo("Bearer " + token, updatedUser);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    AnimatedToast.Success(getApplicationContext(), "Success", "Change Info Success", Gravity.CENTER, Toast.LENGTH_SHORT, AnimatedToast.STYLE_DARK, AnimatedToast.ANIMATION_ROTATE);
                    getUserInfo();
                    onBackPressed();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // Handle failure
            }
        });
    }

    private void getUserInfo() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("myPreferences", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        Call<User> call = ServiceApi.apiService.getUserInfo("Bearer " + token);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    ed_info_username.setText(user.getFullName());
                    ed_info_email.setText(user.getEmail());
                    ed_info_phone.setText(String.valueOf(user.getPhoneNumber()));
                } else {
                    // Handle error
                    Toast.makeText(UserInfoActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t);
            }
        });
    }
}