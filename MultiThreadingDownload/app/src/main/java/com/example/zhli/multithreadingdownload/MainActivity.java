package com.example.zhli.multithreadingdownload;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private static final int DOWNLOAD_ERROR = 1;
    private static final int THREAD_ERROR = 2;
    private static final int DOWNLOAD_FINISH = 0;

    private EditText etPath;
    private EditText etCount;
    private LinearLayout llContainer;   // 存放进度条布局

    private List<ProgressBar> pbs;      // 进度条的集合

    private int threadCount = 3;    	// 线程数量
    private long blockSize;			// 每个下载区块的大小

    // 正在运行的线程数量
    private static int runningThreadCount;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWNLOAD_ERROR:
                    Toast.makeText(getApplicationContext(), "下载失败", Toast.LENGTH_SHORT).show();
                    break;
                case THREAD_ERROR:
                    Toast.makeText(getApplicationContext(), "下载失败", Toast.LENGTH_SHORT).show();
                    break;
                case DOWNLOAD_FINISH:
                    Toast.makeText(getApplicationContext(), "下载完成", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etPath = (EditText) findViewById(R.id.et_path);
        etCount = (EditText) findViewById(R.id.et_count);
        llContainer = (LinearLayout) findViewById(R.id.ll_container);
    }


    /**
     * 下载按钮的点击事件
     * @param v
     */
    public void download(View v) {
        // 下载文件的路径
        final String path = etPath.getText().toString().trim();
        final String count = etCount.getText().toString().trim();
        if(TextUtils.isEmpty(path)) {
            Toast.makeText(this, "下载路径不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(count)) {
            Toast.makeText(this, "下载线程数不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        threadCount = Integer.parseInt(count);

        // 清空旧的进度条
        llContainer.removeAllViews();
        // 添加进度条
        pbs = new ArrayList<>();
        for(int j = 0; j < threadCount; j++) {
            ProgressBar pb = (ProgressBar) View.inflate(this, R.layout.pb, null);
            llContainer.addView(pb);
            pbs.add(pb);
        }

        Toast.makeText(this, "开始下载...", Toast.LENGTH_SHORT).show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setReadTimeout(5000);
                    conn.setConnectTimeout(10000);
                    int code = conn.getResponseCode();
                    if(code == 200) {
                        long size = conn.getContentLength();	// 得到服务器返回的文件大小
                        System.out.println("File size: " + size);

                        blockSize = size / threadCount;

                        // 1. 本地创建一个和服务器文件一样大小的文件
                        File file = new File(Environment.getExternalStorageDirectory(), getFileName(path));
                        RandomAccessFile raf = new RandomAccessFile(file, "rw");
                        raf.setLength(size);

                        runningThreadCount = threadCount;

                        // 2. 开启若干子线程分别下载资源
                        for (int i = 0; i < threadCount; i++) {
                            System.out.println("线程 " + i);
                            long startIndex = i * blockSize;
                            long endIndex = (i + 1) * blockSize - 1;
                            if(i == threadCount - 1)
                                endIndex = size - 1;	// 最后一个线程的结束点

                            System.out.println("Start:" + startIndex + "; end:" + endIndex);

                            // 计算每个线程下载的长度
                            int threadSize = (int) (endIndex - startIndex);
                            pbs.get(i).setMax(threadSize);

                            new DownloadThread(i, startIndex, endIndex, path).start();
                        }
                        raf.close();
                    }
                    conn.disconnect();
                } catch (Exception e){
                    e.printStackTrace();
                    Message msg = Message.obtain();
                    msg.what = DOWNLOAD_ERROR;
                    handler.sendMessage(msg);
                }
            }
        }).start();
    }

    private class DownloadThread extends Thread {
        private int threadId;
        private long startIndex;
        private long endIndex;
        private String path;
        public DownloadThread(int threadId, long startIndex, long endIndex, String path) {
            super();
            this.threadId = threadId;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.path = path;
        }
        @Override
        public void run() {
            try {
                // 当前线程下载的总大小
                int total = 0;
                // 进度保存的文件
                File positionFile = new File(Environment.getExternalStorageDirectory(), getFileName(path) + threadId + ".txt");

                URL url = new URL(path);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                // 断点下载
                if(positionFile.exists() && positionFile.length() > 0) {	// 判断是否有记录
                    FileInputStream fis = new FileInputStream(positionFile);
                    BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                    // 获得当前线程上次下载的大小
                    String lastTotalStr = br.readLine();
                    int lastTotal = Integer.valueOf(lastTotalStr);
                    System.out.println("上次线程 " + threadId + " 下载的大小：" + lastTotal);
                    startIndex += lastTotal;
                    total += lastTotal;			// 加上上次的总大小
                    br.close();
                    fis.close();
                }

                // 指明http协议头，下载哪一段		conn.setRequestProperty("Range", "bytes=1-10");
                conn.setRequestProperty("Range", "bytes=" + startIndex + "-" + endIndex);

                conn.setReadTimeout(5000);
                conn.setConnectTimeout(10000);
                int code = conn.getResponseCode();

                System.out.println("响应码: " + code);

                InputStream is = conn.getInputStream();
                File file = new File(Environment.getExternalStorageDirectory(), getFileName(path));
                RandomAccessFile raf = new RandomAccessFile(file, "rw");
                // 指定文件开始写的位置
                raf.seek(startIndex);
                System.out.println("第 " + threadId + " 个线程，写文件的开始位置：" + String.valueOf(startIndex));
                int len = -1;
                byte[] buffer = new byte[1024 * 1024 * 5];	// 5Mb 写一次数据

                while((len = is.read(buffer)) != -1) {

//					FileOutputStream fos = new FileOutputStream(positionFile);
                    RandomAccessFile rf = new RandomAccessFile(positionFile, "rwd");

                    raf.write(buffer, 0, len);

                    total += len;
                    rf.write(String.valueOf(total).getBytes());
                    rf.close();

                    pbs.get(threadId).setProgress(total);       // 可以直接在子线程中写，不必在主线程中写
                }
                raf.close();
                is.close();
                System.out.println("线程 " + threadId + " 下载完毕");
            } catch (Exception e) {
                e.printStackTrace();
                Message msg = Message.obtain();
                msg.what = THREAD_ERROR;
                handler.sendMessage(msg);
            } finally {
                synchronized (MainActivity.class) {
                    // 只有所有的线程都下载完了，才能删除

                    runningThreadCount--;
                    // 下载完成
                    if(runningThreadCount < 1) {
                        // 删除临时记录的文件
                        System.out.println("删除临时文件");
                        for (int j = 0; j < threadCount; j++) {
                            File f = new File(Environment.getExternalStorageDirectory(), getFileName(path) + j + ".txt");
                            System.out.println(f.delete());
                        }

                        Message msg = Message.obtain();
                        msg.what = DOWNLOAD_FINISH;
                        handler.sendMessage(msg);
                    }
                }
            }
        }
    }

    /**
     * http://192.168.72.1/2.exe --> 2.exe
     * 通过 Url 下载地址获取文件名
     * @param path 下载地址
     * @return 文件名
     */
    private String getFileName(String path) {
        int start = path.lastIndexOf("/") + 1;
        return path.substring(start);
    }
}
