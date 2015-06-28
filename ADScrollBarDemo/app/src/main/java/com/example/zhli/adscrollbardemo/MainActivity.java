package com.example.zhli.adscrollbardemo;

import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private ViewPager viewpager;
    private LinearLayout point_group;
    private TextView image_desc;
    private MyPagerAdapter adapter;
    // 图片资源ID
    private final int[] imageIds = { R.drawable.a, R.drawable.b, R.drawable.c,
            R.drawable.d, R.drawable.e };

    //图片标题集合
    private final String[] imageDescriptions = {
            "巩俐不低俗，我就不能低俗",
            "扑树又回来啦！再唱经典老歌引万人大合唱",
            "揭秘北京电影如何升级",
            "乐视网TV版大派送",
            "热血屌丝的反杀"
    };
    private ArrayList<ImageView> imageList;
    private int lastPosition;               // 上一个指示点(页面)的位置
    private boolean isRunning = false;      // 判断是否自动滚动图片

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // 滚动到下一页
            viewpager.setCurrentItem(viewpager.getCurrentItem() + 1);
            // 再次发送消息
            if (isRunning)
                handler.sendEmptyMessageDelayed(0, 3000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewpager = (ViewPager) findViewById(R.id.viewpager);
        image_desc = (TextView) findViewById(R.id.image_desc);
        point_group = (LinearLayout) findViewById(R.id.point_group);

        image_desc.setText(imageDescriptions[0]);
        imageList = new ArrayList<>();
        for (int i = 0; i < imageIds.length; i++) {
            // 初始化图片资源
            ImageView image = new ImageView(this);
            image.setBackgroundResource(imageIds[i]);
            imageList.add(image);

            // 添加指示点
            ImageView point = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.rightMargin = 10;
            point.setLayoutParams(params);
            point.setBackgroundResource(R.drawable.point_bg);
            if (i == 0) {
                point.setEnabled(true);                // 初始时第一个点设置为focused状态
            } else {
                point.setEnabled(false);
            }
            point_group.addView(point);
        }

        adapter = new MyPagerAdapter();
        viewpager.setAdapter(adapter);
        // 设置当前位置为中间的一个数，可以接近无限向左滑动
        viewpager.setCurrentItem(Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2) % imageList.size());

        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            /**
             * 页面正在滑动时候调用
             */
            @Override
            public void onPageScrolled(int i, float v, int i2) {}
            /**
             * 页面切换后调用
             */
            @Override
            public void onPageSelected(int i) {
                i = i % imageList.size();       // 循环播放

                // 设置文本信息
                image_desc.setText(imageDescriptions[i]);
                // 设置指示点的状态
                // 将当前点设为true
                point_group.getChildAt(i).setEnabled(true);
                    // 上一个点设置为false
                point_group.getChildAt(lastPosition).setEnabled(false);
                lastPosition = i;
            }
            /**
             * 页面状态发生变化时候调用
             */
            @Override
            public void onPageScrollStateChanged(int i) {}
        });

        /**
         * 自动循环播放广告/新闻
         * 1. 定时器
         * 2. 开子线程，while true 循环
         * 3. ClockManager
         * 4. 用 handler 发送消息，实现循环（推荐使用，效率很高）
         */
        isRunning = true;
        handler.sendEmptyMessageDelayed(0, 3000); // 2 s 后执行

    }

    @Override
    protected void onDestroy() {
        isRunning = false;
        super.onDestroy();
    }

    private class MyPagerAdapter extends PagerAdapter {
        /**
         * 获得页面的总数
         */
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }
        /**
         * 判断 view 和 object 对应的关系
         */
        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }
        /**
         * 获得相应位置上的 view
         * @param container view 的容器，viewPager 自身
         * @param position 相应的位置
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // 循环播放
            position = position % imageList.size();

            container.addView(imageList.get(position));
            return imageList.get(position);
        }
        /**
         * 销毁对应位置上的 object
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            object = null;
        }
    }

}
