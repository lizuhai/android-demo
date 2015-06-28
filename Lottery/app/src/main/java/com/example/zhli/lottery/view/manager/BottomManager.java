package com.example.zhli.lottery.view.manager;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhli.lottery.R;

/**
 * 控制底部导航容器
 */
public class BottomManager {
    protected static final String TAG = "BottomManager";

    private static BottomManager instrance;
    private BottomManager() {
    }
    public static BottomManager getInstrance() {
        if (instrance == null) {
            instrance = new BottomManager();
        }
        return instrance;
    }

    /********** 底部菜单容器 **********/
    private RelativeLayout bottomMenuContainer;
    /************ 底部导航 ************/
    private LinearLayout commonBottom;// 购彩通用导航
    private LinearLayout playBottom;// 购彩

    /***************** 导航按钮 ******************/

    /************ 购彩导航底部按钮及提示信息 ************/
    private ImageButton cleanButton;
    private ImageButton addButton;

    private TextView playBottomNotice;

    /************ 通用导航底部按钮 ************/
    private ImageButton homeButton;
    private ImageButton hallButton;
    private ImageButton rechargeButton;
    private ImageButton myselfButton;

    public void init(Activity activity) {
        bottomMenuContainer = (RelativeLayout) activity.findViewById(R.id.ii_bottom);
        commonBottom = (LinearLayout) activity.findViewById(R.id.ii_bottom_common);
        playBottom = (LinearLayout) activity.findViewById(R.id.ii_bottom_game);

        playBottomNotice = (TextView) activity.findViewById(R.id.ii_bottom_game_choose_notice);
        cleanButton = (ImageButton) activity.findViewById(R.id.ii_bottom_game_choose_clean);
        addButton = (ImageButton) activity.findViewById(R.id.ii_bottom_game_choose_ok);

        // 设置监听
        setListener();
    }

    /**
     * 设置监听
     */
    private void setListener() {
        // 清空按钮
        cleanButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Log.i(TAG, "点击清空按钮");

            }
        });
        // 选好按钮
        addButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Log.i(TAG, "点击选好按钮");

            }
        });
    }

    /**
     * 转换到通用导航
     */
    public void showCommonBottom() {
        if (bottomMenuContainer.getVisibility() == View.GONE || bottomMenuContainer.getVisibility() == View.INVISIBLE) {
            bottomMenuContainer.setVisibility(View.VISIBLE);
        }
        commonBottom.setVisibility(View.VISIBLE);
        playBottom.setVisibility(View.INVISIBLE);
    }

    /**
     * 转换到购彩
     */
    public void showGameBottom() {
        if (bottomMenuContainer.getVisibility() == View.GONE || bottomMenuContainer.getVisibility() == View.INVISIBLE) {
            bottomMenuContainer.setVisibility(View.VISIBLE);
        }
        commonBottom.setVisibility(View.INVISIBLE);
        playBottom.setVisibility(View.VISIBLE);
    }

    /**
     * 改变底部导航容器显示情况
     */
    public void changeBottomVisiblity(int type) {
        if (bottomMenuContainer.getVisibility() != type)
            bottomMenuContainer.setVisibility(type);
    }

    /**
     * 设置玩法底部提示信息
     *
     * @param notice
     */
    public void changeGameBottomNotice(String notice) {
        playBottomNotice.setText(notice);
    }
    /*********************************************************************************************/

}
