package com.example.zhli.rpcalculator;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    private EditText etName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = (EditText) findViewById(R.id.et_name);
    }

    public void enter(View v) {

        String name = etName.getText().toString().trim();
        if(TextUtils.isEmpty(name)) {
            Toast.makeText(this, "请输入姓名", Toast.LENGTH_SHORT).show();
            return;
        }

        // 显式意图：一般用于激活自己应用组件
        // 隐式意图：激活第三方组件，无需关心别人的包名和类名

  // 显式意图
            // 方法 1
//        Intent intent = new Intent();
//        intent.setClassName(getPackageName(), "com.example.zhli.rpcalculator.CalcActivity");  // 直接指定要激活的组件
            // 方法 2 (更简单)
        Intent intent = new Intent(this, CalcActivity.class);
        intent.putExtra("name", name);
            // 激活要显示的页面
        startActivity(intent);

        // 隐士意图
 /*
        Intent intent = new Intent();
        intent.setAction("com.example.zhli.rpcalculator.open");
        intent.addCategory(intent.CATEGORY_DEFAULT);
        // setData 和 setType 两个不能分开同时设置（两个会相互清除），要想同时设置，用setDataAndType
//        intent.setData(Uri.parse("jianren:张三"));
//        intent.setType("application/person");
        intent.setDataAndType(Uri.parse("jianren:张三"), "application/person");
        startActivity(intent);
*/

    }

}
