package com.frank.ycj.helpdesk.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.frank.ycj.helpdesk.R;
import com.frank.ycj.helpdesk.config.Global;
import com.frank.ycj.helpdesk.entity.TaskInfo;
import com.mic.etoast2.EToast2;
import com.mic.etoast2.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class PublishTaskActivity extends AppCompatActivity {

    private EditText name,content,tv_date,position;
    private RadioButton zhongdu,fuwuqi,tingdian;
    private RadioGroup radioGroup;
    private String importantThing="中毒";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_task);

        name=findViewById(R.id.editText8);
        content=findViewById(R.id.editText9);
        tv_date=findViewById(R.id.editText10);
        position=findViewById(R.id.editText11);
        zhongdu=findViewById(R.id.radioButton7);
        fuwuqi=findViewById(R.id.radioButton8);
        tingdian=findViewById(R.id.radioButton9);
        radioGroup=findViewById(R.id.rg_task_style);
        if (Global.taskStyle.contains("普通任務")){
            position.setVisibility(View.GONE);
            radioGroup.setVisibility(View.GONE);
        }else if (Global.taskStyle.contains("特別任務")){
            position.setVisibility(View.GONE);
        }/*else if (Global.taskStyle.contains("重要人士")){
            radioGroup.setVisibility(View.GONE);
        }*/

        zhongdu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importantThing="中毒";
            }
        });
        fuwuqi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importantThing="服務器問題";
            }
        });
        tingdian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importantThing="停電通知";
            }
        });
        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(PublishTaskActivity.this,SelectTimeActivity.class),11);
            }
        });

        findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verifyData()){
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");//设置日期格式
                    String nowDate = df.format(new Date());// new Date()为获取当前系统时间
                    TaskInfo taskInfo=new TaskInfo();
                    taskInfo.setTaskId(UUID.randomUUID().toString());
                    taskInfo.setTaskName(name.getText().toString());
                    taskInfo.setTaskContent(content.getText().toString());
                    taskInfo.setTaskExpectDate(tv_date.getText().toString());
                    taskInfo.setImportantPosition(Global.userInfo.getPostion());
                    /*if (Global.userInfo.getPostion().contains("經理")){
                        taskInfo.setImportantPosition(Global.userInfo.getPostion());
                    }else*/ if (Global.taskStyle.contains("特別任務")){
                        taskInfo.setImportantTask(importantThing);
                    }
                    taskInfo.setImportantDegree(Global.taskStyle);
                    taskInfo.setTaskState("等待");
                    taskInfo.setTaskPublishDate(nowDate);
                    taskInfo.setPublisher(Global.userInfo.getUserName());
                    taskInfo.setPublisherName(Global.userInfo.getUserNick());

                    taskInfo.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e==null){
                                Toast.makeText(PublishTaskActivity.this,"發佈成功",EToast2.LENGTH_LONG).show();
                                finish();
                            }else {
                                e.printStackTrace();
                                Toast.makeText(PublishTaskActivity.this,"發佈失敗，請檢查網絡或服務器",EToast2.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

    }

    private boolean verifyData() {
        if (name.getText().length()<2){
            Toast.makeText(PublishTaskActivity.this,"請輸入不少於2個字符任務名稱",EToast2.LENGTH_LONG).show();
            return false;
        }
        if (content.getText().length()<2){
            Toast.makeText(PublishTaskActivity.this,"請輸入不少於2個字符任務內容",EToast2.LENGTH_LONG).show();
            return false;
        }
        if (tv_date.getText().length()<2){
            Toast.makeText(PublishTaskActivity.this,"請雙擊選擇任務時間",EToast2.LENGTH_LONG).show();
            return false;
        }
        /*if (Global.taskStyle.contains("重要人士")&&position.getText().length()<2){
            Toast.makeText(PublishTaskActivity.this,"请填写重要人士職位",EToast2.LENGTH_LONG).show();
            return false;
        }*/
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==11){
            tv_date.setText(Global.dateStr+" "+Global.timeStr);
        }
    }
}
