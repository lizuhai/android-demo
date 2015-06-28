package com.example.zhli.takeoffclothes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;


public class MainActivity extends ActionBarActivity {

    private ImageView iv;
    private Bitmap alterBitmap;     // 可以修改的位图
    private Canvas canvas;          // 画布

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv = (ImageView) findViewById(R.id.iv);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pre1);
        // 创建一个空白的原图拷贝
        alterBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());

        canvas = new Canvas(alterBitmap);

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);

        canvas.drawBitmap(bitmap, new Matrix(), paint);

        iv.setImageBitmap(alterBitmap);

        iv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 手势类型
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:   // 手指按下屏幕时
                        break;
                    case MotionEvent.ACTION_MOVE:   // 手指在屏幕上移动
                        int x = (int) event.getX();       // 相对于 iv 的坐标
//                        event.getRawX();    // 相对于屏幕的x坐标
                        int y = (int) event.getY();
                        System.out.println("设置透明颜色 " + x + ", " + y);

                        for(int i = -4; i < 5; i++) {
                            for(int j = -4; j < 5; j++) {
                                try {
                                    alterBitmap.setPixel(x + i, y + j, Color.TRANSPARENT);
                                } catch (Exception e) {}
                            }
                        }

                        iv.setImageBitmap(alterBitmap);
                        break;
                    case MotionEvent.ACTION_UP:     // 手指离开屏幕时
                        MediaPlayer.create(getApplicationContext(), R.raw.higirl).start();
                        break;
                    case MotionEvent.ACTION_CANCEL:     // 锁屏时候
                        break;
                    default:
                        break;
                }
                return true;        // 返回 true 表示可以重复使用事件，false 是一次性的
            }
        });
    }


}
