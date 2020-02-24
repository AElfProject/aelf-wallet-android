package com.legendwd.hyperpay.aelf.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

/**
 * 图片工具
 */
public class DrawableUtil {

    public static Bitmap translatBitmap(Bitmap bitmap, int color) {

//      BitmapDrawable mBitmapDrawable = (BitmapDrawable) mContext.getResources().getDrawable(R.drawable.enemy_infantry_ninja);  
//      Bitmap mBitmap = mBitmapDrawable.getBitmap();  

        //BitmapDrawable的getIntrinsicWidth（）方法，Bitmap的getWidth（）方法  
        //注意这两个方法的区别  
        //Bitmap mAlphaBitmap = Bitmap.createBitmap(mBitmapDrawable.getIntrinsicWidth(), mBitmapDrawable.getIntrinsicHeight(), Config.ARGB_8888);  
        Bitmap mAlphaBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);

        Canvas mCanvas = new Canvas(mAlphaBitmap);
        Paint mPaint = new Paint();

        mPaint.setColor(color);
        //从原位图中提取只包含alpha的位图  
        Bitmap alphaBitmap = bitmap.extractAlpha();
        //在画布上（mAlphaBitmap）绘制alpha位图  
        mCanvas.drawBitmap(alphaBitmap, 0, 0, mPaint);

        return mAlphaBitmap;
    }

    public static Drawable getStateListDrawable(Drawable state1, Drawable state2) {
        StateListDrawable drawable = new StateListDrawable();
        // Non focused states
        drawable.addState(
                new int[]{-android.R.attr.state_focused,
                        -android.R.attr.state_selected,
                        -android.R.attr.state_pressed}, state1);
        drawable.addState(new int[]{-android.R.attr.state_focused,
                        android.R.attr.state_selected, -android.R.attr.state_pressed},
                state2);
        // Focused states
        drawable.addState(
                new int[]{android.R.attr.state_focused,
                        -android.R.attr.state_selected,
                        -android.R.attr.state_pressed}, state2);
        drawable.addState(new int[]{android.R.attr.state_focused,
                        android.R.attr.state_selected, -android.R.attr.state_pressed},
                state2);
        // Pressed
        drawable.addState(new int[]{android.R.attr.state_selected,
                android.R.attr.state_pressed}, state2);
        drawable.addState(new int[]{android.R.attr.state_pressed}, state2);

        return drawable;
    }
}
