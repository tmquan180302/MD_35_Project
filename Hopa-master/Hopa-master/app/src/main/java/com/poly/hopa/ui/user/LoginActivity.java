package com.poly.hopa.ui.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.poly.hopa.models.ServerRequest.ServerReqLogin;
import com.poly.hopa.MainActivity;
import com.poly.hopa.R;
import com.poly.hopa.models.ServerResponse.ServerResToken;
import com.poly.hopa.api.ServiceApi;
import com.vdx.animatedtoast.AnimatedToast;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout edtEmail, edtPass;
    private Button btnDangNhap;
    private CheckBox cbRemember;
    private TextView tvSignup, tvQMK;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tvSignup = findViewById(R.id.tvSignUp);
        tvQMK = findViewById(R.id.tvQMK);
        edtEmail = findViewById(R.id.edt_Email_Layout);
        edtPass = findViewById(R.id.edt_Pass_Layout);
        btnDangNhap = findViewById(R.id.btnDangNhap);
        cbRemember = findViewById(R.id.cbRemember);

        SharedPreferences sharedPreferences = getSharedPreferences("myPreferences", MODE_PRIVATE);
        boolean isRemembered = sharedPreferences.getBoolean("isRemembered", false);

        if (isRemembered) {
            String savedUserName = sharedPreferences.getString("userName", "");
            String savedPassword = sharedPreferences.getString("password", "");

            // Set the saved credentials to the EditText fields
            edtEmail.getEditText().setText(savedUserName);
            edtPass.getEditText().setText(savedPassword);
            cbRemember.setChecked(true);
        }

        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = edtEmail.getEditText().getText().toString().trim();
                String userPass = edtPass.getEditText().getText().toString().trim();
                try {
                    Pattern uppercase = Pattern.compile("[A-Z]");
                    Pattern lowercase = Pattern.compile("[a-z]");
                    Pattern digit = Pattern.compile("[0-9]");
                    Pattern err = Pattern.compile("[[!@#$%^&*+=?-]]");

                    edtEmail.setError(
                            userName.isEmpty() ? "Không để trống" :
                                    (userName.length() >= 1 && userName.length() < 5) ? "Tối thiểu 5 ký tự" : null
                    );

                    edtPass.setError(
                            userPass.isEmpty() ? "Không để trống mật khẩu" :
                                    (userPass.length() >= 1 && userPass.length() < 8) ? "Tối thiểu 8 ký tự" :
                                            !lowercase.matcher(userPass).find() ? "Cần ít nhất 1 chữ thường" :
                                                    !uppercase.matcher(userPass).find() ? "Cần ít nhất 1 chữ in Hoa" :
                                                            !digit.matcher(userPass).find() ? "Cần ít nhất 1 chữ số" :
                                                                    err.matcher(userPass).find() ? "Không chứa ký tự đặc biệt" : null
                    );

                    if (userName.isEmpty() || userName.length() < 5 ||
                            userPass.isEmpty() || userPass.length() < 8 ||
                            !lowercase.matcher(userPass).find() ||
                            !uppercase.matcher(userPass).find() ||
                            !digit.matcher(userPass).find() ||
                            err.matcher(userPass).find()) {
                        return;
                    }
                    clickDangNhap(userName, userPass);
                } catch (Exception e) {
                    e.printStackTrace(); // Handle the exception appropriately
                }
            }
        });

        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        tvQMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPhoneActivity.class);
                startActivity(intent);
            }
        });
    }


    private void clickDangNhap(String userName, String userPass) {

        ServerReqLogin serverReqLogin = new ServerReqLogin();
        serverReqLogin.setUserName(userName);
        serverReqLogin.setPassWord(userPass);

        if (cbRemember.isChecked()) {
            // Save user credentials to SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("myPreferences", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isRemembered", true);
            editor.putString("userName", userName);
            editor.putString("password", userPass);
            editor.apply();
        } else {
            // Clear saved credentials if the checkbox is not checked
            SharedPreferences sharedPreferences = getSharedPreferences("myPreferences", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("isRemembered");
            editor.remove("userName");
            editor.remove("password");
            editor.apply();
        }

        ServiceApi.apiService.login(serverReqLogin).enqueue(new Callback<ServerResToken>() {
            @Override
            public void onResponse(Call<ServerResToken> call, Response<ServerResToken> response) {
                if (response.isSuccessful()) {
                    Log.d("TAG", "onResponse: " + response.body().getToken());
                    SharedPreferences sharedPreferences = getSharedPreferences("myPreferences", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    // Only clear the token, not the "Remember Me" data
                    editor.remove("token");
                    editor.putString("token", response.body().getToken());
                    editor.apply();

                    AnimatedToast.Success(getApplicationContext(), "Success", "Logged in successfully", Gravity.CENTER, Toast.LENGTH_SHORT, AnimatedToast.STYLE_DARK, AnimatedToast.ANIMATION_ROTATE);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    edtEmail.setError("Tên đăng nhập hoặc mật khẩu không chính xác");
                    edtPass.getEditText().setText("");
                }
            }

            @Override
            public void onFailure(Call<ServerResToken> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t);
            }
        });
    }

}