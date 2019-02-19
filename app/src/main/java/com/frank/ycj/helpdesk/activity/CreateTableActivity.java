package com.frank.ycj.helpdesk.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.frank.ycj.helpdesk.R;
import com.frank.ycj.helpdesk.entity.TaskInfo;
import com.frank.ycj.helpdesk.entity.UserInfo;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class CreateTableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_table);

        UserInfo userInfo=new UserInfo();
        userInfo.setUserName("test");
        userInfo.setPassWord("test");
        userInfo.setUserNick("test");
        userInfo.setSex("test");
        userInfo.setPostion("test");
        userInfo.setUserIntroduce("test");
        userInfo.setRole("test");
        userInfo.setEmail("test");
        userInfo.setError(0);

        userInfo.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {

            }
        });


        TaskInfo taskInfo=new TaskInfo();
        taskInfo.setTaskId("test");
        taskInfo.setTaskName("test");
        taskInfo.setTaskContent("test");
        taskInfo.setTaskPublishDate("test");
        taskInfo.setTaskExpectDate("test");
        taskInfo.setTaskExecuteDate("test");
        taskInfo.setTaskSolvedDate("test");
        taskInfo.setTaskSolvedMan("test");
        taskInfo.setTaskSolvedMethod("test");
        taskInfo.setImportantTask("test");
        taskInfo.setImportantPosition("test");
        taskInfo.setImportantDegree("test");
        taskInfo.setTaskState("test");
        taskInfo.setTaskStyle("test");
        taskInfo.setPublisher("test");
        taskInfo.setPublisherName("test");
        taskInfo.setInstallDate("test");
        taskInfo.setInstallState("test");
        taskInfo.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {

            }
        });


    }
}
