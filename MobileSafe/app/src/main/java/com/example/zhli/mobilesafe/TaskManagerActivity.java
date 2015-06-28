package com.example.zhli.mobilesafe;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhli.mobilesafe.domain.TaskInfo;
import com.example.zhli.mobilesafe.engine.TaskInfoProvider;
import com.example.zhli.mobilesafe.utils.SystemInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class TaskManagerActivity extends ActionBarActivity {

    private TextView tv_process_cuount;
    private TextView tv_memory_info;
    private LinearLayout ll_loading;
    private ListView lv_task_manager;
    private List<TaskInfo> userTaskInfos;   // 用户进程信息集合
    private List<TaskInfo> sysTaskInfos;   // 系统进程信息集合
    private List<TaskInfo> allTaskInfos;   // 所有进程信息集合
    private TaskManagerAdapter adapter;
    private TextView tv_status;
    private int processCount;   // 进程数量
    private long availMem;      // 可用内存
    private long totalMem;      // 总内存

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_manager);

        tv_process_cuount = (TextView) findViewById(R.id.tv_process_count);
        tv_memory_info = (TextView) findViewById(R.id.tv_memory_info);
        ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
        lv_task_manager = (ListView) findViewById(R.id.lv_task_manager);
        tv_status = (TextView) findViewById(R.id.tv_stauts);

        // 填充数据
        fillData();

        lv_task_manager.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(userTaskInfos != null && sysTaskInfos != null) { // 准备好了数据
                    if(firstVisibleItem > userTaskInfos.size()) {
                        tv_status.setText("系统进程:" + sysTaskInfos.size() + "个");
                    } else {
                        tv_status.setText("用户进程:" + userTaskInfos.size() + "个");
                    }
                }
            }
        });

        lv_task_manager.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TaskInfo taskInfo;
                if (position == 0 || position == (userTaskInfos.size() + 1)) { // 进程标签
                    return;
                } else if (position <= userTaskInfos.size()) {
                    taskInfo = userTaskInfos.get(position - 1);
                } else {
                    taskInfo = sysTaskInfos.get(position - 2 - userTaskInfos.size());
                }
                // 不让选自己
                if (getPackageName().equals(taskInfo.getPackname()))
                    return;

                ViewHolder holder = (ViewHolder) view.getTag();
                if (taskInfo.isChecked()) {
                    taskInfo.setChecked(false);
                    holder.cb_status.setChecked(false);
                } else {
                    taskInfo.setChecked(true);
                    holder.cb_status.setChecked(true);
                }
            }
        });
    }

    private void setTitle() {
        processCount = SystemInfo.getRunningProcessCount(this);
        tv_process_cuount.setText("运行中的进程:" + processCount + "个");
        availMem = SystemInfo.getAvailMem(this);
        totalMem = SystemInfo.getTotalMem(this);
        tv_memory_info.setText("剩余/总内存：" + Formatter.formatFileSize(this, availMem) + "/" + Formatter.formatFileSize(this, totalMem));
    }

    private void fillData() {
        ll_loading.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                allTaskInfos = TaskInfoProvider.getTaskInfos(getApplicationContext());
                userTaskInfos = new ArrayList<>();
                sysTaskInfos = new ArrayList<>();
                for (TaskInfo info : allTaskInfos) {
                    if (info.isUserTask()) {
                        userTaskInfos.add(info);
                    } else {
                        sysTaskInfos.add(info);
                    }
                }
                // 跟新页面
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ll_loading.setVisibility(View.INVISIBLE);
                        if (adapter == null) {
                            adapter = new TaskManagerAdapter();
                            lv_task_manager.setAdapter(adapter);
                        } else {
                            adapter.notifyDataSetChanged();
                        }
                        setTitle();
                    }
                });
            }
        }).start();
    }

    private class TaskManagerAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
            if (sp.getBoolean("showsystem", false)) {
                return allTaskInfos.size() + 2;
            } else {
                return userTaskInfos.size() + 1;
            }
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
            TaskInfo taskInfo;
            if (position == 0) { // 用户进程标签
                TextView tv = new TextView(getApplicationContext());
                tv.setBackgroundColor(Color.GRAY);
                tv.setText("用户进程:" + userTaskInfos.size() + "个");
                tv.setTextColor(Color.WHITE);
                return tv;
            } else if (position == (userTaskInfos.size() + 1)) { // 用户进程标签
                TextView tv = new TextView(getApplicationContext());
                tv.setBackgroundColor(Color.GRAY);
                tv.setText("系统进程:" + sysTaskInfos.size() + "个");
                tv.setTextColor(Color.WHITE);
                return tv;
            } else if (position <= userTaskInfos.size()) {
                taskInfo = userTaskInfos.get(position - 1);
            } else {
                taskInfo = sysTaskInfos.get(position - 2 - userTaskInfos.size());
            }
            View view;
            ViewHolder holder;
            if(convertView != null && convertView instanceof RelativeLayout) {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            } else {
                view = View.inflate(getApplicationContext(), R.layout.list_item_taskinfo, null);
                holder = new ViewHolder();
                holder.iv_icon = (ImageView) view.findViewById(R.id.iv_task_icon);
                holder.tv_name = (TextView) view.findViewById(R.id.tv_task_name);
                holder.tv_memsize = (TextView) view.findViewById(R.id.tv_task_memsize);
                holder.cb_status = (CheckBox) view.findViewById(R.id.cb_status);
                view.setTag(holder);
            }
            holder.iv_icon.setImageDrawable(taskInfo.getIcon());
            holder.tv_name.setText(taskInfo.getName());
            holder.tv_memsize.setText("内存占用:" + Formatter.formatFileSize(getApplicationContext(), taskInfo.getMemsize()));
            holder.cb_status.setChecked(taskInfo.isChecked());

            // 不让选自己
            if (getPackageName().equals(taskInfo.getPackname())) {
                holder.cb_status.setVisibility(View.INVISIBLE);
            } else {
                holder.cb_status.setVisibility(View.VISIBLE);
            }
            return view;
        }
    }

    static class ViewHolder {
        ImageView iv_icon;
        TextView tv_name;
        TextView tv_memsize;
        CheckBox cb_status;
    }

    /**
     * 全选
     */
    public void selectAll(View v) {
        for (TaskInfo info : allTaskInfos) {
            // 不让选自己
            if (getPackageName().equals(info.getPackname()))
                continue;
            info.setChecked(true);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 反选
     */
    public void selectOppo(View v) {
        for (TaskInfo info : allTaskInfos) {
            info.setChecked(!info.isChecked());
            // 不让选自己
            if (getPackageName().equals(info.getPackname()))
                continue;
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 清理
     */
    public void killAll(View v) {
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        int count = 0;
        long saveMem = 0;
        Iterator iterator = allTaskInfos.iterator();
        while (iterator.hasNext()) {
            TaskInfo info = (TaskInfo) iterator.next();
            if (info.isChecked()) { // 杀死进程
                am.killBackgroundProcesses(info.getPackname());
                if (info.isUserTask()) {
                    userTaskInfos.remove(info);
                } else {
                    sysTaskInfos.remove(info);
                }
                iterator.remove();
                count ++;
                saveMem += info.getMemsize();
            }
        }
        // 欺骗用户
        adapter.notifyDataSetChanged();
        Toast.makeText(
                this,
                "杀死了" + count +"进程，释放了" + Formatter.formatFileSize(this, saveMem) + "内存",
                Toast.LENGTH_SHORT).show();
        processCount -= count;
        availMem += saveMem;

        tv_process_cuount.setText("运行中的进程:" + processCount + "个");
        tv_memory_info.setText("剩余/总内存：" + Formatter.formatFileSize(this, availMem) + "/" + Formatter.formatFileSize(this, totalMem));

    }

    /**
     * 设置
     */
    public void enterSetting(View v) {
        Intent intent = new Intent(this, TaskSettingActivity.class);
        startActivityForResult(intent, 0);
    }

    // 设置返回刷新页面
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        adapter.notifyDataSetChanged();
        super.onActivityResult(requestCode, resultCode, data);
    }
}
