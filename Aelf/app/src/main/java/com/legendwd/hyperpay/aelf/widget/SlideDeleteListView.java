package com.legendwd.hyperpay.aelf.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.legendwd.hyperpay.aelf.R;


/**
 * 2018.11.14
 *
 * @author Huanglinqing
 */
public class SlideDeleteListView extends ListView {

    private LayoutInflater layoutInflater;
    private View view;
    private Button btn_delete;
    private PopupWindow popupWindow;
    private int popupWindowHeight;
    private int popuWindowWidth;
    private int startX;
    private int startY;
    private int pressX;
    private int pressY;
    private int moveX;
    private int moveY;
    private int dx;
    private int dy;
    private int currentPosition;
    private View currentView;
    private boolean isSlide = false;
    private BtnDeleteListern btnDeleteListern;

    /**
     * 滑动最小距离
     */
    private int touchSlop;

    public SlideDeleteListView(Context context) {
        super(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    public SlideDeleteListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SlideDeleteListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 一些初始化操作
     *
     * @param context
     */
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private void init(Context context) {
        layoutInflater = LayoutInflater.from(context);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        view = layoutInflater.inflate(R.layout.slide_item, null);
        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.getContentView().measure(0, 0);
        popupWindowHeight = popupWindow.getContentView().getMeasuredHeight();
        popuWindowWidth = popupWindow.getContentView().getMeasuredWidth();
        btn_delete = view.findViewById(R.id.delete);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        startX = (int) ev.getX();
        startY = (int) ev.getY();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                pressX = startX;
                pressY = startY;
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    return false;
                }
                currentPosition = pointToPosition(pressX, pressY);
                currentView = getChildAt(currentPosition - getFirstVisiblePosition());
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                moveX = startX;
                moveY = startY;
                dx = moveX - pressX;
                dy = moveY - pressY;
                if (dx < 0 && Math.abs(dx) > touchSlop && Math.abs(dy) < touchSlop) {
                    isSlide = true;
                }
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if (isSlide) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    if (currentView == null) {
                        break;
                    }
                    int outLocation[] = new int[2];
                    currentView.getLocationOnScreen(outLocation);
                    popupWindow.update();
                    popupWindow.showAtLocation(currentView,
                            Gravity.LEFT | Gravity.TOP,
                            outLocation[0] + currentView.getWidth(),
                            outLocation[1] + currentView.getHeight() - popupWindowHeight);
                    btn_delete.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            isSlide = false;
                            btnDeleteListern.deleteOnCliclListern(currentPosition);
                            popupWindow.dismiss();
                            MotionEvent motionEvent = MotionEvent.obtain(System.currentTimeMillis(), System.currentTimeMillis() + 10
                                    , MotionEvent.ACTION_UP, ev.getRawX(), ev.getRawY(), 0);
                            dispatchTouchEvent(motionEvent);
                        }
                    });
                    break;
                case MotionEvent.ACTION_UP:
                    isSlide = false;
                    break;
                default:
                    break;
            }
            return true;
        }


        return super.onTouchEvent(ev);

    }


    public void setBtnDelClickListener(BtnDeleteListern btnDeleteListern) {
        this.btnDeleteListern = btnDeleteListern;
    }
}
