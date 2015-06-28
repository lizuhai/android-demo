package com.example.zhli.limitsofauthority;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.FileOutputStream;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 写数据
        writeToLocal("private.txt", Context.MODE_PRIVATE);
        writeToLocal("readable.txt", Context.MODE_WORLD_READABLE);
        writeToLocal("writeable.txt", Context.MODE_WORLD_WRITEABLE);
        writeToLocal("private.txt", Context.MODE_PRIVATE);

        findViewById(R.id.btn_r_private).setOnClickListener(this);
        findViewById(R.id.btn_w_private).setOnClickListener(this);

        findViewById(R.id.btn_r_readable).setOnClickListener(this);
        findViewById(R.id.btn_w_writeable).setOnClickListener(this);

        findViewById(R.id.btn_r_writeable).setOnClickListener(this);
        findViewById(R.id.btn_w_writeable).setOnClickListener(this);

        findViewById(R.id.btn_r_readable_writeable).setOnClickListener(this);
        findViewById(R.id.btn_w_readable_writeable).setOnClickListener(this);

    }

    private void writeToLocal(String fileName, int mode) {
        try {
            FileOutputStream fos = openFileOutput(fileName, mode);
            fos.write(("first program's data" + fileName).getBytes());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 哪一个控件点击，v 就对应哪个点击的对象
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_r_private:
                break;
            case R.id.btn_w_private:
                break;
            case R.id.btn_r_readable:
                break;
            case R.id.btn_w_readable:
                break;
            case R.id.btn_r_writeable:
                break;
            case R.id.btn_w_writeable:
                break;
            case R.id.btn_r_readable_writeable:
                break;
            case R.id.btn_w_readable_writeable:
                break;
            default:
                break;
        }
    }
}
