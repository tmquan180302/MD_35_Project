package com.poly.hopa.ui.user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.poly.hopa.MainActivity;
import com.poly.hopa.R;
import com.poly.hopa.api.ServiceApi;
import com.poly.hopa.models.ServerRequest.ServerReqLogin;
import com.poly.hopa.models.ServerRequest.ServerReqLoginGoogle;
import com.poly.hopa.models.ServerRequest.ServerReqSignup;
import com.poly.hopa.models.ServerResponse.ServerResToken;
import com.poly.hopa.models.User;

import java.security.SecureRandom;
import java.util.Random;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private ImageView imgGoogle;
    private TextView tvSignIn;
    private EditText edtEmail, edtPass, edtFullName, edtPhoneNumber;
    private Button btnDangKy;

    GoogleSignInOptions sgo;

    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);

        tvSignIn = findViewById(R.id.tvSignIn);
        edtEmail = findViewById(R.id.edtEmail);
        edtPass = findViewById(R.id.edtPass);
        edtFullName = findViewById(R.id.edtFullName);
        edtPhoneNumber = findViewById(R.id.edtPhone);
        btnDangKy = findViewById(R.id.btnDangKy);
        imgGoogle = findViewById(R.id.ivGoogle);


        sgo = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, sgo);

        btnDangKy.setOnClickListener(v -> {

            String userName = edtFullName.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String passWord = edtPass.getText().toString().trim();
            String phoneNumber = edtPhoneNumber.getText().toString().trim();

            if (validate()) {
                Intent intent = new Intent(SignUpActivity.this, OtpSignUpActivity.class);
                intent.putExtra("phone", "+84" + phoneNumber);
                intent.putExtra("phoneSave", phoneNumber);
                intent.putExtra("userName", userName);
                intent.putExtra("email", email);
                intent.putExtra("passWord", passWord);
                startActivity(intent);
            }

        });

        tvSignIn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        });

        imgGoogle.setOnClickListener(v -> {
            clickLogoGoogle();
        });

    }


    private boolean validate() {

        String userName = edtFullName.getText().toString().trim();
        String Email = edtEmail.getText().toString().trim();
        String PassWord = edtPass.getText().toString().trim();
        String PhoneNumber = edtPhoneNumber.getText().toString().trim();

        Pattern uppercase = Pattern.compile("[A-Z]");
        Pattern lowercase = Pattern.compile("[a-z]");
        Pattern digit = Pattern.compile("[0-9]");
        Pattern specialChar = Pattern.compile("[!@#$%^&*+=?-]");

        edtFullName.setError(userName.isEmpty() ? "Không để trống" : (userName.length() < 5 ? "Tối thiểu 5 ký tự" : null));
        edtEmail.setError(Email.isEmpty() ? "Không để trống" : (!Patterns.EMAIL_ADDRESS.matcher(Email).matches() ? "Email không hợp lệ" : null));
        edtPass.setError(
                PassWord.isEmpty() ? "Không để trống mật khẩu" :
                        (PassWord.length() < 8 ? "Tối thiểu 8 ký tự" :
                                (!lowercase.matcher(PassWord).find() ? "Cần ít nhất 1 chữ thường" :
                                        (!uppercase.matcher(PassWord).find() ? "Cần ít nhất 1 chữ in Hoa" :
                                                (!digit.matcher(PassWord).find() ? "Cần ít nhất 1 chữ số" :
                                                        (specialChar.matcher(PassWord).find() ? "Không chứa ký tự đặc biệt" : null))))));
        edtPhoneNumber.setError(PhoneNumber.isEmpty() ? "Không để trống" : (PhoneNumber.length() != 10 ? "Số điện thoại gồm 10 số" : null));

        return edtFullName.getError() == null && edtEmail.getError() == null && edtPass.getError() == null && edtPhoneNumber.getError() == null;
    }


    private void clickLogoGoogle() {
        try {
            Intent intent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(intent, 1000);
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                String userName = account.getId();
                String email = account.getEmail();
                String passWord = generateRandomPassword();
                clickDangKy(userName,email, passWord);

                mGoogleSignInClient.signOut();

            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
        }


    }


    private void clickDangKy(String userName, String email, String passWord) {
        ServerReqSignup serverReqSignup = new ServerReqSignup();
        serverReqSignup.setUserName(userName);
        serverReqSignup.setEmail(email);
        serverReqSignup.setPassWord(passWord);


        ServiceApi.apiService.register(serverReqSignup).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    String userRes = response.body().getUserName();
                    String passRes = response.body().getPassWord();
                    clickDangNhap(userRes, passRes);
                }else {
                    clickDangNhapByGoogle(email);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    private void clickDangNhap(String userName, String userPass) {

        ServerReqLogin serverReqLogin = new ServerReqLogin();
        serverReqLogin.setUserName(userName);
        serverReqLogin.setPassWord(userPass);

        ServiceApi.apiService.login(serverReqLogin).enqueue(new Callback<ServerResToken>() {
            @Override
            public void onResponse(Call<ServerResToken> call, Response<ServerResToken> response) {
                if (response.isSuccessful()) {
                    SharedPreferences sharedPreferences = getSharedPreferences("myPreferences", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.putString("token", response.body().getToken());
                    editor.apply();
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {

                }
            }

            @Override
            public void onFailure(Call<ServerResToken> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t);
            }
        });
    }


    private void clickDangNhapByGoogle(String email) {

        ServerReqLoginGoogle serverReqLoginGoogle = new ServerReqLoginGoogle();
        serverReqLoginGoogle.setEmail(email);

        ServiceApi.apiService.loginByEmail(serverReqLoginGoogle).enqueue(new Callback<ServerResToken>() {
            @Override
            public void onResponse(Call<ServerResToken> call, Response<ServerResToken> response) {
                if (response.isSuccessful()) {
                    SharedPreferences sharedPreferences = getSharedPreferences("myPreferences", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.putString("token", response.body().getToken());
                    editor.apply();
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {

                }
            }

            @Override
            public void onFailure(Call<ServerResToken> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t);
            }
        });
    }


    private static String generateRandomPassword() {
        String uppercaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowercaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";

        String allCharacters = uppercaseLetters + lowercaseLetters + numbers;

        SecureRandom random = new SecureRandom();

        // Tạo chữ cái đầu viết hoa
        char firstUppercaseChar = uppercaseLetters.charAt(random.nextInt(uppercaseLetters.length()));

        // Tạo một chữ cái thường và một số ngẫu nhiên
        char lowercaseChar = lowercaseLetters.charAt(random.nextInt(lowercaseLetters.length()));
        char numberChar = numbers.charAt(random.nextInt(numbers.length()));

        // Tạo các ký tự ngẫu nhiên còn lại
        StringBuilder randomChars = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            char randomChar = allCharacters.charAt(random.nextInt(allCharacters.length()));
            randomChars.append(randomChar);
        }

        // Kết hợp các ký tự để tạo mật khẩu hoàn chỉnh
        String password = "" + firstUppercaseChar + lowercaseChar + numberChar + randomChars.toString();
        return password;
    }


}