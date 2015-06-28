package com.example.zhli.dialog;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 确定/取消 对话框
     * @param v
     */
    public void click1(View v) {
        // 对话框创建器
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Hello");
        builder.setMessage("World");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 什么都不写，就是关闭对话框
            }
        });
//        builder.create().show();
        builder.show();             // 和上面的一样，查看源码就可以知道
        builder.setCancelable(false);       // 对话框不可以被取消掉
    }

    /**
     * 单选对话框
     * @param v
     */
    public void click2(View v) {
        // 对话框创建器
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("choose gender");
        final String[] items = {"man", "woman", "twoSex"};
        builder.setSingleChoiceItems(items, 2, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toas
                t.makeText(getApplicationContext(), items[which], Toast.LENGTH_SHORT).show();
                dialog.dismiss();       // 选择完之后关闭对话框
            }
        });
        builder.create().show();
    }

    /**
     * 多选对话框
     * @param v
     */
    public void click3(View v) {
        // 对话框创建器
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("choose interests");
        final String[] items = {"flowers", "basketball", "football"};
        final boolean[] result = new boolean[]{true, false, false};
        builder.setMultiChoiceItems(items, result, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                result[which] = isChecked;
                Toast.makeText(getApplicationContext(), items[which] + isChecked, Toast.LENGTH_SHORT).show();
            }
        });
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < result.length; i++) {
                    if(result[i]) {
                        sb.append(items[i]).append(",");
                    }
                }
                Toast.makeText(getApplicationContext(), sb, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        builder.create().show();

    }

    public void click4(View v) {
        ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("提取");
        pd.setMessage("正在加载数据，稍等");
        pd.show();
    }

    public void click5(View v) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("提取");
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMax(100);

        pd.setMessage("正在加载数据，稍等");
        pd.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    try {
                        Thread.sleep(40);
                        pd.setProgress(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                pd.dismiss();
            }
        }).start();
    }
}
