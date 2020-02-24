/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.legendwd.hyperpay.aelf.util.zxing.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.google.zxing.ResultPoint;
import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.util.zxing.camera.CameraManager;

import java.util.Collection;
import java.util.HashSet;

/**
 * This mView is overlaid on top of the camera preview. It adds the viewfinder
 * rectangle and partial transparency outside it, as well as the laser scanner
 * animation and result points.
 */
public final class ViewfinderView extends View {
    private static final String TAG = "DLog";
    /**
     * À¢–¬ΩÁ√Êµƒ ±º‰
     */
    private static final long ANIMATION_DELAY = 10L;
    private static final int OPAQUE = 0xFF;
    /**
     * Àƒ∏ˆ¬Ã…´±ﬂΩ«∂‘”¶µƒøÌ∂»
     */
    private static final int CORNER_WIDTH = 10;
    /**
     * …®√ËøÚ÷–µƒ÷–º‰œﬂµƒøÌ∂»
     */
    private static final int MIDDLE_LINE_WIDTH = 6;
    /**
     * …®√ËøÚ÷–µƒ÷–º‰œﬂµƒ”Î…®√ËøÚ◊Û”“µƒº‰œ∂
     */
    private static final int MIDDLE_LINE_PADDING = 5;
    /**
     * ÷–º‰ƒ«Ãıœﬂ√ø¥ŒÀ¢–¬“∆∂Øµƒæ‡¿Î
     */
    private static final int SPEEN_DISTANCE = 5;
    /**
     * ◊÷ÃÂ¥Û–°
     */
    private static final int TEXT_SIZE = 16;
    /**
     * ◊÷ÃÂæ‡¿Î…®√ËøÚœ¬√Êµƒæ‡¿Î
     */
    private static final int TEXT_PADDING_TOP = 30;
    /**
     *  ÷ª˙µƒ∆¡ƒª√‹∂»
     */
    private static float density;
    private final int maskColor;
    private final int resultColor;
    private final int resultPointColor;
    boolean isFirst;
    /**
     * Àƒ∏ˆ¬Ã…´±ﬂΩ«∂‘”¶µƒ≥§∂»
     */
    private int ScreenRate;
    /**
     * ª≠± ∂‘œÛµƒ“˝”√
     */
    private Paint paint;
    /**
     * ÷–º‰ª¨∂Øœﬂµƒ◊Ó∂•∂ÀŒª÷√
     */
    private int slideTop;
    /**
     * ÷–º‰ª¨∂Øœﬂµƒ◊Óµ◊∂ÀŒª÷√
     */
    private int slideBottom;
    /**
     * Ω´…®√Ëµƒ∂˛Œ¨¬Î≈ƒœ¬¿¥£¨’‚¿Ô√ª”–’‚∏ˆπ¶ƒ‹£¨‘› ±≤ªøº¬«
     */
    private Bitmap resultBitmap;
    private Collection<ResultPoint> possibleResultPoints;
    private Collection<ResultPoint> lastPossibleResultPoints;

    public ViewfinderView(Context context, AttributeSet attrs) {
        super(context, attrs);

        density = context.getResources().getDisplayMetrics().density;
        //Ω´œÒÀÿ◊™ªª≥…dp
        ScreenRate = (int) (20 * density);

        paint = new Paint();
        Resources resources = getResources();
        maskColor = resources.getColor(R.color.viewfinder_mask);
        resultColor = resources.getColor(R.color.result_view);

        resultPointColor = resources.getColor(R.color.possible_result_points);
        possibleResultPoints = new HashSet<ResultPoint>(5);
    }

    @Override
    public void onDraw(Canvas canvas) {
        //÷–º‰µƒ…®√ËøÚ£¨ƒ„“™–ﬁ∏ƒ…®√ËøÚµƒ¥Û–°£¨»•CameraManager¿Ô√Ê–ﬁ∏ƒ
        Rect frame = CameraManager.get().getFramingRect();
        if (frame == null) {
            return;
        }

        //≥ı ºªØ÷–º‰œﬂª¨∂Øµƒ◊Ó…œ±ﬂ∫Õ◊Óœ¬±ﬂ
        if (!isFirst) {
            isFirst = true;
            slideTop = frame.top;
            slideBottom = frame.bottom;
        }

        //ªÒ»°∆¡ƒªµƒøÌ∫Õ∏ﬂ
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        paint.setColor(resultBitmap != null ? resultColor : maskColor);

        //ª≠≥ˆ…®√ËøÚÕ‚√Êµƒ“ı”∞≤ø∑÷£¨π≤Àƒ∏ˆ≤ø∑÷£¨…®√ËøÚµƒ…œ√ÊµΩ∆¡ƒª…œ√Ê£¨…®√ËøÚµƒœ¬√ÊµΩ∆¡ƒªœ¬√Ê
        //…®√ËøÚµƒ◊Û±ﬂ√ÊµΩ∆¡ƒª◊Û±ﬂ£¨…®√ËøÚµƒ”“±ﬂµΩ∆¡ƒª”“±ﬂ
        canvas.drawRect(0, 0, width, frame.top, paint);
        canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
        canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1,
                paint);
        canvas.drawRect(0, frame.bottom + 1, width, height, paint);


