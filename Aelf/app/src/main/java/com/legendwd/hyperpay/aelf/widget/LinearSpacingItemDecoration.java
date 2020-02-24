package com.legendwd.hyperpay.aelf.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.legendwd.hyperpay.aelf.util.ScreenUtils;

public class LinearSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int dividerHeight;
    private Paint dividerPaint;

    public LinearSpacingItemDecoration(Context context, int height) {
        dividerPaint = new Paint();
        dividerPaint.setColor(Color.TRANSPARENT);
        dividerHeight = ScreenUtils.dip2px(context, height);
    }

    public LinearSpacingItemDecoration(Context context, int height, int color) {
        dividerPaint = new Paint();
        dividerPaint.setColor(color);
        dividerHeight = ScreenUtils.dip2px(context, height);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = dividerHeight;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        for (int i = 0; i < childCount - 1; i++) {
            View view = parent.getChildAt(i);
            float top = view.getBottom();
            float bottom = view.getBottom() + dividerHeight;
            c.drawRect(left, top, right, bottom, dividerPaint);
        }
    }

}

