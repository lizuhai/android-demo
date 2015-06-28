package com.example.zhli.ipdial;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    private EditText etNumber;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNumber = (EditText) findViewById(R.id.et_number);
        sp = getSharedPreferences("config", MODE_PRIVATE);
    }


    public void save(View v) {
        String ipNumber = etNumber.getText().toString().trim();
        if(TextUtils.isEmpty(ipNumber)) {
            Toast.makeText(this, "claear ip dail ok", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "set ip dail ok", Toast.LENGTH_SHORT).show();
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("ipNumber", ipNumber);
        editor.commit();
    }
}
