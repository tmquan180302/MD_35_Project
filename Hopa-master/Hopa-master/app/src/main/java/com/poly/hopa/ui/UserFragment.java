package com.poly.hopa.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.poly.hopa.R;
import com.poly.hopa.ui.booking.HistoryBookingActivity;
import com.poly.hopa.ui.user.LoginActivity;
import com.poly.hopa.ui.user.ResetPassActivity;
import com.poly.hopa.ui.user.UserInfoActivity;


public class UserFragment extends Fragment {


    private LinearLayout llUserInfo,llHistory,llDoiMK,llLogOut;
    public UserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        llUserInfo = view.findViewById(R.id.llUserInfo);
        llHistory = view.findViewById(R.id.llHistory);
        llDoiMK = view.findViewById(R.id.llDoiMK);
        llLogOut = view.findViewById(R.id.llLogOut);

        llHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), HistoryBookingActivity.class));
            }
        });
        llUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), UserInfoActivity.class));
            }
        });

        llDoiMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ResetPassActivity.class));
            }
        });

        llLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = createDialog();
                dialog.show();
            }
            AlertDialog createDialog(){
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Xác nhận đăng xuất");
                builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //log out here
                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                });

                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        createDialog().dismiss();
                    }
                });
                return builder.create();
            }
        });
    }
}