package com.frank.ycj.helpdesk.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.frank.ycj.helpdesk.R;
import com.frank.ycj.helpdesk.config.Global;
import com.frank.ycj.helpdesk.entity.TaskInfo;
import com.mic.etoast2.EToast2;
import com.mic.etoast2.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class FillMethodActivity extends AppCompatActivity {

    private EditText et_method,et_install_time;
    private RadioButton r1,r2;
    private String taskStyle="一般任務";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_method);

        et_method=findViewById(R.id.editText12);
        et_install_time=findViewById(R.id.editText13);

        r1=findViewById(R.id.radioButton10);
        r2=findViewById(R.id.radioButton11);
        et_install_time.setVisibility(View.GONE);
        r1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskStyle="一般任務";
                et_install_time.setVisibility(View.GONE);
            }
        });

        r2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_install_time.setVisibility(View.VISIBLE);
                taskStyle="安裝設備";
            }
        });

        et_install_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(FillMethodActivity.this,SelectTimeActivity.class),12);
            }
        });

        findViewById(R.id.button6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");//设置日期格式
                    String nowDate = df.format(new Date());// new Date()为获取当前系统时间
                    TaskInfo taskInfo=Global.taskInfo;
                    taskInfo.setTaskSolvedMan(Global.userInfo.getUserName());
                    taskInfo.setTaskSolvedDate(nowDate);
                    taskInfo.setTaskSolvedMethod(et_method.getText().toString());
                    taskInfo.setTaskState("已完成");
                    taskInfo.setTaskStyle(taskStyle);
                    if (taskStyle.contains("安裝設備")){
                        taskInfo.setInstallDate(et_install_time.getText().toString());
                        taskInfo.setInstallState("待安装");
                    }
                    taskInfo.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e==null){
                                Toast.makeText(FillMethodActivity.this,"提交成功",EToast2.LENGTH_LONG).show();
                                finish();
                            }else {
                                Toast.makeText(FillMethodActivity.this,"提交失敗，請檢查網絡或返回重試",EToast2.LENGTH_LONG).show();
                            }
                        }
                    });

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==12){
            et_install_time.setText(Global.dateStr+" "+Global.timeStr);
        }
    }
}
