package com.example.zhli.application2;

import android.view.View;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

    private Button btnGetMoney;
    private Button btnLostMoney;
    private TextView tvGetMoney;
    private EditText editGoalMoney;
    private Button btnSwitchImage;
    private ImageView ivAndroid;
    private int money = 0;

    private int[] images = {R.drawable.a, R.drawable.b, R.drawable.c};
    private int imageIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGetMoney = (Button) findViewById(R.id.btnGetMoney);
        tvGetMoney = (TextView) findViewById(R.id.tvGetMoney);
        btnLostMoney = (Button) findViewById(R.id.btnLostMoney);
        editGoalMoney = (EditText) findViewById(R.id.editGoalMoney);
        btnSwitchImage = (Button) findViewById(R.id.btnSwitchImage);
        ivAndroid = (ImageView) findViewById(R.id.ivAndroid);

        btnGetMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strMoney = editGoalMoney.getText().toString().trim();
                int goalMoney = Integer.parseInt(strMoney);
                if (money == goalMoney) {
                    Toast.makeText(MainActivity.this, "you hava come up with your aim!", Toast.LENGTH_SHORT).show();
                } else {
                    money ++;
                    tvGetMoney.setText("我通过点击鼠标轻易赚了" + money + "元");
                }
            }
        });

        btnLostMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(money == 0) {
                    // 弹出框弹出信息
                    Toast.makeText(MainActivity.this, "别再点了，没钱了", Toast.LENGTH_SHORT).show();
                } else {
                    money--;
                    tvGetMoney.setText("我通过点击鼠标轻易lost了" + money + "元");
                }
            }
        });

        btnSwitchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageIndex ++;
                if(imageIndex > 2) {
                    imageIndex = 0;
                }
                ivAndroid.setImageResource(images[imageIndex]);
            }
        });
    }

}