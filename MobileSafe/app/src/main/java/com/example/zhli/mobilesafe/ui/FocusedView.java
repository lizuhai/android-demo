package com.example.zhli.mobilesafe.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 自定义 TextView，一出生就有焦点
 */
public class FocusedView extends TextView {

    public FocusedView(Context context) {
        super(context);
    }

    public FocusedView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FocusedView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 当前并没有焦点，只是欺骗 android 系统
     * @return 焦点
     */
    @Override
    public boolean isFocused() {
        return true;
    }


}
