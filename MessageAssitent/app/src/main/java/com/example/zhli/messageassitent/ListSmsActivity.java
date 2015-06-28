package com.example.zhli.messageassitent;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class ListSmsActivity extends ActionBarActivity {

    private ListView lv;
    private String[] objects = {"dfa", "a", "b"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_sms);

        lv = (ListView) findViewById(R.id.lv);
        lv.setAdapter(new ArrayAdapter<String>(this,
                R.layout.sms_item,
                R.id.tv_info,
                objects));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String smsInfo = objects[position];
                Intent data = new Intent();
                data.putExtra("smsInfo", smsInfo);

                setResult(0, data);

                finish();   // 关闭界面, 并回传数据
            }
        });
    }



}
