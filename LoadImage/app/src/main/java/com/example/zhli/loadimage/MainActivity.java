package com.example.zhli.loadimage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    private ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv = (ImageView) findViewById(R.id.iv);
    }


    /**
     * 加载图片到内存
     * @param v
     */
    public void loadImageToMemory(View v) {
        // 相当消耗内存资源
//        Bitmap bitmap = BitmapFactory.decodeFile("/mnt/sdcard/DCIM/Camera/IMG_20150123_074551.jpg");
//        iv.setImageBitmap(bitmap);

        // 缩放图片
            // 1. 得到手机屏幕的像素（宽高）
        WindowManager wm = getWindowManager();
                // API 13+ 才有用，若要兼容低版本的手机，用
                // int width = wm.getDefaultDisplay().getWidth;
                // int height = wm.getDefaultDisplay().getHeight;
        Point outSize = new Point();
        wm.getDefaultDisplay().getSize(outSize);
        int screenWidth = outSize.x;
        int screenHeight = outSize.y;
        System.out.println("屏幕宽高：" + screenWidth + ", " + screenHeight);

            // 2. 得到图片的像素（宽高）
        BitmapFactory.Options opts = new BitmapFactory.Options();   // 解析图片的附加条件
        opts.inJustDecodeBounds = true;                         // 不去解析真实的位图，只是获取位图的头文件信息
        Bitmap bitmap = BitmapFactory.decodeFile("/mnt/sdcard/DCIM/Camera/IMG_20150123_074551.jpg", opts);
        int bitmapWidth = opts.outWidth;
        int bitmapHeight = opts.outHeight;
        System.out.println("图片宽高：" + bitmapWidth + ", " + bitmapHeight);

            // 3. 计算缩放比例
        int dx = bitmapWidth / screenWidth;
        int dy = bitmapHeight / screenHeight;
        int scale = 1;
        if(dx > dy && dy > 1) {
            System.out.println("按水平缩放,缩放比例：" + dx);
            scale = dx;
        }
        if(dx < dy && dx > 1) {
            System.out.println("按垂直缩放,缩放比例：" + dy);
            scale = dy;
        }

            // 4. 缩放加载图片到内存
        opts.inSampleSize = scale;
        opts.inJustDecodeBounds = false;        // 真正加载图片
        bitmap = BitmapFactory.decodeFile("/mnt/sdcard/DCIM/Camera/IMG_20150123_074551.jpg", opts);

            // 5. 展示图片
        iv.setImageBitmap(bitmap);
    }
}
