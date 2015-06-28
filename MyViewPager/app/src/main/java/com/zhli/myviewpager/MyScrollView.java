package com.zhli.myviewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zhli on 2015/6/28.
 */
public class MyScrollView extends ViewGroup {

    private Context context;
    private GestureDetector gestureDetector;

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        gestureDetector = new GestureDetector(context, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent motionEvent) {
                return false;
            }
            @Override
            public void onShowPress(MotionEvent motionEvent) {
            }
            @Override
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                return false;
            }
            @Override
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                scrollBy((int) v, 0);
                return false;
            }
            @Override
            public void onLongPress(MotionEvent motionEvent) {
            }

            @Override
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                return false;
            }
        });

    }


    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        for (int j = 0; j < getChildCount(); j++) {
            View v = getChildAt(j);
            v.layout(getWidth() * j, 0, getWidth() * (j + 1), getHeight());
        }
    }

    /**
     * down时候的 x 坐标
     */
    private int lastX;
    /**
     * 当前页
     */
    private int currId;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = (int) event.getX();
                break;
            case MotionEvent.ACTION_UP:
                if (event.getX() - lastX > getWidth() / 2) {
                    currId --;
                } else if (-event.getX() + lastX > getWidth() / 2) {
                    currId ++;
                }
                move2Dest(currId);
                break;
            default:
                break;
        }
        return true;
    }

    private void move2Dest(int id) {
        currId = id < 0 ? 0 : id;
        currId = id > getChildCount() - 1 ? getChildCount() - 1 : id;
        scrollTo(currId * getWidth(), 0);
    }
}
