package com.example.zhli.lottery.view;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 第一个简单页面
 * Created by zhli on 2015/2/10.
 */
public class FirstUI {

    private Context context;

    public FirstUI(Context context) {
        super();
        this.context = context;
    }

    /**
     * 获取需要在中间容器加载的控件
     * @return
     */
    public View getChild() {
        TextView textView = new TextView(context);
        ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
        layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        textView.setLayoutParams(layoutParams);
        textView.setBackgroundColor(Color.BLUE);
        textView.setText("first UI");

        return textView;
    }
}
