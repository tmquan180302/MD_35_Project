package com.poly.hopa.ui.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputLayout;
import com.poly.hopa.R;
import com.poly.hopa.models.ServerRequest.ServerReqForgotPass;
import com.poly.hopa.api.ServiceApi;
import com.poly.hopa.models.User;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPassForgotActivity extends AppCompatActivity {
    private ImageView imgBack;

    Button btnDoneForgot;

    TextInputLayout edtNewPassForgot, edtNewPassForgotConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
        imgBack = findViewById(R.id.imgBackForgotPass);
        edtNewPassForgot = findViewById(R.id.edtNewPassForgot);
        edtNewPassForgotConfirm = findViewById(R.id.edtNewPassForgotConfirm);
        btnDoneForgot = findViewById(R.id.btnDoneForgot);


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnDoneForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeUserPassWordForgot();
            }
        });

    }

    private void changeUserPassWordForgot() {
        String newPassWord = edtNewPassForgot.getEditText().getText().toString();
        String confirmNewPassword = edtNewPassForgotConfirm.getEditText().getText().toString();
        int phoneForgot = getIntent().getIntExtra("phoneForgot", 0);


        // Validate passwords
        String newPasswordError = validatePasswords(newPassWord, confirmNewPassword);
        edtNewPassForgot.setError(newPasswordError);

        if (newPasswordError != null) {
            return;
        }

        ServerReqForgotPass serverReqForgotPass = new ServerReqForgotPass();
        serverReqForgotPass.setPhoneNumber(phoneForgot);
        serverReqForgotPass.setNewPassWord(newPassWord);

        ServiceApi.apiService.changeUserPassWordForgot(serverReqForgotPass).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Intent intent = new Intent(ResetPassForgotActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });


    }

    private String validatePasswords(String newPassword, String confirmNewPassword) {
        Pattern lowercase = Pattern.compile("[a-z]");
        Pattern uppercase = Pattern.compile("[A-Z]");
        Pattern digit = Pattern.compile("[0-9]");
        Pattern err = Pattern.compile("[!@#$%^&*+=?-]");

        if (newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
            return "Không để trống mật khẩu";
        }

        if (newPassword.length() < 8) {
            return "Tối thiểu 8 ký tự";
        }

        if (!lowercase.matcher(newPassword).find()) {
            return "Cần ít nhất 1 chữ thường";
        }

        if (!uppercase.matcher(newPassword).find()) {
            return "Cần ít nhất 1 chữ in Hoa";
        }

        if (!digit.matcher(newPassword).find()) {
            return "Cần ít nhất 1 chữ số";
        }

        if (err.matcher(newPassword).find()) {
            return "Không chứa ký tự đặc biệt";
        }

        if (!newPassword.equals(confirmNewPassword)) {
            return "Mật khẩu mới và xác nhận mật khẩu mới không khớp";
        }

        return null; // No validation error
    }
}
