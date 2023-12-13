package com.poly.hopa.ui.booking;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import com.paypal.checkout.approve.Approval;
import com.paypal.checkout.approve.OnApprove;
import com.paypal.checkout.createorder.CreateOrder;
import com.paypal.checkout.createorder.CreateOrderActions;
import com.paypal.checkout.createorder.CurrencyCode;
import com.paypal.checkout.createorder.OrderIntent;
import com.paypal.checkout.createorder.UserAction;
import com.paypal.checkout.order.Amount;
import com.paypal.checkout.order.AppContext;
import com.paypal.checkout.order.CaptureOrderResult;
import com.paypal.checkout.order.OnCaptureComplete;
import com.paypal.checkout.order.OrderRequest;
import com.paypal.checkout.order.PurchaseUnit;
import com.paypal.checkout.paymentbutton.PaymentButtonContainer;
import com.poly.hopa.R;
import com.poly.hopa.api.ServiceApi;
import com.poly.hopa.models.Booking;
import com.poly.hopa.models.CreateOrderZalo;
import com.poly.hopa.models.Room;
import com.poly.hopa.models.Service;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class ConfirmPaymentActivity extends AppCompatActivity {
    private TextView tvNameTT, tvPriceTT, tvDayTT, guestTT, tvNight, tvCleaningFee, tvServiceFee, tvTotal, tvCost, typeTotal, typeNight;
    private Button btnPay;

    private PaymentButtonContainer paymentButtonContainer;
    private ImageView imgRoomTT, imgBackXN;

    private Context context = this;

    private int dayNight;
    private int serviceFee;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xacnhan_ttactivity);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ZaloPaySDK.init(2553, Environment.SANDBOX);

        tvNameTT = findViewById(R.id.tvNameTT);
        tvCost = findViewById(R.id.tvCost);
        tvPriceTT = findViewById(R.id.tvPriceTT);
        tvDayTT = findViewById(R.id.tvDayTT);
        guestTT = findViewById(R.id.guestTT);
        tvNight = findViewById(R.id.tvNight);
        tvCleaningFee = findViewById(R.id.tvCleaningFee);
        tvServiceFee = findViewById(R.id.tvServiceFee);
        tvTotal = findViewById(R.id.tvTotal);

        typeTotal = findViewById(R.id.typeTotal);
        typeNight = findViewById(R.id.typeNight);


        imgRoomTT = findViewById(R.id.imgRoomTT);
        imgBackXN = findViewById(R.id.imgBackXN);

        btnPay = findViewById(R.id.btnPay);
        paymentButtonContainer = findViewById(R.id.payment_button_container);


        imgBackXN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getDulieu();


    }

    private void addPaymentZalo(String amount, Booking booking) {

        CreateOrderZalo orderApi = new CreateOrderZalo();

        try {
            JSONObject data = orderApi.createOrder(amount);
            Log.d("Amount", amount);
            String code = data.getString("return_code");
            if (code.equals("1")) {

                String token = data.getString("zp_trans_token");
                ZaloPaySDK.getInstance().payOrder(ConfirmPaymentActivity.this, token, "demozpdk://app", new PayOrderListener() {
                    @Override
                    public void onPaymentSucceeded(String s, String s1, String s2) {

                    }

                    @Override
                    public void onPaymentCanceled(String s, String s1) {

                    }

                    @Override
                    public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {

                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void addPaymentPayPal(String amount, Booking booking) {
        paymentButtonContainer.setup(
                new CreateOrder() {
                    @Override
                    public void create(@NotNull CreateOrderActions createOrderActions) {
                        ArrayList<PurchaseUnit> purchaseUnits = new ArrayList<>();
                        purchaseUnits.add(
                                new PurchaseUnit.Builder()
                                        .amount(
                                                new Amount.Builder()
                                                        .currencyCode(CurrencyCode.USD)
                                                        .value(amount)
                                                        .build()
                                        )
                                        .build()
                        );
                        OrderRequest order = new OrderRequest(
                                OrderIntent.CAPTURE,
                                new AppContext.Builder()
                                        .userAction(UserAction.PAY_NOW)
                                        .build(),
                                purchaseUnits
                        );
                        createOrderActions.create(order, (CreateOrderActions.OnOrderCreated) null);
                    }
                },
                new OnApprove() {
                    @Override
                    public void onApprove(@NotNull Approval approval) {

                        approval.getOrderActions().capture(new OnCaptureComplete() {
                            @Override
                            public void onCaptureComplete(@NotNull CaptureOrderResult result) {
                                if (result == new CaptureOrderResult.Error("fail", "stupid")) {

                                } else {
                                    Intent intent = new Intent(ConfirmPaymentActivity.this, PaymentCompleteActivity.class);
                                    intent.putExtra("room", booking.getRoom().getType());
                                    intent.putExtra("roomImg", booking.getRoom().getImage().get(0));
                                    intent.putExtra("service", booking.getService().getName());
                                    intent.putExtra("serviceImg", booking.getService().getImage().get(0));
                                    intent.putExtra("checkInDate", booking.getCheckInDate());
                                    intent.putExtra("checkOutDate", booking.getCheckOutDate());
                                    intent.putExtra("total", booking.getTotalPrice());

                                    startActivity(intent);
                                }
                            }
                        });
                    }
                }
        );
    }

    private void getDulieu() {
        Intent intent = getIntent();
        String idRoom = intent.getStringExtra("idRoom");
        String idService = intent.getStringExtra("idService");
        int quantityGuest = intent.getIntExtra("quantityGuest", 0);
        int quantityChildren = intent.getIntExtra("quantityChildren", 0);
        String startDate = intent.getStringExtra("startDate");
        String endDate = intent.getStringExtra("endDate");
        String note = intent.getStringExtra("note");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        try {
            Date startDateD = sdf.parse(startDate);
            Date endDateD = sdf.parse(endDate);
            long diffInMillies = Math.abs(endDateD.getTime() - startDateD.getTime());
            long diffInDays = diffInMillies / (24 * 60 * 60 * 1000);
            dayNight = (int) diffInDays;
            Log.d("TAG", "getDulieu: " + diffInDays);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (idRoom != null) {
            ServiceApi.apiService.getInfoRoom(idRoom).enqueue(new Callback<Room>() {
                @Override
                public void onResponse(Call<Room> call, Response<Room> response) {
                    if (response.isSuccessful()) {
                        Glide.with(context).load(response.body().getImage().get(0)).into(imgRoomTT);
                        tvNameTT.setText(response.body().getType());
                        tvNight.setText(String.valueOf(dayNight + 1) + " night");
                        tvPriceTT.setText(String.valueOf(response.body().getPrice()) + " VNĐ /1 night");
                        tvDayTT.setText(startDate + "  to   " + endDate);
                        guestTT.setText(String.valueOf(quantityGuest + quantityChildren) + " Guest");

                        int priceRoom = response.body().getPrice();
                        int total = priceRoom * (dayNight + 1);
                        int totalToUsd = (int) (total / 24.280);

                        tvCost.setText(String.valueOf(priceRoom * 85 / 100));
                        tvCleaningFee.setText(String.valueOf(priceRoom * 10 / 100) + " VNĐ");
                        tvServiceFee.setText(String.valueOf(priceRoom * 5 / 100) + " VNĐ");
                        tvTotal.setText(String.valueOf(total));

                        Booking booking = new Booking();
                        booking.setRoom(response.body());
                        booking.setCheckInDate(startDate);
                        booking.setCheckOutDate(endDate);
                        booking.setNote(note);
                        booking.setTotalPrice(total);

                        btnPay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                addPaymentZalo(tvTotal.getText().toString(), booking);
                                addBooking(booking);

                            }
                        });

                        addPaymentPayPal(String.valueOf(totalToUsd), booking);


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

                    Glide.with(context).load(response.body().getImage().get(0)).into(imgRoomTT);

                    tvNameTT.setText(response.body().getName());
                    tvNight.setText(String.valueOf(dayNight + 1) + " Day");
                    tvPriceTT.setText(String.valueOf(response.body().getPrice()) + " VNĐ/1 person");
                    tvDayTT.setText(startDate + "  to   " + endDate);
                    guestTT.setText(String.valueOf(quantityGuest + quantityChildren) + " Guest");
                    typeNight.setText("Day");
                    typeTotal.setText("Deposit");

                    int priceRoom = response.body().getPrice() * (quantityGuest + quantityChildren);
                    int totalToUsd = (int) (priceRoom / 24.280);

                    tvCost.setText(String.valueOf(priceRoom));
                    tvCleaningFee.setText(" 0 VNĐ");
                    tvServiceFee.setText("0 VNĐ");
                    tvTotal.setText(String.valueOf(priceRoom));
                    Booking booking = new Booking();
                    booking.setService(response.body());
                    booking.setCheckInDate(startDate);
                    booking.setCheckOutDate(endDate);
                    booking.setNote(note);
                    booking.setTotalPrice(priceRoom);

                    btnPay.setText("Thanh toán tiền cọc qua ZaloPay ");
                    btnPay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addPaymentZalo(tvTotal.getText().toString(),booking);
                            addBooking(booking);
                        }
                    });

                    addPaymentPayPal(String.valueOf(totalToUsd), booking);
                }

                @Override
                public void onFailure(Call<Service> call, Throwable t) {

                }
            });
        }


    }

    private void addBooking(Booking booking) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("myPreferences", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        ServiceApi.apiService.addBooking("Bearer " + token, booking).enqueue(new Callback<Booking>() {
            @Override
            public void onResponse(Call<Booking> call, Response<Booking> response) {
                if (response.isSuccessful()) {

                }
            }

            @Override
            public void onFailure(Call<Booking> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }
}