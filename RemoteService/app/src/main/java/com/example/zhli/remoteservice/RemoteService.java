package com.example.zhli.remoteservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.INotificationSideChannel;

public class RemoteService extends Service {
    @Override
    public void onCreate() {
        System.out.println("remote service has created");
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {

        return new MiddlePerson();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        System.out.println("remote service has destoried");
        super.onDestroy();
    }

    private void methodInService() {
        System.out.println("I'am a methond in RemoteService, I have been called");
    }

    // 创建一个中间人
    private class MiddlePerson extends IMiddlePerson.Stub {
        public void callMethodInService() {
            methodInService();
        }
    }
}
