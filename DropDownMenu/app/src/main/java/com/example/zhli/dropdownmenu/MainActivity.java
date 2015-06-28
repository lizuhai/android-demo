package com.example.zhli.dropdownmenu;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private EditText input;
    private ImageView down_arrow;
    private List<String> msgList;
    private PopupWindow popupWindow;
    private ListView listView;
    private MyListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input = (EditText) findViewById(R.id.input);
        down_arrow = (ImageView) findViewById(R.id.down_arrow);
        msgList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            msgList.add("061210" + i);
        }

        initListView();

        down_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 定义 popupWindow
                popupWindow = new PopupWindow(MainActivity.this);
                popupWindow.setWidth(input.getWidth());
                popupWindow.setHeight(200);
                popupWindow.setContentView(listView);   // 为popWindow设置填充内容
                popupWindow.setOutsideTouchable(true);  // 点击 popupWindow 之外的区域，取消掉 弹出窗口
                popupWindow.showAsDropDown(input, 0, 0);    // 设置弹出窗口显示的位置

            }
        });
    }

    private void initListView() {
        listView = new ListView(this);
        listView.setBackgroundResource(R.drawable.listview_background);
        listView.setDivider(null);      // 设置分割线为null
        listView.setVerticalScrollBarEnabled(false);    // 关闭竖直滑动条
        adapter = new MyListViewAdapter();
        listView.setAdapter(adapter);
    }

    private class MyListViewAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return msgList.size();
        }
        @Override
        public Object getItem(int position) {
            return position;
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(), R.layout.list_item, null);
                holder = new ViewHolder();
                holder.delete = (ImageView) convertView.findViewById(R.id.delete);
                holder.tv_msg = (TextView) convertView.findViewById(R.id.tv_list_item);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv_msg.setText(msgList.get(position));
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 删除对应的条目
                    msgList.remove(position);
                    // 刷新 listView
                    MyListViewAdapter.this.notifyDataSetChanged();
                }
            });

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 设置输入框
                    input.setText(msgList.get(position));
                    if (popupWindow != null)
                        popupWindow.dismiss();
                }
            });
            return convertView;
        }

    }

    private class ViewHolder {
        TextView tv_msg;
        ImageView delete;
    }
}
