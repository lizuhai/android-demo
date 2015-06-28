package com.example.zhli.mobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class GPSService extends Service {

    private LocationManager lm;
    private MyLocationListener listener;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
//        List<String> providers = lm.getAllProviders();      // 有三种定位方式（网络，gps，基站）
//        for (String l : providers) {
//            System.out.println(l);
//        }

        // 监听位置服务注册
        listener = new MyLocationListener();
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
//        criteria.setAccuracy(Criteria.ACCURACY_FINE);//设置为最大精度
//        criteria.setAltitudeRequired(false);//不要求海拔信息
//        criteria.setBearingRequired(false);//不要求方位信息
//        criteria.setCostAllowed(true);//是否允许付费
//        criteria.setPowerRequirement(Criteria.POWER_LOW);//对电量的要求

        String provider = lm.getBestProvider(criteria, true);         // 在 criteria 条件下选最好的位置提供者
        // lm.requestLocationUpdates("gps", 60000-> 1分中, 50 米, listener);   //一分钟更新一次数据
        lm.requestLocationUpdates(provider, 0, 0, listener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 取消监听位置服务
        lm.removeUpdates(listener);
        listener = null;
    }

    class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            String longitude = "j:" + location.getLongitude() + "\n";    // 经度
            String latitude = "w:" + location.getLatitude() + "\n";      // 纬度
            String accracy = "a:" + location.getAccuracy() + "\n";    // 精确度

            // 发送短信给安全号码
            SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("lastlocation", longitude + latitude + accracy);
            editor.commit();
        }

        /**
         * 关闭/开启 GPS
         */
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        /**
         * 某一个（网络，gps，基站）可用了，触发
         */
        @Override
        public void onProviderEnabled(String provider) {

        }

        /**
         * 某一个（网络，gps，基站）不可用了，触发
         */
        @Override
        public void onProviderDisabled(String provider) {

        }
    }
}
