package com.example.zhli.apkinstall;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.io.File;


public class MainActivity extends ActionBarActivity {

    private EditText etPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etPath = (EditText) findViewById(R.id.et_path);
    }


    public void install(View v) {
        String path = etPath.getText().toString().trim();
        /**
         *
         <activity android:name=".PackageInstallerActivity"
         android:configChanges="orientation|keyboardHidden"
         android:theme="@style/TallTitleBarTheme">
             <intent-filter>
                 <action android:name="android.intent.action.VIEW" />
                 <category android:name="android.intent.category.DEFAULT" />
                 <data android:scheme="content" />
                 <data android:scheme="file" />
                 <data android:mimeType="application/vnd.android.package-archive" />
             </intent-filter>
         </activity>
         */
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setDataAndType(Uri.fromFile(new File(path)),
                "application/vnd.android.package-archive");
        startActivity(intent);
    }
}
