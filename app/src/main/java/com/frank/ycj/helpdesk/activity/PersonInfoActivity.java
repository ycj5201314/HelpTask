package com.frank.ycj.helpdesk.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.frank.ycj.helpdesk.R;
import com.frank.ycj.helpdesk.config.Global;

public class PersonInfoActivity extends AppCompatActivity {

    private TextView name,sex,addr,introduce,role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);

        name=findViewById(R.id.textView3);
        sex=findViewById(R.id.textView4);
        addr=findViewById(R.id.textView5);
        introduce=findViewById(R.id.textView7);
        role=findViewById(R.id.textView6);

        name.setText("用戶帳號："+Global.userInfo.getUserName());
        sex.setText("用戶性別："+Global.userInfo.getSex());
        addr.setText("用戶職位："+Global.userInfo.getPostion());
        introduce.setText("用戶簡介："+Global.userInfo.getUserIntroduce());
        role.setText("用戶角色："+Global.userInfo.getRole());
    }
}
