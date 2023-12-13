package com.poly.hopa.ui.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.poly.hopa.R;
import com.poly.hopa.models.ServerRequest.ServerReqChangePass;
import com.poly.hopa.api.ServiceApi;
import com.poly.hopa.models.User;
import com.vdx.animatedtoast.AnimatedToast;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPassActivity extends AppCompatActivity {

    private ImageButton btn_back;
    private EditText oldPasswordEditText, newPasswordEditText, confirmNewPasswordEditText;
    private Button changePasswordButton;
    private Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);
        btn_back = findViewById(R.id.btn_resetpass_back);
        oldPasswordEditText = findViewById(R.id.oldPasswordEditText);
        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        confirmNewPasswordEditText = findViewById(R.id.confirmNewPasswordEditText);
        changePasswordButton = findViewById(R.id.changePasswordButton);


        changePasswordButton.setOnClickListener(v -> changePassword());
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void changePassword() {
        String oldPassword = oldPasswordEditText.getText().toString();
        String newPassword = newPasswordEditText.getText().toString();
        String confirmNewPassword = confirmNewPasswordEditText.getText().toString();

        // Validate passwords
        String newPasswordError = validatePasswords(newPassword, confirmNewPassword);
        newPasswordEditText.setError(newPasswordError);

        if (newPasswordError != null) {
            return;
        }

        ServerReqChangePass serverReqChangePass = new ServerReqChangePass();
        serverReqChangePass.setOldPassWord(oldPassword);
        serverReqChangePass.setNewPassWord(newPassword);

        SharedPreferences sharedPreferences = context.getSharedPreferences("myPreferences", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        ServiceApi.apiService.changePassword("Bearer " + token, serverReqChangePass).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    AnimatedToast.Success(getApplicationContext(), "Success","Changed password successfully", Gravity.CENTER, Toast.LENGTH_SHORT,AnimatedToast.STYLE_DARK, AnimatedToast.ANIMATION_ROTATE);
                    onBackPressed();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t);
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