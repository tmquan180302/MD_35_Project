package com.poly.hopa.ui.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import com.poly.hopa.R;

import com.vdx.animatedtoast.AnimatedToast;


import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;


public class OtpForgotPassActivity extends AppCompatActivity {

    private String phoneNumber;
    private int phoneForgot;
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
        setContentView(R.layout.activity_forgot_otp);

        otpInput = findViewById(R.id.forgot_otp);
        nextBtn = findViewById(R.id.forgot_next_btn);
        progressBar = findViewById(R.id.forgot_progress_bar);
        resendOtpTextView = findViewById(R.id.forgotResend_otp_textview);

        phoneNumber = getIntent().getStringExtra("phone");
        phoneForgot = getIntent().getIntExtra("phoneForgot", 0);


        sendOtp(phoneNumber, false);
        resendOtpTextView.setOnClickListener(v -> {
            sendOtp(phoneNumber, true);
        });
        nextBtn.setOnClickListener(v -> {
            String enteredOtp = otpInput.getText().toString();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, enteredOtp);
            signIn(credential);
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

                    Intent intent = new Intent(OtpForgotPassActivity.this, ResetPassForgotActivity.class);
                    intent.putExtra("phoneForgot", phoneForgot);
                    startActivity(intent);


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


}













