package com.example.zhli.listensystemmessage;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 监听系统短信
        ContentResolver resolver = getContentResolver();

        // 注册一个内容观察者观察短信数据库
        resolver.registerContentObserver(Uri.parse("content://sms/"), true, new MyContentObserver(new Handler()));
    }

    /**
     * 内容观察者
     */
    class MyContentObserver extends ContentObserver {

        private static final String TAG = "MyContentOberver";

        public MyContentObserver(Handler handler) {
            super(handler);
        }
        /**
         * 当被监听的内容发生改变，发生回调
         * @param selfChange
         */
        @Override
        public void onChange(boolean selfChange) {
            Log.i(TAG, "短信内容改变了");
            // 查询发件箱的内容

            // Uri: 会收到三个一样的短信内容改变的信息，最好把 Uri 设置成下面的
            Cursor cursor = getContentResolver().query(Uri.parse("content://sms/outbox"),
                    new String[]{"address", "date", "body"},
                    null, null, null);
            if(cursor != null && cursor.getCount() > 0) {
                String address;
                long date;
                String body;
                while (cursor.moveToNext()) {
                    address = cursor.getString(0);
                    date = cursor.getLong(1);
                    body = cursor.getString(2);
                    Log.i(TAG, "号码：" + address + ", 日期：" + date + "， 内容：" +body);
                }
                cursor.close();
            }
        }
    }

}
