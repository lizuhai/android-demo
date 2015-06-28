package com.example.zhli.mobilesafe.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.zhli.mobilesafe.db.BlackNumberDBOpenHelper;
import com.example.zhli.mobilesafe.domain.BlackNumberInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhli on 2015/1/29.
 * 病毒数据库的 CURD
 */
public class AntivirsuDao {
    /**
     * 查询以个 md5 是否在病毒数据库存在
     * @param md5 文件的 md5 值
     * @return
     */
    public static boolean isVirus(String md5) {
        String path = "/data/data/com.example.zhli.mobilesafe/files/antivirus.db";
        boolean result = false;
        SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = db.rawQuery("select * from datable where md5 = ?", new String[]{md5});
        if(cursor.moveToNext())
            result = true;
        cursor.close();
        db.close();
        return result;
    }

}
