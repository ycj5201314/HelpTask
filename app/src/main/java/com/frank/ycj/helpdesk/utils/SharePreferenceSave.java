package com.frank.ycj.helpdesk.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;


public class SharePreferenceSave {
    //保存账号和登錄密碼到data.xml中
    public static boolean saveUserInfo(Context context, String username,String password){
        SharedPreferences sp = context.getSharedPreferences ( "data", context.MODE_PRIVATE );
        SharedPreferences.Editor edit = sp.edit ();
        edit.putString ( "username",username );
        edit.putString ( "password",password );
        edit.commit ();
        return true;
    }
    //从data.xml文件中获取存储的账号和密码
    public static Map<String,String> getUserInfo(Context context){
        SharedPreferences sp = context.getSharedPreferences ( "data", context.MODE_PRIVATE );
        String username = sp.getString ( "username", "" );
        String password = sp.getString ( "password", "" );
        Map<String,String> userMap = new HashMap<>(  );
        userMap.put ( "username", username );
        userMap.put ( "password", password );
        return userMap;
    }

}
