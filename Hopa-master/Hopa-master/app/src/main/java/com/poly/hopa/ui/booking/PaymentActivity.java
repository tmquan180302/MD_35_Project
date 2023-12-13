package com.poly.hopa.ui.booking;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.google.android.material.textfield.TextInputLayout;
import com.poly.hopa.R;
import com.poly.hopa.api.ServiceApi;
import com.poly.hopa.models.Room;
import com.poly.hopa.models.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity {
    private String startDate = null;
    private String endDate = null;
    private String note;
    private TextView txtNameRoom, txtPriceRoom, txtQuantityGuest, txtQuantityChildren, txtClearGuest, typeNightTT;

    private TextInputLayout txtNote;

    private CalendarView calendarView;
    private ImageView imgBack, imgAddGuest, imgRemoveGuest, getImgAddChildren, getImgRemoveChildren;
    private Button btn_datNgay;

    private int guest = 0;

    private int children = 0;

    private boolean selectingStartDate = false;

    private boolean selectingEndDate = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);

        initView();
        Calendar currentDate = Calendar.getInstance();
        calendarView.setMinimumDate(currentDate);




        getDulieu();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imgAddGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guest++;
                txtQuantityGuest.setText(String.valueOf(guest));

            }
        });
        imgRemoveGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guest--;
                txtQuantityGuest.setText(String.valueOf(guest));

            }
        });
        getImgAddChildren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                children++;
                txtQuantityChildren.setText(String.valueOf(children));

            }
        });

        getImgRemoveChildren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                children--;
                txtQuantityChildren.setText(String.valueOf(children));

            }
        });

        txtClearGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtQuantityGuest.setText("0");
                txtQuantityChildren.setText("0");
            }
        });


        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(@NonNull EventDay eventDay) {
                Calendar selectedDate = eventDay.getCalendar();

                int day = selectedDate.get(Calendar.DAY_OF_MONTH);
                int month = selectedDate.get(Calendar.MONTH) + 1;
                int year = selectedDate.get(Calendar.YEAR);
                String dateString = day + "/" + month + "/" + year;

                // Lấy ngày hiện tại
                Calendar currentDate = Calendar.getInstance();

                try {
                    if (selectedDate.after(currentDate) || selectedDate.equals(currentDate)) {
                        // Ngày được chọn là ngày hiện tại hoặc trong tương lai

                        if (selectingStartDate == false || selectingEndDate == true) {
                            startDate = dateString;
                            endDate = null; // Đặt lại endDate thành null khi chọn startDate
                            selectingStartDate = true;
                            selectingEndDate = false;
                        } else if (selectingEndDate == false) {
                            endDate = dateString;
                            checkAndSwapDates();
                            selectingStartDate = false;
                            selectingEndDate = true;
                        }
                    } else {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


    }


    private void getDulieu() {
        Intent intent = getIntent();
        String idRoom = intent.getStringExtra("idRoom");
        String idService = intent.getStringExtra("idService");



        if (idRoom != null) {
            ServiceApi.apiService.getInfoRoom(idRoom).enqueue(new Callback<Room>() {
                @Override
                public void onResponse(Call<Room> call, Response<Room> response) {

                    if (response.isSuccessful()) {
                        txtNameRoom.setText(response.body().getType());
                        txtPriceRoom.setText(String.valueOf(response.body().getPrice()) + " VNĐ");

                        btn_datNgay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Kiểm tra xem có đủ dữ liệu trong Intent không
                                if (startDate != null && endDate != null && guest != 0) {
                                    try {
                                        note = txtNote.getEditText().getText().toString();

                                        Intent intent = new Intent(PaymentActivity.this, ConfirmPaymentActivity.class);
                                        intent.putExtra("idRoom", response.body().getRoomId());
                                        intent.putExtra("quantityGuest", guest);
                                        intent.putExtra("quantityChildren", children);
                                        intent.putExtra("startDate", startDate);
                                        intent.putExtra("endDate", endDate);
                                        intent.putExtra("note", note);
                                        startActivity(intent);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    showAlertDialog();
                                }
                            }
                        });

                    }

                }

                @Override
                public void onFailure(Call<Room> call, Throwable t) {

                }
            });

        } else if (idService != null) {
            ServiceApi.apiService.getInfoService(idService).enqueue(new Callback<Service>() {
                @Override
                public void onResponse(Call<Service> call, Response<Service> response) {
                    if (response.isSuccessful()) {
                        txtNameRoom.setText(response.body().getName());
                        txtPriceRoom.setText(String.valueOf(response.body().getPrice()) + " VNĐ");
                        typeNightTT.setText("/person");

                        btn_datNgay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Kiểm tra xem có đủ dữ liệu trong Intent không
                                if (startDate != null && endDate != null && guest != 0) {
                                    try {
                                        Intent intent = new Intent(PaymentActivity.this, ConfirmPaymentActivity.class);
                                        intent.putExtra("idService", response.body().getServiceID());
                                        intent.putExtra("quantityGuest", guest);
                                        intent.putExtra("quantityChildren", children);
                                        intent.putExtra("startDate", startDate);
                                        intent.putExtra("endDate", endDate);
                                        intent.putExtra("note", note);
                                        startActivity(intent);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    showAlertDialog();
                                }
                            }
                        });

                    }
                }

                @Override
                public void onFailure(Call<Service> call, Throwable t) {

                }
            });
        }
    }

    private void checkAndSwapDates() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            Date start = dateFormat.parse(startDate);
            Date end = dateFormat.parse(endDate);

            if (end.before(start)) {
                // endDate đứng trước startDate, đổi chỗ cho nhau
                String temp = startDate;
                startDate = endDate;
                endDate = temp;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PaymentActivity.this);
        builder.setTitle("Thông báo");
        builder.setMessage("Vui lòng chọn ngày bắt đầu, kết thúc và số lượng ");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Đóng Dialog nếu người dùng chọn OK
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void initView() {
        imgBack = findViewById(R.id.imgBackTT);
        txtNameRoom = findViewById(R.id.tvNameRoomTT);
        txtPriceRoom = findViewById(R.id.tvPriceRoomTT);

        txtQuantityGuest = findViewById(R.id.tvQuantity);
        txtQuantityChildren = findViewById(R.id.tvQuantity2);

        txtNote = findViewById(R.id.edtNote);
        txtClearGuest = findViewById(R.id.idClearGuests);


        imgAddGuest = findViewById(R.id.imgAdd);
        imgRemoveGuest = findViewById(R.id.imgRemove);
        getImgAddChildren = findViewById(R.id.imgAdd2);
        getImgRemoveChildren = findViewById(R.id.imgRemove2);
        typeNightTT = findViewById(R.id.typeNightTT);

        calendarView = findViewById(R.id.calendarViewMaterial);
        btn_datNgay = findViewById(R.id.btn_datNgay);
    }
}