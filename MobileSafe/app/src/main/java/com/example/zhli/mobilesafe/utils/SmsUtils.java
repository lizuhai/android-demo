package com.example.zhli.mobilesafe.utils;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhli on 2015/1/29.
 * 短信工具类
 */
public class SmsUtils {

    /**
     * 短信备份的回掉接口
     */
    public interface BackupCallBack {
        /**
         * 开始备份时设置进度最大值
         * @param max
         */
        public void beforeBackup(int max);

        /**
         * 备份过程中，增加进度
         * @param process：当前进度
         */
        public void onSmsBackup(int process);
    }

    /**
     * 短信备份
     * @param context 上下文
     */
//    public static void backupSms(Context context, ProgressDialog pd) throws Exception {
    public static void backupSms(Context context, BackupCallBack bcb) throws Exception {
        File file = new File(Environment.getExternalStorageDirectory(), "backup.xml");
        FileOutputStream fos = new FileOutputStream(file);

        XmlSerializer serializer = Xml.newSerializer();
        ContentResolver resolver = context.getContentResolver();
        serializer.setOutput(fos, "utf-8");
        serializer.startDocument("utf-8", true);
        serializer.startTag(null, "smss");

        Uri uri = Uri.parse("content://sms/");
        Cursor cursor = resolver.query(uri, new String[]{"address", "date", "type", "body"}, null, null, null);

        int max = cursor.getCount();
        serializer.attribute(null, "max", max + "");    // 为了解析时候可以获得最大值

        // 设置进度条的最大值
//        pd.setMax(cursor.getCount());
        bcb.beforeBackup(max);

        int process = 0;
        while (cursor.moveToNext()) {
            String address = cursor.getString(0);
            String date = cursor.getString(1);
            String type = cursor.getString(2);
            String body = cursor.getString(3);

            serializer.startTag(null, "sms");

            serializer.startTag(null, "address");
            serializer.text(address);
            serializer.endTag(null, "address");

            serializer.startTag(null, "date");
            serializer.text(date);
            serializer.endTag(null, "date");

            serializer.startTag(null, "type");
            serializer.text(type);
            serializer.endTag(null, "type");

            serializer.startTag(null, "body");
            serializer.text(body);
            serializer.endTag(null, "body");

            serializer.endTag(null, "sms");

            // 设置进度条的当前进度
//            pd.setProgress(++process);
            bcb.onSmsBackup(++process);
        }
        cursor.close();
        serializer.endTag(null, "smss");
        serializer.endDocument();
        fos.close();
    }

    /**
     * 短信备份还原
     * @param context 上下文
     */
    public static void restoreSms(Context context) {
        Xml.newPullParser();

    }

}
