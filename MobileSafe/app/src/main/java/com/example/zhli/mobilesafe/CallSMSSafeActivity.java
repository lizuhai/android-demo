package com.example.zhli.mobilesafe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhli.mobilesafe.db.dao.BlackNumberDao;
import com.example.zhli.mobilesafe.domain.BlackNumberInfo;

import java.util.List;


public class CallSMSSafeActivity extends ActionBarActivity {

    private static final String TAG = "CallSMSSafeActivity";
    private ListView lv_callsms_safe;
    private List<BlackNumberInfo> infos;
    private BlackNumberDao dao;
    private CallSmsSafeAdapter adapter;
    private LinearLayout ll_loading;        // 加载

    private int offset = 0;
    private int maxnumber = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_smssafe);

        lv_callsms_safe = (ListView) findViewById(R.id.lv_callsms_safe);
        ll_loading = (LinearLayout) findViewById(R.id.ll_loading);

        dao = new BlackNumberDao(this);

        // 实验数据
//        for(int i = 0; i < 100; i++) {
//                dao.insert("156918657" + i, String.valueOf(new Random().nextInt(3) + 1));
//        }
        fillData();


//        Log.i("CallSMSSafeActivity", String.valueOf(infos.size()));
//        adapter = new CallSmsSafeAdapter();
//        lv_callsms_safe.setAdapter(adapter);

        // 滚动到页面底端时候再加载数据
        // listView 注册一个滚动事件的监听器
        lv_callsms_safe.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:    // 空闲
                        // 判断当前 listview 的滚动位置
                        int lastPosition = lv_callsms_safe.getLastVisiblePosition();       // 获取最后一个可见条目在集合里面的位置
                        if(lastPosition == infos.size() - 1) {  // 列表到达最后一个位置
                            // 加载更多数据
                            offset += maxnumber;
                            fillData();
                        }
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:    // 手指触摸滚动
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:   // 惯性滑行状态
                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private void fillData() {
        ll_loading.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
//                infos = dao.queryAll();
                if(infos == null) {
                    infos = dao.queryPart(offset, maxnumber);
                } else { // 原来已经有数据了
                    infos.addAll(dao.queryPart(offset, maxnumber));
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ll_loading.setVisibility(View.INVISIBLE);
                        if(adapter == null) {
                            adapter = new CallSmsSafeAdapter();
                            lv_callsms_safe.setAdapter(adapter);
                        } else {
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        }).start();
    }

    private class CallSmsSafeAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return infos.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        /**
         * 有多少的条目，就会调用这个方法多少次
         */
        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) { // w参数的作用，
            View view;
            ViewHolder holder;
            // 1. 减少内存中 view 对象创建的个数
            if(convertView == null) {
                Log.i(TAG, "创建新的 view 对象" + position);
                    // 将一个布局文件转换成 View 对象（十分消耗资源）
                view = View.inflate(getApplicationContext(), R.layout.list_callsms_item, null);

                // 2. 减少子孩子的查询次数
                holder = new ViewHolder();
                holder.tv_number = (TextView) view.findViewById(R.id.tv_black_number);
                holder.tv_mode = (TextView) view.findViewById(R.id.tv_block_mode);
                holder.iv_delte = (ImageView) view.findViewById(R.id.iv_delte);

                // 当对象被创建的时候，存在记事本里面
                view.setTag(holder);
            } else {
                Log.i(TAG, "复用 view 对象" + position);
                view = convertView;
                holder = (ViewHolder) view.getTag();
            }

            holder.tv_number.setText(infos.get(position).getNumber());
            String mode = infos.get(position).getMode();

            if ("1".equals(mode)) {
                holder.tv_mode.setText("电话拦截");
            } else if ("2".equals(mode)) {
                holder.tv_mode.setText("短信拦截");
            } else if("3".equals(mode)) {
                holder.tv_mode.setText("全部拦截");
            }

            holder.iv_delte.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CallSMSSafeActivity.this);
                    builder.setTitle("警告！");
                    builder.setMessage("你确定要这条删除吗？");
                    builder.setNegativeButton("取消", null);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dao.delete(infos.get(position).getNumber());
                            infos.remove(position);
                            adapter.notifyDataSetChanged();
                        }
                    });
                    builder.show();
                }
            });

            return view;
        }
        /**
         * view 对象的容器
         * 记录view 对象的地址（相当于一个记事本）
         */
    }
    static class ViewHolder {
        TextView tv_number;
        TextView tv_mode;
        ImageView iv_delte;
    }

    /**
     * 添加黑名单
     */
    public void addBlackNumber(View v) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        AlertDialog dialog = builder.create();
//        View view = View.inflate(this, R.layout.dialog_add_blacknumber, null);
//        dialog.setView(view, 0, 0, 0, 0);
//        dialog.show();
        showAddBlackNumberDialog();
    }

    private AlertDialog dialog;
    private CheckBox cb_phone;
    private CheckBox cb_sms;
    private Button ok;
    private Button cancel;
    private EditText et_blacknumber;

    /**
     * 设置添加黑名单对话框
     */
    private void showAddBlackNumberDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CallSMSSafeActivity.this);
        // 自定义一个布局文件
        View view = View.inflate(CallSMSSafeActivity.this, R.layout.dialog_add_blacknumber, null);

        cb_phone = (CheckBox) view.findViewById(R.id.cb_phone);
        cb_sms = (CheckBox) view.findViewById(R.id.cb_sms);
        ok = (Button) view.findViewById(R.id.ok);
        cancel = (Button) view.findViewById(R.id.cancel);
        et_blacknumber = (EditText) view.findViewById(R.id.et_blacknumber);

        dialog = builder.create();
        dialog.setView(view, 0, 0, 0, 0);   // 设置对话框四周都填满

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String blackNumber = et_blacknumber.getText().toString().trim();
                if (TextUtils.isEmpty(blackNumber)) {
                    Toast.makeText(getApplicationContext(), "号码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                String mode = "3";
                if (cb_sms.isChecked() && cb_phone.isChecked()) {     // 全部拦截
                    mode = "3";
                } else if (cb_phone.isChecked()) {   // 电话拦截
                    mode = "1";
                } else if (cb_sms.isChecked()) {    // 短信拦截
                    mode = "2";
                } else {
                    Toast.makeText(getApplicationContext(), "请选择拦截模式", Toast.LENGTH_SHORT).show();
                }
                // 加入数据库
                dao.insert(blackNumber, mode);
                // 显示数据
                BlackNumberInfo info = new BlackNumberInfo();
                info.setMode(mode);
                info.setNumber(blackNumber);
                infos.add(0, info);
                adapter.notifyDataSetChanged();

                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
