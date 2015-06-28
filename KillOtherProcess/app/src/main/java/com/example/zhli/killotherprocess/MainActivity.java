package com.example.zhli.killotherprocess;

import android.app.ActivityManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class MainActivity extends ActionBarActivity {

    private ActivityManager am;     // 相当于进程管理器
    private EditText etPackname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etPackname = (EditText) findViewById(R.id.et_packname);

        am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
    }

    public void click(View v) {
        String packname = etPackname.getText().toString().trim();

        // 只能杀死空进程和后台进程，所以不能自杀
        am.killBackgroundProcesses(packname);
    }

}
