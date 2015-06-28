package com.example.zhli.backupsms;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void backupSms(View v) {
        // 1.查出所有的短信
        Uri uri = Uri.parse("content://sms/");
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(uri, new String[]{"_id", "address", "date", "type", "body"}, null, null, null);
        if(cursor != null && cursor.getCount() > 0) {
            List<SmsInfo> smsList = new ArrayList<>();
            SmsInfo sms;
            while (cursor.moveToNext()) {
                sms = new SmsInfo();
                sms.setId(cursor.getInt(0));
                sms.setAddress(cursor.getString(1));
                sms.setDate(cursor.getLong(2));
                sms.setType(cursor.getInt(3));
                sms.setBody(cursor.getString(4));
                smsList.add(sms);
            }
            cursor.close();

            // 2.序列化到本地
            writeToLocal(smsList);
        }
    }

    /**
     * 序列化到本地
     */
    private void writeToLocal(List<SmsInfo> smsList) {
//        Toast.makeText(this, smsList.toString(), Toast.LENGTH_SHORT).show();
        XmlSerializer serializer = Xml.newSerializer();
        try {
            FileOutputStream os = new FileOutputStream("/mnt/sdcard/sms.xml");
            serializer.setOutput(os, "utf-8");

            serializer.startDocument("utf-8", true);
            serializer.startTag(null, "smss");
            for(SmsInfo s : smsList) {
                serializer.startTag(null, "sms");
                serializer.attribute(null, "id", String.valueOf(s.getId()));

                serializer.startTag(null, "address");
                serializer.text(s.getAddress());
                serializer.endTag(null, "address");

                serializer.startTag(null, "date");
                serializer.text(String.valueOf(s.getDate()));
                serializer.endTag(null, "date");

                serializer.startTag(null, "type");
                serializer.text(s.getType() + "");
                serializer.endTag(null, "type");

                serializer.startTag(null, "body");
                serializer.text(s.getBody());
                serializer.endTag(null, "body");

                serializer.endTag(null, "sms");
            }
            serializer.endTag(null, "smss");
            serializer.endDocument();

            Toast.makeText(this, "backup ok", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
