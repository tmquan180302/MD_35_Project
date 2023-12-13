package com.poly.hopa.ui.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputLayout;
import com.poly.hopa.R;

public class ForgotPhoneActivity extends AppCompatActivity {

    TextInputLayout tvPhoneForgot;
    private ImageView imgBack;
    private Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        tvPhoneForgot = findViewById(R.id.edtPhoneForgot);
        imgBack = findViewById(R.id.btnBackPhone);
        btnNext = findViewById(R.id.btnNext);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneForgot = tvPhoneForgot.getEditText().getText().toString();
                int phoneForgotInt = Integer.parseInt(phoneForgot);
                Intent intent = new Intent(ForgotPhoneActivity.this, OtpForgotPassActivity.class);
                intent.putExtra("phone", "+84" + phoneForgot);
                intent.putExtra("phoneForgot", phoneForgotInt);
                startActivity(intent);
            }
        });
    }
}