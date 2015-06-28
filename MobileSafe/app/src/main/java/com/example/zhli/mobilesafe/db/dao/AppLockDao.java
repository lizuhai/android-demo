package com.example.zhli.mobilesafe.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.zhli.mobilesafe.db.AppLockDBOpenHelper;
import com.example.zhli.mobilesafe.db.BlackNumberDBOpenHelper;
import com.example.zhli.mobilesafe.domain.BlackNumberInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 程序锁 DAO
 * Created by zhli on 2015/1/31.
 */
public class AppLockDao {
    private AppLockDBOpenHelper helper;
    private Context context;

    public AppLockDao(Context context) {
        helper = new AppLockDBOpenHelper(context);
        this.context = context;
    }

    /**
     * 查询程序锁包名记录是否存在
     * @param packname 程序包名
     * @return
     */
    public boolean query(String packname) {
        boolean result = false;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from applock where packname = ?", new String[]{packname});
        if(cursor.moveToNext())
            result = true;
        cursor.close();
        db.close();
        return result;
    }

    /**
     * 查询所有程序锁包名记录
     * @return
     */
    public List<String> queryAll() {
        List<String> protectPacknames = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("applock", new String[]{"packname"}, null, null, null, null, null);
//        Cursor cursor = db.rawQuery("select packname from applock", null);
        while(cursor.moveToNext())
            protectPacknames.add(cursor.getString(0));
        cursor.close();
        db.close();
        return protectPacknames;
    }


    /**
     * 添加要锁定的程序包名
     * @param packname 程序包名
     */
    public void insert(String packname) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("packname", packname);
        db.insert("applock", null, values);
        db.close();

        Intent intent = new Intent();
        intent.setAction("com.example.zhli.mobilesafe.applockchange");
        context.sendBroadcast(intent);
    }

    /**
     * 删除锁定的程序包名
     * @param packname 程序包名
     */
    public void delete(String packname) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete("applock", "packname = ?", new String[] {packname});
        db.close();

        Intent intent = new Intent();
        intent.setAction("com.example.zhli.mobilesafe.applockchange");
        context.sendBroadcast(intent);
    }
}
