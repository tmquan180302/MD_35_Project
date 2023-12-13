package com.poly.hopa.ui.booking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.poly.hopa.R;
import com.poly.hopa.adapter.UserHistoryAdapter;
import com.poly.hopa.ui.room.RoomHistoryFragment;
import com.poly.hopa.ui.service.ServiceHistoryFragment;

public class HistoryBookingActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tableLayout;
    private ViewPager viewPager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        tableLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPagerHistory);
        toolbar = findViewById(R.id.toolbarUserHistory);

        setSupportActionBar(toolbar);

        toolbar.setTitle("");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        UserHistoryAdapter userHistoryAdapter = new UserHistoryAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        userHistoryAdapter.addFragment(new RoomHistoryFragment(), "Room");
        userHistoryAdapter.addFragment(new ServiceHistoryFragment() ,"Service");
        viewPager.setAdapter(userHistoryAdapter);

        tableLayout.setupWithViewPager(viewPager);

    }

}