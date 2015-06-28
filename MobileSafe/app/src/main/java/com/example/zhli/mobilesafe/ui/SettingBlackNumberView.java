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
 * 自定义的组合控件(两个 TextView，一个 CheckBox， 还有一个 View)
 */
public class SettingBlackNumberView extends RelativeLayout {

    private CheckBox cb_status;
    private TextView tv_title;
    private TextView tv_desc;

    public SettingBlackNumberView(Context context) {
        super(context);
        iniView(context);
    }

    public SettingBlackNumberView(Context context, AttributeSet attrs) {
        super(context, attrs);
        iniView(context);
    }

    public SettingBlackNumberView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        iniView(context);
    }

    /**
     * 初始化布局文件
     * @param context
     */
    private void iniView(Context context) {
        // 把一个布局文件 --> View, 并加载在 SettingSIMBindView
        View.inflate(context, R.layout.setting_item_view, SettingBlackNumberView.this);

        cb_status = (CheckBox) this.findViewById(R.id.cb_status);
        tv_title = (TextView) this.findViewById(R.id.tv_title);
        tv_desc = (TextView) this.findViewById(R.id.tv_desc);

        tv_title.setText("黑名单拦截设置");
    }

    /**
     * 校验组合控件是否有选中
     */
    public boolean isChecked() {
        return cb_status.isChecked();
    }

    /**
     * 设置组合控件的状态
     */
    public void setChecked(boolean checked) {
        if(checked) {
            setDesc("黑名单拦截功能已经开启");
        } else {
            setDesc("黑名单拦截功能已经关闭");
        }
        cb_status.setChecked(checked);
    }

    /**
     * 组合控件的描述信息
     */
    private void setDesc(String text) {
        tv_desc.setText(text);
    }
}
