package com.example.zhli.lottery.view.manager;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhli.lottery.R;

/**
 * 管理标题容器的工具
 * Created by zhli on 2015/2/10.
 */
public class TitleManager {
    // 单例模式
    private static TitleManager instance = new TitleManager();
    private TitleManager() {}
    public static TitleManager getInstance() {
        if (instance == null) {
            instance = new TitleManager();
        }
        return instance;
    }

    private RelativeLayout commonContainer;
    private RelativeLayout loginContainer;
    private RelativeLayout unLoginContainer;

    private ImageView goback;// 返回
    private ImageView help;// 帮助
    private ImageView login;// 登录

    private TextView titleContent;// 标题内容
    private TextView userInfo;// 用户信息

    public void init(Activity activity) {
        commonContainer = (RelativeLayout) activity.findViewById(R.id.ii_common_container);
        unLoginContainer = (RelativeLayout) activity.findViewById(R.id.ii_unlogin_title);
        loginContainer = (RelativeLayout) activity.findViewById(R.id.ii_login_title);
        goback = (ImageView) activity.findViewById(R.id.ii_title_goback);
        help = (ImageView) activity.findViewById(R.id.ii_title_help);
        login = (ImageView) activity.findViewById(R.id.ii_title_login);
        titleContent = (TextView) activity.findViewById(R.id.ii_title_content);
        userInfo = (TextView) activity.findViewById(R.id.ii_top_user_info);

        setListener();
    }

    private void setListener() {
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("goback");
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("help");
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("login");
            }
        });
    }

    private void initTitle() {
        commonContainer.setVisibility(View.GONE);
        loginContainer.setVisibility(View.GONE);
        unLoginContainer.setVisibility(View.GONE);
    }

    // 显示和隐藏

    /**
     * 显示同用标题
     */
    public void showCommonTitle() {
        initTitle();
        commonContainer.setVisibility(View.VISIBLE);
    }

    /**
     * 显示未登录标题
     */
    public void showUnloginTitle() {
        initTitle();
        unLoginContainer.setVisibility(View.VISIBLE);
    }

    /**
     * 显示已登陆标题
     */
    public void showLoginTitle() {
        initTitle();
        loginContainer.setVisibility(View.VISIBLE);
    }

    /**
     * 根据传入的字符串修改标题内容
     * @param title
     */
    public void changeTitle(String title) {
        titleContent.setText(title);
    }
}
