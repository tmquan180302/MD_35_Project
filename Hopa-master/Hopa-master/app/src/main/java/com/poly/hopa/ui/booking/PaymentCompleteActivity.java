package com.poly.hopa.ui.booking;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.poly.hopa.MainActivity;
import com.poly.hopa.R;

public class PaymentCompleteActivity extends AppCompatActivity {

    Button btnBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ht_thanhtoan);
        btnBook = findViewById(R.id.btnSuccessHT);


        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentCompleteActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

}