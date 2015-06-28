package com.example.zhli.contacts;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.nfc.Tag;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.awt.font.TextAttribute;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void queryContacts(View v) {
        // 1. 取出 raw_contacts 中所有 id （多少个人）--> Uri: content://com.android.contacts/raw_contacts
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri dataUri = Uri.parse("content://com.android.contacts/data");
        Cursor cursor = getContentResolver().query(uri, new String[] {"_id"}, null, null, null);
//        printCursor(cursor);
        if(cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);

                // 2. 在 data 表上根据 1 查到的 id取出相应的数据 --> Uri: content://com.android.contacts/data
                String selection = "raw_contact_id = ?";
                String [] selectionArgs = {String.valueOf(id)};
                Cursor cursor1 = getContentResolver().query(dataUri,
                        new String[] {"data1", "mimetype"},     // 这儿不能写成是 mimetype_id, 他是做了一个视图，直接用 mimetype 即可
                        selection,
                        selectionArgs,
                        null);
//                printCursor(cursor1);
                if(cursor1 != null && cursor1.getCount() > 0) {
                    while (cursor1.moveToNext()) {
                        String mimetype = cursor1.getString(1);     // 有三种值：手机号码/姓名/email
                        String data1 = cursor1.getString(0);
                        if("vnd.android.cursor.item/phone_v2".equals(mimetype)) {
                            System.out.println("号码：" + data1);
                        } else if ("vnd.android.cursor.item/name".equals(mimetype)) {
                            System.out.println("姓名：" + data1);
                        } else if ("vnd.android.cursor.item/email_v2".equals(mimetype)) {
                            System.out.println("email：" + data1);
                        }
                    }
                    cursor1.close();
                }
            }
            cursor.close();
        }
    }

    public void insertContacts(View v) {
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri dataUri = Uri.parse("content://com.android.contacts/data");
        // 1. 在raw_contacts 表中添加一条记录
            // 取出 contact_id 值加 1 作为新的 contact_id 的值
        Cursor cursor = getContentResolver().query(uri, new String[]{"contact_id"}, null, null, "contact_id desc limit 1");
        if(cursor != null && cursor.moveToNext()) {
            int contact_id = cursor.getInt(0);
            contact_id ++;
            cursor.close();

            ContentValues values = new ContentValues();
            values.put("contact_id", contact_id);
            getContentResolver().insert(uri, values);

            // 2. 根据上面添加纪录的 id， 向 data 表中添加三条记录
                // 存号码
            values = new ContentValues();
            values.put("mimetype", "vnd.android.cursor.item/phone_v2");
            values.put("data1", "10086");
            values.put("raw_contact_id", contact_id);
            getContentResolver().insert(dataUri, values);

                // 存姓名
            values = new ContentValues();
            values.put("mimetype", "vnd.android.cursor.item/name");
            values.put("data1", "中国移动");
            values.put("raw_contact_id", contact_id);
            getContentResolver().insert(dataUri, values);

                // 存email
            values = new ContentValues();
            values.put("mimetype", "vnd.android.cursor.item/email_v2");
            values.put("data1", "10086@kendie.com");
            values.put("raw_contact_id", contact_id);
            getContentResolver().insert(dataUri, values);
        }
    }

    private void printCursor(Cursor cursor) {
        if(cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int columnCount = cursor.getColumnCount();
                for (int i = 0; i < columnCount; i++) {
                    String columnName = cursor.getColumnName(i);
                    String columnValue = cursor.getString(i);       // 取出对应 i 位置列上的值
                    System.out.println("当前是第" + cursor.getPosition() + "行 " + columnName + "-" + columnValue);
                }
            }
            cursor.close();
        }
    }

}
