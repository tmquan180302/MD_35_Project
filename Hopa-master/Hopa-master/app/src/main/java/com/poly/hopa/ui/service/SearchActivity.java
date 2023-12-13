package com.poly.hopa.ui.service;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.poly.hopa.R;
import com.poly.hopa.adapter.SearchAdapter;
import com.poly.hopa.models.ServerRequest.ServerReqSearch;
import com.poly.hopa.api.ServiceApi;
import com.poly.hopa.models.ServerResponse.ServerResRoom;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    public SearchAdapter searchAdapter;
    private List<ServerResRoom> mlistSearchRoom;

    public ImageView imgBackSearch;
    public ImageView imgSearch;
    public ImageView img_filter;
    public EditText edtSearch;
    public RecyclerView rcvSearch;
    private CheckBox chkHighToLow;
    private CheckBox chkLowToHigh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        imgBackSearch = findViewById(R.id.img_BackSearch);
        imgSearch = findViewById(R.id.img_Search);
        img_filter = findViewById(R.id.img_filter);
        edtSearch = findViewById(R.id.idEdtSearch);
        rcvSearch = findViewById(R.id.rcvSearch);
        chkHighToLow = findViewById(R.id.chkHighToLow);
        chkLowToHigh = findViewById(R.id.chkLowToHigh);

        mlistSearchRoom = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvSearch.setLayoutManager(linearLayoutManager);
        searchAdapter = new SearchAdapter(mlistSearchRoom, this);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, R.color.black, (int) getResources().getDimension(R.dimen.divider_height));

        rcvSearch.addItemDecoration(itemDecoration);
        rcvSearch.setAdapter(searchAdapter);
        rcvSearch.addItemDecoration(new RecyclerView.ItemDecoration() {
        });
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // Xử lý tìm kiếm ở đây
                    String searchQuery = edtSearch.getText().toString();
                    searchByName(searchQuery);

                    // Ẩn bàn phím
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edtSearch.getWindowToken(), 0);

                    return true; // Đánh dấu sự kiện đã được xử lý
                }
                return false;
            }
        });

        imgBackSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchQuery = edtSearch.getText().toString();
                searchByName(searchQuery);

                // Ẩn bàn phím
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtSearch.getWindowToken(), 0);
            }
        });
        img_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Kiểm tra trạng thái của checkbox và hiển thị/ẩn nó
                if (chkHighToLow.getVisibility() == View.INVISIBLE) {
                    chkHighToLow.setVisibility(View.VISIBLE);
                    chkLowToHigh.setVisibility(View.VISIBLE);
                } else {
                    chkHighToLow.setVisibility(View.INVISIBLE);
                    chkLowToHigh.setVisibility(View.INVISIBLE);
                }
            }
        });

        chkHighToLow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    searchRoomHighToLow();
                    chkLowToHigh.setClickable(false);
                }else {
                    chkLowToHigh.setClickable(true);
                }
            }
        });

        chkLowToHigh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    searchRoomLowToHigh();
                    chkHighToLow.setClickable(false);
                }else {
                    chkHighToLow.setClickable(true);
                }
            }
        });

    }



    private void searchByName(String type) {
        ServerReqSearch serverReqSearch = new ServerReqSearch();

        serverReqSearch.setType(type);
        Log.d("TAG", "searchByName: "+type);
        ServiceApi.apiService.getSearchByName(serverReqSearch).enqueue(new Callback<List<ServerResRoom>>() {
            @Override
            public void onResponse(Call<List<ServerResRoom>> call, Response<List<ServerResRoom>> response) {
                if (response.isSuccessful()) {
                    Log.d("TAG", "onResponse: "+response.body());
                    mlistSearchRoom.clear();
                    mlistSearchRoom.addAll(response.body());
                    searchAdapter.notifyDataSetChanged();
                    Log.d("TAG", "onResponse: " + response.body());
                }
            }

            @Override
            public void onFailure(Call<List<ServerResRoom>> call, Throwable t) {
                Log.d("TAG", "onFailure: ");
            }
        });


    }

    private void searchRoomHighToLow() {
    ServiceApi.apiService.searchRoomHighToLow().enqueue(new Callback<List<ServerResRoom>>() {
        @Override
        public void onResponse(Call<List<ServerResRoom>> call, Response<List<ServerResRoom>> response) {
            if (response.isSuccessful()) {
                Log.d("TAG", "onResponse: "+response.body());
                mlistSearchRoom.clear();
                mlistSearchRoom.addAll(response.body());
                searchAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onFailure(Call<List<ServerResRoom>> call, Throwable t) {

        }
    });

    }

    private void searchRoomLowToHigh() {
        ServiceApi.apiService.searchRoomLowToHigh().enqueue(new Callback<List<ServerResRoom>>() {
            @Override
            public void onResponse(Call<List<ServerResRoom>> call, Response<List<ServerResRoom>> response) {
                if (response.isSuccessful()) {
                    mlistSearchRoom.clear();
                    mlistSearchRoom.addAll(response.body());
                    searchAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<ServerResRoom>> call, Throwable t) {

            }
        });
    }
    public class DividerItemDecoration extends RecyclerView.ItemDecoration {
        private final Paint paint;

        public DividerItemDecoration(Context context, int color, int height) {
            paint = new Paint();
            paint.setColor(context.getResources().getColor(color));
            paint.setStrokeWidth(height);
        }

        @Override
        public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + (int) paint.getStrokeWidth();

                c.drawLine(left, top, right, bottom, paint);
            }
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            // Không thêm khoảng cách cho phần tử cuối cùng
            if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
                outRect.bottom = (int) paint.getStrokeWidth();
            }
        }
    }
}
