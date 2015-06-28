package com.example.zhli.copyimage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;


public class MainActivity extends ActionBarActivity {

    private ImageView iv1;
    private ImageView iv2;
    private Bitmap srcBitmap;
    private Bitmap alterBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv1 = (ImageView) findViewById(R.id.iv1);
        iv2 = (ImageView) findViewById(R.id.iv2);

        // 给 iv1 一个默认的位图
        srcBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tomcat);
        iv1.setImageBitmap(srcBitmap);


        // 创建一个可以修改的原图的副本。创建的是一个空白的图形
        alterBitmap = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), srcBitmap.getConfig());
//        Bitmap.createBitmap(Bitmap src);      // 与上面区别是，他是不可修改的


    }

    /**
     * copy 上面的图片
     * @param v
     */
    public void copy(View v) {
        Canvas canvas = new Canvas(alterBitmap);

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);    // 设置画笔默认颜色

        Matrix m = new Matrix();        // 原比例
//        m.setRotate(30);              // 旋转
        m.setScale(-1.0f, 1.0f);        // 缩放（简单 api）
        m.postTranslate(srcBitmap.getWidth(), 0);   // 移动
//        m.setRotate(180, srcBitmap.getWidth() / 2, srcBitmap.getHeight() / 2);   // 图像中心旋转
        // 图形缩放（复杂 api）
//        m.setValues(new float[] {
//                0.5f, 0, 0,
//                0, 0.5f, 0,
//                0, 0, 1
//        });

        // 更改图片颜色
        ColorMatrix cm = new ColorMatrix();
        cm.set(new float[] {
                0.5f, 0, 0, 0, 0,
                0, 1.0f, 0, 0, 0,
                0, 0, 1, 0, 0,
                0, 0, 0, 1, 0
        });
        paint.setColorFilter(new ColorMatrixColorFilter(cm));

        canvas.drawBitmap(srcBitmap, m, paint);

        iv2.setImageBitmap(alterBitmap);
    }
}
