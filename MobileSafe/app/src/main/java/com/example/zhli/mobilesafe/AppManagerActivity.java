package com.example.zhli.mobilesafe;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhli.mobilesafe.db.dao.AppLockDao;
import com.example.zhli.mobilesafe.domain.AppInfo;
import com.example.zhli.mobilesafe.engine.AppInfoProvider;
import com.example.zhli.mobilesafe.utils.DensityUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AppManagerActivity extends ActionBarActivity implements View.OnClickListener {

    private static final String TAG = "AppManagerActivity";
    private TextView tv_avail_rom;
    private TextView tv_avail_sd;
    private ListView lv_app_manager;
    private LinearLayout ll_loading;
    private List<AppInfo> appInfos;     // 所有应用程序包信息
    private List<AppInfo> userAppInfos; // 用户应用程序包信息
    private List<AppInfo> sysAppInfos; // 系统应用程序包信息
    private TextView tv_stauts;         // 当前程序信息的状态
    private PopupWindow popupWindow;    //弹出的悬浮窗体
    private LinearLayout ll_start;      // 开启
    private LinearLayout ll_uninstall;      // 卸载
    private LinearLayout ll_share;      // 分享
    private AppInfo appInfo;            // 被点击的条目
    private AppManagerAdapter adapter;
    private AppLockDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_manager);

        tv_avail_rom = (TextView) findViewById(R.id.tv_avail_rom);
        tv_avail_sd = (TextView) findViewById(R.id.tv_avail_sd);
        lv_app_manager = (ListView) findViewById(R.id.lv_app_manager);
        ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
        tv_stauts = (TextView) findViewById(R.id.tv_stauts);
        dao = new AppLockDao(this);

        long sdSize = getAvailSpace(Environment.getExternalStorageDirectory().getAbsolutePath());
        long romSize = getAvailSpace(Environment.getDataDirectory().getAbsolutePath());
        tv_avail_sd.setText("SD 卡可用空间：" + Formatter.formatFileSize(this, sdSize));
        tv_avail_rom.setText("内存可用空间：" + Formatter.formatFileSize(this, romSize));

        fillData();

        // 给 listview 注册一个滚动监听器
        lv_app_manager.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }
            // firstVisibleItem第一个可见条目在 listview 集合中的位置
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                dismissPopupWindow();
                // 必须确保子线程执行完了，不然会有空指针异常
                if(userAppInfos != null && sysAppInfos != null) {
                    if(firstVisibleItem > userAppInfos.size()) {
                        tv_stauts.setText("系统应用程序（" + sysAppInfos.size() + "）个");
                    } else {
                        tv_stauts.setText("用户应用程序（" + userAppInfos.size() + "）个");
                    }
                }
            }
        });


        /**
         * 设置 listview 的点击事件(气泡/弹出窗体方式)
         */
        lv_app_manager.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if ((position == 0) || (position == (userAppInfos.size() + 1))) {
                    return;
                } else if (position <= userAppInfos.size()){    // 用户程序
                    int newPosition = position - 1;     // 多了一个 textview 占用了位置
                    appInfo = userAppInfos.get(newPosition);
                } else {
                    int newPosition = position - userAppInfos.size() - 2;
                    appInfo = sysAppInfos.get(newPosition);
                }
                // System.out.println(appInfo.getPackname());
                dismissPopupWindow();

                View contentView = View.inflate(getApplicationContext(), R.layout.popup_app_item, null);
                ll_start = (LinearLayout) contentView.findViewById(R.id.ll_start);
                ll_uninstall = (LinearLayout) contentView.findViewById(R.id.ll_uninstall);
                ll_share = (LinearLayout) contentView.findViewById(R.id.ll_share);

                ll_start.setOnClickListener(AppManagerActivity.this);
                ll_share.setOnClickListener(AppManagerActivity.this);
                ll_uninstall.setOnClickListener(AppManagerActivity.this);

                popupWindow = new PopupWindow(contentView, -2, -2); //-2:wrap_conent，-1：match_parent

                // 动画效果的播放必须要求窗体有背景颜色
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));    // 透明色也是一中颜色

                int[] location = new int[2];
                view.getLocationInWindow(location);
                int dip = 60;
                int px = DensityUtil.dip2px(getApplicationContext(), dip);
                popupWindow.showAtLocation(parent, Gravity.LEFT | Gravity.TOP, px, location[1]);

                // 动画效果
                ScaleAnimation sa = new ScaleAnimation(0.3f, 1.0f, 0.3f, 1.0f, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0.5f);
                sa.setDuration(300);
                AlphaAnimation aa = new AlphaAnimation(0.5f, 1.0f);
                aa.setDuration(300);
                AnimationSet set = new AnimationSet(false); // 独立播放
                set.addAnimation(aa);
                set.addAnimation(sa);
                contentView.startAnimation(set);
            }
        });

        // 程序锁de长点击事件监听器
        lv_app_manager.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if ((position == 0) || (position == (userAppInfos.size() + 1))) {
                    return true;
                } else if (position <= userAppInfos.size()){    // 用户程序
                    int newPosition = position - 1;     // 多了一个 textview 占用了位置
                    appInfo = userAppInfos.get(newPosition);
                } else {
                    int newPosition = position - userAppInfos.size() - 2;
                    appInfo = sysAppInfos.get(newPosition);
                }
                Log.i(TAG, "长点击了" + appInfo.toString());
                ViewHolder holder = (ViewHolder) view.getTag();
                if (dao.query(appInfo.getPackname())) {
                    // 锁定状态，解除锁定，更新界面
                    dao.delete(appInfo.getPackname());
                    holder.iv_status.setImageResource(R.drawable.unlock);
                } else {
                    dao.insert(appInfo.getPackname());
                    holder.iv_status.setImageResource(R.drawable.lock);
                }

                return true;
            }
        });
    }


    @Override
    protected void onDestroy() {
        dismissPopupWindow();
        super.onDestroy();
    }

    /**
     * 布局对应的点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        dismissPopupWindow();
        switch (v.getId()) {
            case R.id.ll_start:
                Log.i(TAG, "start" + appInfo.getName());
                startApplication();
                break;
            case R.id.ll_share:
                Log.i(TAG, "share" + appInfo.getName());
                shareApplication();
                break;
            case R.id.ll_uninstall:
                if (appInfo.isUserApp()) {
                    Log.i(TAG, "uninstall" + appInfo.getName());
                    uninstallApplication();
                } else {
                    Toast.makeText(this, "卸载系统应用需要 root 权限", Toast.LENGTH_SHORT).show();
                    // 代码卸载手机系统软件(要有 root 权限)
//                    try {
//                        Runtime.getRuntime().exec("");
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                }
                break;
        }
    }

    /**
     * 分享一个应用程序
     */
    private void shareApplication() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "推荐使用一款软件，名字叫：xxx");
        startActivity(intent);
    }


    /**
     * 卸载用用程序
     */
    private void uninstallApplication() {
//        <action android:name="android.intent.action.VIEW" />
//        <action android:name="android.intent.action.DELETE" />
//        <category android:name="android.intent.category.DEFAULT" />
//        <data android:scheme="package" />
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setAction("android.intent.action.DELETE");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setData(Uri.parse("package:" + appInfo.getPackname()));
        // 要刷新页面，不能简单用 startActivity(intent);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 刷新页面
        fillData();
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 开启一个应用程序
     */
    private void startApplication() {
        // 查询入口 activity ，启动
        PackageManager pm = getPackageManager();
        /*
        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        // 查询手机上所有具有启动能力的启动的 activity
        List<ResolveInfo> infos = pm.queryIntentActivities(intent, PackageManager.GET_INTENT_FILTERS);
        */
        // 启动某个
        Intent intent = pm.getLaunchIntentForPackage(appInfo.getPackname());
        if(intent != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "不能启动当前应用", Toast.LENGTH_SHORT).show();
        }

    }

    private class AppManagerAdapter extends BaseAdapter {
        @Override
        public int getCount() {
//            return appInfos.size();
            // 两个 1 是显示两个额外的标签
            return 1 + userAppInfos.size() + 1 + sysAppInfos.size();
        }
        @Override
        public Object getItem(int position) {
            return null;
        }
        @Override
        public long getItemId(int position) {
            return 0;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            AppInfo appInfo;

            // 两个标签的特殊位置
            if(position == 0) {
                TextView tv = new TextView(getApplicationContext());
                tv.setTextColor(Color.WHITE);
                tv.setBackgroundColor(Color.GRAY);
                tv.setText("用户应用程序（" + userAppInfos.size() + "）个");
                return tv;
            } else if (position == userAppInfos.size() + 1) {
                TextView tv = new TextView(getApplicationContext());
                tv.setTextColor(Color.WHITE);
                tv.setBackgroundColor(Color.GRAY);
                tv.setText("系统应用程序（" + sysAppInfos.size() + "）个");
                return tv;
            } else if (position <= userAppInfos.size()){
                int newPosition = position - 1;     // 多了一个 textview 占用了位置
                appInfo = userAppInfos.get(newPosition);
            } else {
                int newPosition = position - userAppInfos.size() - 2;
                appInfo = sysAppInfos.get(newPosition);
            }

            View view;
            ViewHolder holder;

//            if (position < userAppInfos.size()) { // 用户程序显示的位置
//                appInfo = userAppInfos.get(position);
//            } else {    // 系统程序
//                int newPosition = position - userAppInfos.size();
//                appInfo = sysAppInfos.get(newPosition);
//            }

            // 不仅需要检查是否为空，还要检查是否是合适的对象
            if(convertView != null && convertView instanceof RelativeLayout) {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            } else {
                view = View.inflate(getApplicationContext(), R.layout.list_item_appinfo, null);
                holder = new ViewHolder();
                holder.iv_icon = (ImageView) view.findViewById(R.id.iv_app_icon);
                holder.tv_location = (TextView) view.findViewById(R.id.tv_app_location);
                holder.tv_name = (TextView) view.findViewById(R.id.tv_app_name);
                holder.iv_status = (ImageView) view.findViewById(R.id.iv_status);
                view.setTag(holder);
            }
//            AppInfo appInfo = appInfos.get(position);
            holder.iv_icon.setImageDrawable(appInfo.getIcon());
            holder.tv_name.setText(appInfo.getName());
            if(appInfo.isInRom()) {
                holder.tv_location.setText("手机内存");
            } else {
                holder.tv_location.setText("外部存储设备");
            }

            // 程序锁状态
            if (dao.query(appInfo.getPackname())) {
                holder.iv_status.setImageResource(R.drawable.lock);
            } else {
                holder.iv_status.setImageResource(R.drawable.unlock);
            }
            return view;
        }
    }

    static class ViewHolder {
        TextView tv_name;
        TextView tv_location;
        ImageView iv_icon;
        ImageView iv_status;
    }

    /**
     * 获取某个目录的可用空间
     * @param path
     * @return
     */
    private long getAvailSpace(String path) {
        StatFs statFs = new StatFs(path);
        long size = statFs.getBlockSize();  // 获取磁盘每个分区的大小
        long count = statFs.getAvailableBlocks();    // 可用区块的个数
        return size * count;
    }

    /**
     * 关闭旧的弹出窗体
     */
    private void dismissPopupWindow() {
        if(popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }

    private void fillData() {
        ll_loading.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                appInfos = AppInfoProvider.getAppInfos(AppManagerActivity.this);
                userAppInfos = new ArrayList<>();
                sysAppInfos = new ArrayList<>();
                for(AppInfo info : appInfos) {
                    if (info.isUserApp()) {
                        userAppInfos.add(info);
                    } else {
                        sysAppInfos.add(info);
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(adapter == null) {
                            adapter = new AppManagerAdapter();
                            lv_app_manager.setAdapter(adapter);
                        } else {
                            adapter.notifyDataSetChanged();
                        }
                        ll_loading.setVisibility(View.INVISIBLE);
                    }
                });
            }
        }).start();
    }

}
