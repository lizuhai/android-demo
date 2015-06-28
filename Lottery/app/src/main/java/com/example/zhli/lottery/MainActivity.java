package com.example.zhli.lottery;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.example.zhli.lottery.utils.FadeUtil;
import com.example.zhli.lottery.view.FirstUI;
import com.example.zhli.lottery.view.manager.BottomManager;
import com.example.zhli.lottery.view.manager.SecondUI;
import com.example.zhli.lottery.view.manager.TitleManager;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    private RelativeLayout middle;      // 中间占位的容器
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            changeUI();
            super.handleMessage(msg);
        }
    };
    private View child1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.il_main);

        init();

      /*
        Message message = new Message();
        CurrentIssueElement element = new CurrentIssueElement();
        element.getLotteryid().setTagValue("118");
        String xml = message.getXml(element);
        Log.i(TAG, xml);
*/
//        NetUtil.checkNet(getApplicationContext());

    /*    UserEngineImpl impl = new UserEngineImpl();
        User user = new User();
        user.setUsername("li");
        user.setPassword("111");
        impl.login(user);*/
e
       /* UserEngine engine = BeanFactory.getImpl(UserEngine.class);
        User user = new User();
        user.setUsername("li");
        user.setPassword("111");
        engine.login(user);*/
    }

    private void init() {
        TitleManager manager = TitleManager.getInstance();
        manager.init(this);
        manager.showUnloginTitle();

        BottomManager.getInstrance().init(this);
        BottomManager.getInstrance().showCommonBottom();

        middle = (RelativeLayout) findViewById(R.id.ii_middle);
        loadFirstView();

        // 当第一个页面加载完 2s 后，加载第二个界面
        handler.sendEmptyMessageDelayed(10, 2000);
  }

    private void loadFirstView() {
        FirstUI firstUI = new FirstUI(this);
        child1 = firstUI.getChild();
        middle.addView(child1);
    }
    private void loadSecondView() {
        SecondUI secondUI = new SecondUI(this);
        View child = secondUI.getChild();
        middle.addView(child);

        // 执行切换动画
//        child.startAnimation(AnimationUtils.loadAnimation(this, R.anim.ia_view_change));
        FadeUtil.fadeIn(child, 2000, 1000);
    }

    /**
     * 切换界面
     */
    protected void changeUI() {
        // 清理上一个显示内容
//        middle.removeAllViews();
        FadeUtil.fadeOut(child1, 2000);
        loadSecondView();
    }
    // 存在的问题
    // 1. 切换界面时清理上一个显示内容
    // 2. 切换动画(简单 -- 复杂（淡入淡出）)
    // 3. 切换界面的通用处理 changeUI()
    // 4. 不使用 handler，任意点击按钮切换
}




//<?xml version='1.0' encoding='UTF-8' ?>
//<message version="1.0">
//<header><agenterid>889931</agenterid>
//<source>ivr</source>
//<compress>DES</compress>
//<messengerid>20150204171338992527</messengerid>
//<timestamp>20150204171338</timestamp>
//<digest>212e35bdb6522d030a416197e7120664</digest>
//<transactiontype></transactiontype>
//<username></username>
//</header>
//<body><elements></elements></body>
//</message>