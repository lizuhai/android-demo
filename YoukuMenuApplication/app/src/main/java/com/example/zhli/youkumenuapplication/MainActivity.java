package com.example.zhli.youkumenuapplication;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.zhli.youkumenuapplication.utils.Animations;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private ImageView icon_menu;
    private ImageView icon_home;
    private RelativeLayout level1;
    private RelativeLayout level2;
    private RelativeLayout level3;
    private RelativeLayout level4;
    private RelativeLayout level5;
    private RelativeLayout level6;
    private RelativeLayout level7;

    private boolean isLevel3Show = true;   // 3 级菜单是否显示状态
    private boolean isLevel2Show = true;   // 2 级菜单是否显示状态
    private boolean isLevel1Show = true;   // 1 级菜单是否显示状态

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        icon_menu = (ImageView) findViewById(R.id.icon_menu);
        icon_home = (ImageView) findViewById(R.id.icon_home);

        level1 = (RelativeLayout) findViewById(R.id.level1);
        level2 = (RelativeLayout) findViewById(R.id.level2);
        level3 = (RelativeLayout) findViewById(R.id.level3);

        icon_home.setOnClickListener(this);
        icon_menu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.icon_menu:        // 处理 menu 图标点击事件
                // 三级菜单隐藏状态变成显示，显示变隐藏
                if (isLevel3Show) {
                    // 隐藏第三级菜单
                    Animations.startAnimOut(level3, 0);
                } else {
                    Animations.startAnimIn(level3, 0);
                }
                isLevel3Show = !isLevel3Show;
                break;
            case R.id.icon_home:        // 处理 home 图标点击事件
                // 若二级菜单是显示状态，隐藏二三级菜单
                if (isLevel2Show) {
                    Animations.startAnimOut(level2, 200);
                    isLevel2Show = false;
                    if (isLevel3Show) {
                        Animations.startAnimOut(level3, 0);
                        isLevel3Show = false;
                    }
                } else {    // 若二级菜单是隐藏状态，显示二级菜单
                    Animations.startAnimIn(level2, 0);
                    isLevel2Show = true;
                }
                break;
            default:
                break;
        }
    }

    /**
     * 监听按键的动作
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) { // 监听 menu 按键
            changeLevel1State();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 改变第一级菜单的状态
     */
    private void changeLevel1State() {
        // 若第一级菜单是显示状态，隐藏所有菜单的显示状态
        if (isLevel1Show) {
            Animations.startAnimOut(level1, 400);
            isLevel1Show = false;
            if (isLevel2Show) {
                Animations.startAnimOut(level2, 200);
                isLevel2Show = false;
                if (isLevel3Show) {
                    Animations.startAnimOut(level3, 0);
                    isLevel2Show = false;
                }
            }
        } else {    // 若第一级菜单是隐藏状态，显示一、二级菜单
            Animations.startAnimIn(level1, 0);
            isLevel1Show = true;
            Animations.startAnimIn(level2, 200);
            isLevel2Show = true;
        }
    }
}
