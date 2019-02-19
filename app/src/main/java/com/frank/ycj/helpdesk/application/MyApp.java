package com.frank.ycj.helpdesk.application;

import android.app.Application;

import com.frank.ycj.helpdesk.bomb.BmobDBHelper;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //bmob数据库初始化
        BmobDBHelper.getInstance().init(this);
    }
}
