package com.example.zhli.mobilesafe.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 号码归属地查询工具类
 * Created by zhli on 2015/1/28.
 */
public class NumberAddressQueryUtils {

    private static String  path = "data/data/com.example.zhli.mobilesafe/files/address.db";

    /**
     * @param number 电话号码
     * @return 相应的号码归属地
     */
    public static String queryNumber(String number) {
        String address = number;
        SQLiteDatabase database = database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        // 手机号码正则表达式
        if(number.matches("^1[34568]\\d{9}$")) {
            // path 把 address.db 数据库文件 copy 到 data/data/com.example.zhli.mobilesafe/files/address.db
            String sql = "select location from data2 where id = (select outkey from data1 where id = ?)";
            Cursor cursor = database.rawQuery(sql, new String[]{number.substring(0, 7)});
            while (cursor.moveToNext()) {
                address = cursor.getString(0);
            }
            cursor.close();
        } else {
            // 110 等其他
            switch (number.length()) {
                case 3:             // 110 119
                    address = "特殊号码";
                    break;
                case 4:
                    address = "模拟器";
                    break;
                case 5:
                    address = "客服电话";
                    break;
                case 7:
                case 8:
                    address = "本地号码";
                    break;
                default:
                    // 长途电话 > 10位，0xx-xxxxxxxxxx
                    if(number.length() > 10 && number.startsWith("0")) {
                        // 010-123231221    --> 10
                        Cursor cursor = database.rawQuery("select location from data2 where area = ?", new String[] {number.substring(1, 3)});
                        while (cursor.moveToNext()) {
                            address = cursor.getString(0);      // 北京移动
//                            address = address.substring(0, location.length() - 2); // 北京
                        }
                        cursor.close();

                        // 0855-123231221    --> 10
                        cursor = database.rawQuery("select location from data2 where area = ?", new String[] {number.substring(1, 4)});
                        while (cursor.moveToNext()) {
                            address = cursor.getString(0);      // 北京移动
//                            address = address.substring(0, location.length() - 2); // 北京
                        }
                        cursor.close();
                    }
                    break;
            }
        }
        return address;
    }

}
