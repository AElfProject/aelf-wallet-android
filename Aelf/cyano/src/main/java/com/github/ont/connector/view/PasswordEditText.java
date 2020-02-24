/*
 * **************************************************************************************
 *  Copyright © 2014-2018 Ontology Foundation Ltd.
 *  All rights reserved.
 *
 *  This software is supplied only under the terms of a license agreement,
 *  nondisclosure agreement or other written agreement with Ontology Foundation Ltd.
 *  Use, redistribution or other disclosure of any parts of this
 *  software is prohibited except in accordance with the terms of such written
 *  agreement with Ontology Foundation Ltd. This software is confidential
 *  and proprietary information of Ontology Foundation Ltd.
 *
 * **************************************************************************************
 */

package com.github.ont.connector.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;

import com.github.ont.connector.R;

import java.util.ArrayList;

/**
 * 封装成lib，方便直接调用
 * 仿照支付宝的密码输入控件
 * Created by hua on 2016/7/24.
 */
public class PasswordEditText extends View {
    private final int DEFAULT_COUNT = 6;
    private InputMethodManager input;//输入法管理
    private ArrayList<Integer> result;//输入结果保存
    private int count;//密码位数
    private int size;//默认每一格的大小
    private Paint mBorderPaint;//边界画笔
    private Paint mDotPaint;//掩盖点的画笔
    private int mBorderColor;//边界颜色
    private int mDotColor;//掩盖点的颜色
    private RectF mRoundRect;//外面的圆角矩形
    private int mRoundRadius;//圆角矩形的圆角程度
    private float mFocusLineLength;//
    private ObjectAnimator mFocusAnim;//焦点转换动画
    private InputCallBack inputCallBack;//输入完成的回调
    private int divideW;// 间隔宽度
    private int viewW;
    private int viewH;

    public PasswordEditText(Context context) {
        super(context);
        init(null);
    }