        if (resultBitmap != null) {
            // Draw the opaque result bitmap over the scanning rectangle
            paint.setAlpha(OPAQUE);
            canvas.drawBitmap(resultBitmap, frame.left, frame.top, paint);
        } else {

            //ª≠…®√ËøÚ±ﬂ…œµƒΩ«£¨◊‹π≤8∏ˆ≤ø∑÷
            paint.setColor(Color.GREEN);
            canvas.drawRect(frame.left, frame.top, frame.left + ScreenRate,
                    frame.top + CORNER_WIDTH, paint);
            canvas.drawRect(frame.left, frame.top, frame.left + CORNER_WIDTH, frame.top
                    + ScreenRate, paint);
            canvas.drawRect(frame.right - ScreenRate, frame.top, frame.right,
                    frame.top + CORNER_WIDTH, paint);
            canvas.drawRect(frame.right - CORNER_WIDTH, frame.top, frame.right, frame.top
                    + ScreenRate, paint);
            canvas.drawRect(frame.left, frame.bottom - CORNER_WIDTH, frame.left
                    + ScreenRate, frame.bottom, paint);
            canvas.drawRect(frame.left, frame.bottom - ScreenRate,
                    frame.left + CORNER_WIDTH, frame.bottom, paint);
            canvas.drawRect(frame.right - ScreenRate, frame.bottom - CORNER_WIDTH,
                    frame.right, frame.bottom, paint);
            canvas.drawRect(frame.right - CORNER_WIDTH, frame.bottom - ScreenRate,
                    frame.right, frame.bottom, paint);


            //ªÊ÷∆÷–º‰µƒœﬂ,√ø¥ŒÀ¢–¬ΩÁ√Ê£¨÷–º‰µƒœﬂÕ˘œ¬“∆∂ØSPEEN_DISTANCE
            slideTop += SPEEN_DISTANCE;
            if (slideTop >= frame.bottom) {
                slideTop = frame.top;
            }
            canvas.drawRect(frame.left + MIDDLE_LINE_PADDING, slideTop - MIDDLE_LINE_WIDTH / 2, frame.right - MIDDLE_LINE_PADDING, slideTop + MIDDLE_LINE_WIDTH / 2, paint);


            //ª≠…®√ËøÚœ¬√Êµƒ◊÷
            paint.setColor(Color.WHITE);
            paint.setTextSize(TEXT_SIZE * density);
            paint.setAlpha(0x40);
            paint.setTypeface(Typeface.create("System", Typeface.BOLD));
            canvas.drawText("", frame.left, (float) (frame.bottom + (float) TEXT_PADDING_TOP * density), paint);


            Collection<ResultPoint> currentPossible = possibleResultPoints;
            Collection<ResultPoint> currentLast = lastPossibleResultPoints;
            if (currentPossible.isEmpty()) {
                lastPossibleResultPoints = null;
            } else {
                possibleResultPoints = new HashSet<ResultPoint>(5);
                lastPossibleResultPoints = currentPossible;
                paint.setAlpha(OPAQUE);
                paint.setColor(resultPointColor);
                for (ResultPoint point : currentPossible) {
                    canvas.drawCircle(frame.left + point.getX(), frame.top
                            + point.getY(), 6.0f, paint);
                }
            }
            if (currentLast != null) {
                paint.setAlpha(OPAQUE / 2);
                paint.setColor(resultPointColor);
                for (ResultPoint point : currentLast) {
                    canvas.drawCircle(frame.left + point.getX(), frame.top
                            + point.getY(), 3.0f, paint);
                }
            }


            //÷ªÀ¢–¬…®√ËøÚµƒƒ⁄»›£¨∆‰À˚µÿ∑Ω≤ªÀ¢–¬
            postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top,
                    frame.right, frame.bottom);

        }
    }

    public void drawViewfinder() {
        resultBitmap = null;
        invalidate();
    }

    /**
     * Draw a bitmap with the result points highlighted instead of the live
     * scanning display.
     *
     * @param barcode An image of the decoded barcode.
     */
    public void drawResultBitmap(Bitmap barcode) {
        resultBitmap = barcode;
        invalidate();
    }

    public void addPossibleResultPoint(ResultPoint point) {
        possibleResultPoints.add(point);
    }

}
