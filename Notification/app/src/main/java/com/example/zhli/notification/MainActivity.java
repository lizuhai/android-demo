package com.example.zhli.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void click(View v) {
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new Notification(R.drawable.notification, "first notification", System.currentTimeMillis());
        notification.flags = Notification.FLAG_AUTO_CANCEL;     // 点击之后自动取消
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:95559"));
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);             // 延期意图
        notification.setLatestEventInfo(this, "Title", "content", contentIntent);
        nm.notify(0, notification);
    }

    /**
     * 新版本(api 16+)才有的
     * @param v
     */
    public void click2(View v) {
        Notification noti = new Notification.Builder(this)
                .setContentTitle("New version notification")
                .setContentText("eeeeeeeeeeeeee")
                .setSmallIcon(R.drawable.notification)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
                .build();

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(0, noti);
    }
}
