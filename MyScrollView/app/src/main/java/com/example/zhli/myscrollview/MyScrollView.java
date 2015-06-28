package com.example.zhli.myscrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 *
 * Created by zhli on 2015/2/3.
 */
public class MyScrollView extends ViewGroup {

    private Context ctx;
    private int currId; // 当前位置(下标)

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.ctx = context;
        initView();
    }

    private void initView() {
        myScroller = new MyScroller(ctx);
        detector = new GestureDetector(ctx, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                // 移动屏幕
                scrollBy((int) distanceX, 0);
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        });
    }

    /**
     * 对 view 进行布局，确定子 view 的位置
     * @param changed true：布局发生了变化
     * @param l,t,r,b viewgroup 在父 view 中的位置
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            view.layout(0 + i * getWidth(), 0, getWidth() * (i + 1), getHeight()); // 指定子view 的位置
        }
    }

    // 手势识别的工具类
    private GestureDetector detector;

    private int firstX; // down 时候的坐标
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                firstX = (int) event.getX();
                break;
            case MotionEvent.ACTION_UP:
                int tempId = 0;
                if (event.getX() - firstX > getWidth() / 2) {   // 手指右滑超过屏幕一半
                    tempId = currId - 1;
                } else if (firstX - event.getX() > getWidth() / 2) { // 手指左滑超过屏幕一半
                    tempId = currId + 1;
                } else {
                    tempId = currId;
                }
                moveToDest(tempId);
                break;
            default:
                break;
        }
        return true;
    }

    //  计算位移距离的工具类
    private MyScroller myScroller;
    /**
     * 移动到指定屏幕上
     */
    private void moveToDest(int nextId) {
        currId = (nextId > 0) ? nextId : 0;
        currId = (nextId < getChildCount() - 1) ? nextId : (getChildCount() - 1);
        scrollTo(currId * getWidth(), 0);   // 瞬间移动

        int distance = currId * getWidth() - getScrollX();  // 要移动的距离 = 最终位置 - 现在的位置

        myScroller.startScroll(getScrollX(), 0, distance, 0);
        invalidate();       // 刷新当前 view，会导致 onDraw() 和 computeScroll() 方法的执行
    }

    /**
     * invalidate() 刷新当前 view，会导致 onDraw() 和 computeScroll() 方法的执行
     */
    @Override
    public void computeScroll() {
        if (myScroller.computeScrollOffset()) {
            int newX = (int) myScroller.getCurrX();
            System.out.println(newX);
            scrollTo(newX, 0);
            invalidate();
        }
    }
}
