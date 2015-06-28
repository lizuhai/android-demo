package com.example.zhli.destopapplication;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private List<String> packnames;
    private GridView gv;
    private PackageManager pm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gv = (GridView) findViewById(R.id.gv);

        packnames = new ArrayList<>();
        pm = getPackageManager();
        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        // 查询手机上所有具有启动能力的启动的 activity
        List<ResolveInfo> infos = pm.queryIntentActivities(intent, PackageManager.GET_INTENT_FILTERS);
        for (ResolveInfo info : infos) {
            String packname = info.activityInfo.packageName;
            packnames.add(packname);
        }
        gv.setAdapter(new MyAdapter());
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String packname = packnames.get(position);
                Intent i = pm.getLaunchIntentForPackage(packname);
                startActivity(i);
            }
        });
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return packnames.size();
        }
        @Override
        public Object getItem(int position) {
            return null;
        }
        @Override
        public long getItemId(int position) {
            return 0;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
//            TextView textView = new TextView(getApplicationContext());
            View view = View.inflate(getApplicationContext(), R.layout.item, null);
            TextView tv = (TextView) view.findViewById(R.id.tv);
            ImageView iv = (ImageView) view.findViewById(R.id.iv);
            String packname = packnames.get(position);
            try {
                tv.setText(pm.getPackageInfo(packname, 0).applicationInfo.loadLabel(pm));
                iv.setImageDrawable(pm.getPackageInfo(packname, 0).applicationInfo.loadIcon(pm));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            tv.setTextColor(Color.BLACK);
            return view;
        }
    }

    @Override
    public void onBackPressed() {

    }
}
