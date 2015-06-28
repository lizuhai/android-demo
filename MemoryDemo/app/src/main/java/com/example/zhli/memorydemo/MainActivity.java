package com.example.zhli.memorydemo;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.File;
import java.util.Enumeration;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tvMemoryInfo = (TextView) findViewById(R.id.tv_memory_info);

        // 获得 sd 卡的状态
        File sdcardDir = Environment.getExternalStorageDirectory();
        String sdcardMemory = getMemoryInfo(sdcardDir);

        // 获得手机内存的状态
        File data = Environment.getDataDirectory();
        String  dataMemory = getMemoryInfo(data);

        tvMemoryInfo.setText("SD卡：" + sdcardMemory + "\n" + "手机内存：" + dataMemory);
    }

    /**
     * 根据路径获取内存状态
     * @param path
     * @return
     */
    private String getMemoryInfo(File path) {
        // 获得一个磁盘状态对象
        StatFs stat = new StatFs(path.getPath());
        // 获得一个扇区的大小
        long blockSize = stat.getBlockSize();
        // 获得总共有多少个扇区
        long totalBlocks = stat.getBlockCount();
        // 获得可用的扇区数量
        long availableBlocks = stat.getAvailableBlocks();

        String totalMemory = Formatter.formatFileSize(this, totalBlocks * blockSize);
        String availableMemory = Formatter.formatFileSize(this, availableBlocks * blockSize);

        return "总空间: " + totalMemory + "\n" + "\t剩余空间: " + availableMemory;
    }

}
