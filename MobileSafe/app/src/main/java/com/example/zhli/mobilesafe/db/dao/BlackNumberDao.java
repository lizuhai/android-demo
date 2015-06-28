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
 * 黑名单数据库的 CURD
 */
public class BlackNumberDao {
    private BlackNumberDBOpenHelper helper;

    public BlackNumberDao(Context context) {
        helper = new BlackNumberDBOpenHelper(context);
    }

    /**
     * 查询黑名单号码是否存在
     * @param number
     * @return
     */
    public boolean query(String number) {
        boolean result = false;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from blacknumber where number = ?", new String[]{number});
        if(cursor.moveToNext())
            result = true;
        cursor.close();
        db.close();
        return result;
    }

    /**
     * 查询黑名单号码的拦截模式
     * @param number
     * @return 号码的拦截模式，不是黑名单号码就返回 null
     */
    public String queryForMode(String number) {
        String result = null;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select mode from blacknumber where number = ?", new String[]{number});
        if(cursor.moveToNext())
            result = cursor.getString(0);
        cursor.close();
        db.close();
        return result;
    }

    /**
     * 查询全部黑名单号码
     * @return List<BlackNumberInfo>
     */
    public List<BlackNumberInfo> queryAll() {
        List<BlackNumberInfo> result = new ArrayList<BlackNumberInfo>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select number, mode from blacknumber order by _id desc", null);
        while (cursor.moveToNext()) {
            BlackNumberInfo info = new BlackNumberInfo();
            info.setNumber(cursor.getString(0));
            info.setMode(cursor.getString(1));
            result.add(info);
        }
        cursor.close();
        db.close();
        return result;
    }

    /**
     * 查询部分黑名单号码
     * @return List<BlackNumberInfo>
     */
    public List<BlackNumberInfo> queryPart(int offset, int maxnumber) {
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        List<BlackNumberInfo> result = new ArrayList<BlackNumberInfo>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select number, mode from blacknumber order by _id desc limit ? offset ?",
                new String[] {String.valueOf(maxnumber), String.valueOf(offset)});
        while (cursor.moveToNext()) {
            BlackNumberInfo info = new BlackNumberInfo();
            info.setNumber(cursor.getString(0));
            info.setMode(cursor.getString(1));
            result.add(info);
        }
        cursor.close();
        db.close();
        return result;
    }

    /**
     * 添加黑名单号码
     * @param number 黑名单号码
     * @param mode 拦截模式（1：电话拦截，2：短信拦截，3：全部拦截）
     */
    public void insert(String number, String mode) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("number", number);
        values.put("mode", mode);
        db.insert("blacknumber", null, values);
        db.close();
    }

    /**
     * 修改黑名单号码的拦截模式
     * @param number 黑名单号码
     * @param newMode 新的拦截模式
     */
    public void update(String number, String newMode) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("mode", newMode);
        db.update("blacknumber", values, "number = ?", new String[] {number});
        db.close();
    }

    /**
     * 删除黑名单号码
     * @param number 黑名单号码
     */
    public void delete(String number) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete("blacknumber", "number = ?", new String[] {number});
        db.close();
    }
}
