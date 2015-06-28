package com.example.zhli.mobilesafe;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhli.mobilesafe.utils.MD5Utils;


public class HomeActivity extends ActionBarActivity {

    private static final String TAG = "HomeActivity";
    private GridView list_home;
    private MyAdapter adapter;
    // 功能名称列表
    private static String[] names = {
            "手机防盗", "通讯卫士", "软件管理",
            "进程管理", "流量统计", "手机杀毒",
            "缓存清理", "高级工具", "设置中心"
    };
    // 功能图片列表
    private static int[] ids = {
            R.drawable.safe, R.drawable.callmsgsafe, R.drawable.app,
            R.drawable.taskmanager, R.drawable.netmanager, R.drawable.trojan,
            R.drawable.sysoptimize, R.drawable.atools, R.drawable.settings
    };

    private SharedPreferences sp;   // 存储密码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sp = getSharedPreferences("config", MODE_PRIVATE);
        list_home = (GridView) findViewById(R.id.list_home);
        adapter = new MyAdapter();
        list_home.setAdapter(adapter);

        // 设置每个的点击事件
        list_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                switch (position) {
                   // "手机防盗", "通讯卫士", "软件管理",
                   // "进程管理", "流量统计", "手机杀毒",
                   // "缓存清理", "高级工具", "设置中心"
                    case 0:
                        showLostDialog();
                        break;
                    case 1:
                        intent = new Intent(HomeActivity.this, CallSMSSafeActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(HomeActivity.this, AppManagerActivity.class);
                        startActivity(intent);
                        break;
                    case 3: //流量统计
                        intent = new Intent(HomeActivity.this, TaskManagerActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent(HomeActivity.this, TrafficManagerActivity.class);
                        startActivity(intent);
                        break;
                    case 5:
                        intent = new Intent(HomeActivity.this, AntiVirusActivity.class);
                        startActivity(intent);
                        break;
                    case 6:
                        intent = new Intent(HomeActivity.this, CleanCacheActivity.class);
                        startActivity(intent);
                        break;
                    case 7:     // 进入高级工具
                        intent = new Intent(HomeActivity.this, AtoolsActivity.class);
                        startActivity(intent);
                        break;
                    case 8:         // 设置中心
                        intent = new Intent(HomeActivity.this, SettingActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        break;

                }
            }
        });
    }

    private void showLostDialog() {
        // 判断设置过了密码没有
        if(isSetupPwd()) {  // 设置过了密码
            showEnterDialog();
        } else {
            showSetupPwdDialog();
        }
    }

    /**
     * 输入密码对话框
     */
    private void showEnterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        // 自定义一个布局文件
        View view = View.inflate(HomeActivity.this, R.layout.dialog_enter_password, null);

        et_setup_pwd = (EditText) view.findViewById(R.id.et_setup_pwd);
        ok = (Button) view.findViewById(R.id.ok);
        cancel = (Button) view.findViewById(R.id.cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = et_setup_pwd.getText().toString().trim();
                if(TextUtils.isEmpty(password)) {
                    Toast.makeText(HomeActivity.this, "密码不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                String pwd = sp.getString("password", null);
                if(MD5Utils.md5(password).equals(pwd)) {
                    // 关闭对话框，enter
                    dialog.dismiss();
                    Log.i(TAG, "关闭对话框，enter");
                    Intent intent = new Intent(HomeActivity.this, LostFindActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(HomeActivity.this, "密码错误", Toast.LENGTH_LONG).show();
                    // 密码框置空
                    et_setup_pwd.setText("");
                    return;
                }
            }
        });

//        builder.setView(view);
//        dialog = builder.show();
        // 下面两行替换上面三行
        dialog = builder.create();
        dialog.setView(view, 0, 0, 0, 0);   // 设置对话框四周都填满
        dialog.show();
    }

    private EditText et_setup_pwd;
    private EditText et_setup_confirm;
    private Button ok;
    private Button cancel;

    private AlertDialog dialog;

    /**
     * 设置密码对话框
     */
    private void showSetupPwdDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        // 自定义一个布局文件
        View view = View.inflate(HomeActivity.this, R.layout.dialog_setup_password, null);

        et_setup_pwd = (EditText) view.findViewById(R.id.et_setup_pwd);
        et_setup_confirm = (EditText) view.findViewById(R.id.et_setup_confirm);
        ok = (Button) view.findViewById(R.id.ok);
        cancel = (Button) view.findViewById(R.id.cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = et_setup_pwd.getText().toString().trim();
                String confirm = et_setup_confirm.getText().toString().trim();
                if(!TextUtils.isEmpty(password) && password.equals(confirm)) {
                    // 保存密码，关闭对话框，进入手机防盗页面
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("password", MD5Utils.md5(password));   // 加密保存
                    editor.commit();

                    dialog.dismiss();

                    Log.i(TAG, "保存密码，关闭对话框，进入手机防盗页面");
                    Intent intent = new Intent(HomeActivity.this, LostFindActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(HomeActivity.this, "亲，密码不一致哦", Toast.LENGTH_SHORT).show();
                    // 确认密码框置空
                    et_setup_confirm.setText("");
                    return;
                }
            }
        });

//        builder.setView(view);
//        dialog = builder.show();
        // 下面两行替换上面三行
        dialog = builder.create();
        dialog.setView(view, 0, 0, 0, 0);   // 设置对话框四周都填满
        dialog.show();
    }

    /**
     * 判断是否设置过密码
     * @return
     */
    private boolean isSetupPwd() {
        String password = sp.getString("password", null);
        return !TextUtils.isEmpty(password);
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return names.length;
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
            View view = View.inflate(HomeActivity.this, R.layout.list_home_item, null);
            ImageView iv_item = (ImageView) view.findViewById(R.id.iv_item);
            TextView tv_item = (TextView) view.findViewById(R.id.tv_item);

            tv_item.setText(names[position]);
            iv_item.setImageResource(ids[position]);
            return view;
        }
    }
}
