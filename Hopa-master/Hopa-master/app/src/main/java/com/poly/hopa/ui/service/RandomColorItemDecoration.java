package com.poly.hopa.ui.service;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Random;

public class RandomColorItemDecoration extends RecyclerView.ItemDecoration {
    private final Paint backgroundPaint = new Paint();
    private final Random random = new Random();

    public RandomColorItemDecoration(Context context) {
        backgroundPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        // Đặt khoảng trống ở đây nếu cần
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(child);

            // Tạo màu nền ngẫu nhiên
            int color = getRandomColor();
            backgroundPaint.setColor(color);

            // Vẽ màu nền cho mỗi item
            c.drawRect(child.getLeft(), child.getTop(), child.getRight(), child.getBottom(), backgroundPaint);
        }
    }

    private int getRandomColor() {
        return Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }
}

