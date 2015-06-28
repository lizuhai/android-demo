package com.example.zhli.mobilesafe;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SelectContactActivity extends ActionBarActivity {

    private ListView list_select_contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contact);

        list_select_contact = (ListView) findViewById(R.id.list_select_contact);
        final List<Map<String, String>> data = getContactInfo();
        list_select_contact.setAdapter(new SimpleAdapter(this, data, R.layout.contact_item_view,
                new String[]{"name", "phone"}, new int[] {R.id.tv_name, R.id.tv_phone}));

        list_select_contact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String phone = data.get(position).get("phone");
                Intent intent = new Intent();
                intent.putExtra("phone", phone);
                setResult(0, intent);

                finish();
            }
        });
    }

    /**
     * 读取手机里面的联系人
     * @return
     */
    private List<Map<String, String>> getContactInfo() {
        List<Map<String, String>> list = new ArrayList();
        // 得到内容解析器
        ContentResolver resolver = getContentResolver();
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri uriData = Uri.parse("content://com.android.contacts/data");
        Cursor cursor = resolver.query(uri, new String[]{"contact_id"}, null, null, null);
        while (cursor.moveToNext()) {
            Map<String, String> map = new HashMap<>();
            String contact_id = cursor.getString(0);
            if(contact_id != null) {
                Cursor dataCursor = resolver.query(uriData, new String[]{"data1", "mimetype"}, "contact_id=?", new String[]{contact_id}, null);
                while (dataCursor.moveToNext()) {
                    String data1 = dataCursor.getString(0);
                    String mimetype = dataCursor.getString(1);
                    if("vnd.android.cursor.item/phone_v2".equals(mimetype)) {       // 电话号码
                        map.put("phone", data1);
                    } else if("vnd.android.cursor.item/name".equals(mimetype)) {    // 姓名
                        map.put("name", data1);
                    }
                }
                list.add(map);
                dataCursor.close();
            }

        }
        cursor.close();
        return list;
    }

}
