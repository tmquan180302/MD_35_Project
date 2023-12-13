package com.poly.hopa.ui.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.poly.hopa.MainActivity;
import com.poly.hopa.R;
import com.poly.hopa.models.ServerRequest.ServerReqLogin;
import com.poly.hopa.models.ServerRequest.ServerReqSignup;
import com.poly.hopa.models.ServerResponse.ServerResToken;
import com.poly.hopa.api.ServiceApi;
import com.poly.hopa.models.User;
import com.vdx.animatedtoast.AnimatedToast;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpSignUpActivity extends AppCompatActivity {

    String userName, phoneNumber, email, passWord, phoneSave;
    Long timeoutSeconds = 60L;
    String verificationCode;
    PhoneAuthProvider.ForceResendingToken resendingToken;

    EditText otpInput;
    Button nextBtn;
    ProgressBar progressBar;
    TextView resendOtpTextView;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_otp);

        otpInput = findViewById(R.id.login_otp);
        nextBtn = findViewById(R.id.login_next_btn);
        progressBar = findViewById(R.id.login_progress_bar);
        resendOtpTextView = findViewById(R.id.resend_otp_textview);

        phoneNumber = getIntent().getStringExtra("phone");
        phoneSave = getIntent().getStringExtra("phoneSave");
        userName = getIntent().getStringExtra("userName");
        email = getIntent().getStringExtra("email");
        passWord = getIntent().getStringExtra("passWord");


        sendOtp(phoneNumber, false);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredOtp = otpInput.getText().toString();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, enteredOtp);
                signIn(credential);
            }
        });

        resendOtpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOtp(phoneNumber, true);
            }
        });


    }

    void sendOtp(String phoneNumber, boolean isResend) {
        startResendTimer();
        setInProgress(true);
        PhoneAuthOptions.Builder builder =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(timeoutSeconds, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signIn(phoneAuthCredential);
                                setInProgress(false);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                AnimatedToast.Success(getApplicationContext(), "Fail", "OTP sent fail", Gravity.CENTER, Toast.LENGTH_SHORT, AnimatedToast.STYLE_DARK, AnimatedToast.ANIMATION_ROTATE);

                                setInProgress(false);
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                verificationCode = s;
                                resendingToken = forceResendingToken;
                                AnimatedToast.Success(getApplicationContext(), "Success", "OTP sent successfully", Gravity.CENTER, Toast.LENGTH_SHORT, AnimatedToast.STYLE_DARK, AnimatedToast.ANIMATION_ROTATE);
                                setInProgress(false);
                            }
                        });
        if (isResend) {
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(resendingToken).build());
        } else {
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }

    }


    void setInProgress(boolean inProgress) {
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            nextBtn.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            nextBtn.setVisibility(View.VISIBLE);
        }
    }


    void signIn(PhoneAuthCredential phoneAuthCredential) {
        //login and go to next activity
        setInProgress(true);
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                setInProgress(false);
                if (task.isSuccessful()) {
                    clickDangKy(userName, email, passWord, phoneSave);
                } else {
                    AnimatedToast.Success(getApplicationContext(), "Fail", "OTP sent fail", Gravity.CENTER, Toast.LENGTH_SHORT, AnimatedToast.STYLE_DARK, AnimatedToast.ANIMATION_ROTATE);

                }
            }
        });


    }


    void startResendTimer() {
        resendOtpTextView.setEnabled(false);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeoutSeconds--;
                resendOtpTextView.setText("Resend OTP in " + timeoutSeconds + " seconds");
                if (timeoutSeconds <= 0) {
                    timeoutSeconds = 60L;
                    timer.cancel();
                    runOnUiThread(() -> {
                        resendOtpTextView.setEnabled(true);
                    });
                }
            }
        }, 0, 1000);
    }

    private void clickDangKy(String userName, String email, String passWord, String phoneNumber) {
        ServerReqSignup serverReqSignup = new ServerReqSignup();
        serverReqSignup.setUserName(userName);
        serverReqSignup.setEmail(email);
        serverReqSignup.setPassWord(passWord);
        serverReqSignup.setPhoneNumber(phoneNumber);


        ServiceApi.apiService.register(serverReqSignup).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    String userRes = response.body().getUserName();
                    String passRes = response.body().getPassWord();
                    clickDangNhap(userRes, passRes);
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
                    Intent intent = new Intent(OtpSignUpActivity.this, MainActivity.class);
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


}