    public PasswordEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public PasswordEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    /**
     * 初始化相关参数
     */
    void init(AttributeSet attrs) {
        final float dp = getResources().getDisplayMetrics().density;
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
        input = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        result = new ArrayList<>();
        if (attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.PswInputView);
            mBorderColor = ta.getColor(R.styleable.PswInputView_border_color, Color.BLACK);
            mDotColor = ta.getColor(R.styleable.PswInputView_dot_color, Color.GREEN);
            count = ta.getInt(R.styleable.PswInputView_count, DEFAULT_COUNT);
            divideW = ta.getInt(R.styleable.PswInputView_divide, (int) (dp * 10));
            if (count < 1) {
                count = 1;
            }
            ta.recycle();
        } else {
            mBorderColor = Color.BLACK;
            mDotColor = Color.GRAY;
            count = DEFAULT_COUNT;//默认6位密码
            divideW = (int) (dp * 10);
        }
        size = (int) (dp * 40);//默认40dp一格
        //color
        mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mBorderPaint.setStrokeWidth(2);
        mBorderPaint.setStyle(Paint.Style.FILL);
        mBorderPaint.setColor(mBorderColor);
        mDotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDotPaint.setStrokeWidth(3);
        mDotPaint.setStyle(Paint.Style.FILL);
        mDotPaint.setColor(mDotColor);
        mRoundRect = new RectF();
        mRoundRadius = (int) (5 * dp);
        mFocusLineLength = 0;
        this.setOnKeyListener(new MyKeyListener());
//        this.setBackgroundResource(R.drawable.bg_password_edittext);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int w = measureWidth(widthMeasureSpec);
//        int h = measureHeight(heightMeasureSpec);
//        int wsize = MeasureSpec.getSize(widthMeasureSpec);
//        int hsize = MeasureSpec.getSize(heightMeasureSpec);
//        int divide;
//        if (count >= 2) {
//            divide = (count - 1) * divideW;
//        } else {
//            divide = 0;
//        }
//         w = h * count + divide;
//        if(h == -1) {
//            if(w == -1) {
//                // 宽高未定
//                w = size * count + divide;
//                h = size;
//            } else {
//                // 宽已定高未定
//                h = (w - divide) / count;
//            }
//        } else {
//            // 以高为准，重新定宽
//            w = h * count + divide;
//        }
//
//
//        //宽度没指定,但高度指定
//        if (w == -1) {
//            if (h != -1) {
//                w = h * count;//宽度=高*数量
//                size = h;
//            } else {//两个都不知道,默认宽高
//                w = size * count;
//                h = size;
//            }
//        } else {//宽度已知
//            if (h == -1) {//高度不知道
//                h = w / count;
//                size = h;
//            }
//        }
//        setMeasuredDimension(Math.min(w, wsize), Math.min(h, hsize));
//        setMeasuredDimension(w, h);
//        viewW = w;
//        viewH = h;
//        Log.d("test", w + "*" + h);
        int w = measureWidth(widthMeasureSpec);
        int h = measureHeight(heightMeasureSpec);
        int wsize = MeasureSpec.getSize(widthMeasureSpec);
        int hsize = MeasureSpec.getSize(heightMeasureSpec);
        int divide;
        if (count >= 2) {
            divide = (count - 1) * divideW;
        } else {
            divide = 0;
        }
        //���ûָ��,���߶�ָ��
        if (w == -1) {
            if (h != -1) {
                w = h * count + divide;
                size = h;
            } else {//��������֪��,Ĭ�Ͽ��
                w = size * count + divide;
                h = size;
            }
        } else {//�����֪
            if (h == -1) {//�߶Ȳ�֪��
                h = (w - divide) / count;
                size = h;
            } else {
                if (h * count <= w) {
                    size = h;
                    divideW = (w - h * count) / (count - 1);
                }
            }
        }
        setMeasuredDimension(Math.min(w, wsize), Math.min(h, hsize));
    }

    private int clampSize(int size, int spec) {
        int specMode = MeasureSpec.getMode(spec);
        int specSize = MeasureSpec.getSize(spec);
        switch (specMode) {
            case MeasureSpec.EXACTLY: {
                return specSize;
            }
            case MeasureSpec.AT_MOST: {
                return Math.min(size, specSize);
            }
            case MeasureSpec.UNSPECIFIED:
            default: {
                return size;
            }
        }
    }


    private int measureWidth(int widthMeasureSpec) {
        //宽度
        int wmode = MeasureSpec.getMode(widthMeasureSpec);
        int wsize = MeasureSpec.getSize(widthMeasureSpec);
        if (wmode == MeasureSpec.AT_MOST) {//wrap_content
            return -1;
        }
        return wsize;
    }

    private int measureHeight(int heightMeasureSpec) {
        //高度
        int hmode = MeasureSpec.getMode(heightMeasureSpec);
        int hsize = MeasureSpec.getSize(heightMeasureSpec);
        if (hmode == MeasureSpec.AT_MOST) {//wrap_content
            return -1;
        }
        return hsize;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {//点击控件弹出输入键盘
            requestFocus();
            input.showSoftInput(this, InputMethodManager.RESULT_UNCHANGED_SHOWN);
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        if (mFocusAnim != null) {
            mFocusAnim.end();
        }
        if (gainFocus) {
            mFocusAnim = ObjectAnimator.ofFloat(this, "FocusLine", mFocusLineLength, (float) (getWidth() - 2 * mRoundRadius));
            input.showSoftInput(this, InputMethodManager.RESULT_UNCHANGED_SHOWN);
        } else {
            mFocusAnim = ObjectAnimator.ofFloat(this, "FocusLine", mFocusLineLength, 0);
            input.hideSoftInputFromInputMethod(this.getWindowToken(), 0);
        }
        mFocusAnim.setDuration(1000).setInterpolator(new OvershootInterpolator());
        mFocusAnim.start();
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (!hasWindowFocus) {
            input.hideSoftInputFromWindow(this.getWindowToken(), 0);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        final int width = getWidth()- 2;
//        final int height = getHeight() - 2;
//        Log.d("test", width + "*" + height);
        //先画个圆角矩形
//        mRoundRect.set(0, 0, width, height);
//        canvas.drawRoundRect(mRoundRect, mRoundRadius, mRoundRadius, mBorderPaint);
        //画分割线
        RectF f;
        for (int i = 0; i < count; i++) {
//            f = new RectF();
            int left = i * (size + divideW);
            int right = left + size;
//            f.set(left, 0, right, size);
//            canvas.drawRoundRect(f, mRoundRadius, mRoundRadius, mBorderPaint);
            canvas.drawLine(left, size, right, size, mBorderPaint);
        }

        //画掩盖点,
        // 这是前面定义的变量 private ArrayList<Integer> result;//输入结果保存
        int dotRadius = size / 6;//圆圈占格子的三分之一
        for (int i = 0; i < result.size(); i++) {
            final float x = (float) (size * (i + 0.5) + divideW * i);
            final float y = size / 2;
            canvas.drawCircle(x, y, dotRadius, mDotPaint);
        }
        //画提示线
    }

    @Override
    public boolean onCheckIsTextEditor() {
        return true;
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        outAttrs.inputType = InputType.TYPE_CLASS_NUMBER;//输入类型为数字
        outAttrs.imeOptions = EditorInfo.IME_ACTION_DONE;
        return new MyInputConnection(this, false);
    }

    public void setInputCallBack(InputCallBack inputCallBack) {
        this.inputCallBack = inputCallBack;
    }

    public void clearResult() {
        result.clear();
        invalidate();
    }

    private void setFocusLine(float length) {
        mFocusLineLength = length;
        invalidate();
    }

    public String getResult() {
        if (null == result || result.size() == 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (int i : result) {
            sb.append(i);
        }
        return sb.toString();
    }

    public interface InputCallBack {
        void onInputFinish(String result);
    }

    class MyInputConnection extends BaseInputConnection {
        public MyInputConnection(View targetView, boolean fullEditor) {
            super(targetView, fullEditor);
        }

        @Override
        public boolean commitText(CharSequence text, int newCursorPosition) {
            //这里是接受输入法的文本的，我们只处理数字，所以什么操作都不做
            return super.commitText(text, newCursorPosition);
        }

        @Override
        public boolean deleteSurroundingText(int beforeLength, int afterLength) {
            //软键盘的删除键 DEL 无法直接监听，自己发送del事件
            if (beforeLength == 1 && afterLength == 0) {
                return super.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL))
                        && super.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
            }
            return super.deleteSurroundingText(beforeLength, afterLength);
        }
    }

    /**
     * 按键监听器
     */
    class MyKeyListener implements OnKeyListener {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (event.isShiftPressed()) {//处理*#等键
                    return false;
                }
                if (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9) {//只处理数字
                    if (result.size() < count) {
                        result.add(keyCode - 7);
                        invalidate();
                        ensureFinishInput();
                    }
                    return true;
                }
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (!result.isEmpty()) {//不为空，删除最后一个
                        result.remove(result.size() - 1);
                        invalidate();
                    }
                    return true;
                }
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    ensureFinishInput();
                    return true;
                }
            }
            return false;
        }//onKey

        /**
         * 判断是否输入完成，输入完成后调用callback
         */
        void ensureFinishInput() {
            if (result.size() == count && inputCallBack != null) {//输入完成
                StringBuffer sb = new StringBuffer();
                for (int i : result) {
                    sb.append(i);
                }
                inputCallBack.onInputFinish(sb.toString());
            }
        }
    }//in class

}
