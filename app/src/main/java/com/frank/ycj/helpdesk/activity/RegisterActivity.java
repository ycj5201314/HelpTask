package com.frank.ycj.helpdesk.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.frank.ycj.helpdesk.R;
import com.frank.ycj.helpdesk.entity.UserInfo;
import com.mic.etoast2.EToast2;
import com.mic.etoast2.Toast;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends AppCompatActivity {

    private EditText userName,passWord,sex,addr,userIntroduce,userNick;
    private String roleName="普通用戶";
    private RadioButton r1,r2,r3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userName=findViewById(R.id.editText3);
        userNick=findViewById(R.id.editText14);
        passWord=findViewById(R.id.editText4);
        sex=findViewById(R.id.editText5);
        addr=findViewById(R.id.editText6);
        userIntroduce=findViewById(R.id.editText7);

        r1=findViewById(R.id.radioButton4);
        r2=findViewById(R.id.radioButton5);
        r3=findViewById(R.id.radioButton6);

        r1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roleName="普通用戶";
            }
        });

        r2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roleName="IT同事";
            }
        });

        r3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roleName="IT經理";
            }
        });

        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verify()){

                    UserInfo userInfo=new UserInfo();
                    userInfo.setUserName(userName.getText().toString());
                    userInfo.setPassWord(passWord.getText().toString());
                    userInfo.setSex(sex.getText().toString());
                    userInfo.setUserNick(userNick.getText().toString());
                    userInfo.setPostion(addr.getText().toString());
                    userInfo.setUserIntroduce(userIntroduce.getText().toString());
                    userInfo.setRole(roleName);

                    userInfo.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e==null){
                                Toast.makeText(RegisterActivity.this,"註冊成功",EToast2.LENGTH_LONG).show();
                                finish();
                            }else {
                                Toast.makeText(RegisterActivity.this,"註冊失敗",EToast2.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private boolean verify() {
        if (userName.getText().length()<3){
            Toast.makeText(RegisterActivity.this,"請輸入不少於3個字符的用戶帳號",EToast2.LENGTH_LONG).show();
            return false;
        }
        if (passWord.getText().length()<3){
            Toast.makeText(RegisterActivity.this,"請輸入不少於3個字符的用戶密碼",EToast2.LENGTH_LONG).show();
            return false;
        }
        if (sex.getText().length()<1){
            Toast.makeText(RegisterActivity.this,"請輸入用戶性別",EToast2.LENGTH_LONG).show();
            return false;
        }
        if (userNick.getText().length()<1){
            Toast.makeText(RegisterActivity.this,"請輸入不少於2個字符的用戶姓名",EToast2.LENGTH_LONG).show();
            return false;
        }
        if (addr.getText().length()<2){
            Toast.makeText(RegisterActivity.this,"請輸入不少於2個字符的用戶職位",EToast2.LENGTH_LONG).show();
            return false;
        }
        if (userIntroduce.getText().length()<3){
            Toast.makeText(RegisterActivity.this,"請輸入不少於3個字符的用戶簡介",EToast2.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
}
