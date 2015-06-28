package com.example.zhli.mobilesafe.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhli.mobilesafe.R;

/**
 * Created by zhli on 2015/1/26.
 * 自定义的组合控件(两个 TextView，一个 ImageView， 还有一个 View)
 */
public class SettingClickView extends RelativeLayout {

    private TextView tv_title;
    private TextView tv_desc;

    public SettingClickView(Context context) {
        super(context);
        iniView(context);
    }

    public SettingClickView(Context context, AttributeSet attrs) {
        super(context, attrs);
        iniView(context);
    }

    public SettingClickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        iniView(context);
    }

    /**
     * 初始化布局文件
     * @param context
     */
    private void iniView(Context context) {
        // 把一个布局文件 --> View, 并加载在 SettingItemView
        View.inflate(context, R.layout.setting_click_view, SettingClickView.this);

        tv_title = (TextView) this.findViewById(R.id.tv_title);
        tv_desc = (TextView) this.findViewById(R.id.tv_desc);
    }

    /**
     * 组合控件的标题
     */
    public void setTitle(String title) {
        tv_title.setText(title);
    }

    /**
     * 组合控件的描述信息
     */
    public void setDesc(String desc) {
        tv_desc.setText(desc);
    }
}
