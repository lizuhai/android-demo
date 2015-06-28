package com.example.zhli.mobilesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.zhli.mobilesafe.db.dao.BlackNumberDao;

/**
 * Created by zhli on 2015/1/29.
 */
public class BlackNumberDBOpenHelper extends SQLiteOpenHelper {
    // 数据库名称：blacknumber.db
    public BlackNumberDBOpenHelper(Context context) {
        super(context, "blacknumber.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists blacknumber(_id integer primary key autoincrement, number varchar(20), mode varchar(2))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
