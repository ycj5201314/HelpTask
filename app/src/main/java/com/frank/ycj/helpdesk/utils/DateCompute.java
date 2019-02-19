package com.frank.ycj.helpdesk.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateCompute {
    public static String getTimeDifference(String startTime,String endTime) {
        //格式日期格式，在此我用的是"2018-01-24 19:49:50"这种格式
        //可以更改为自己使用的格式，例如：yyyy/MM/dd HH:mm:ss 。。。
        //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String result="";
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try{
            Date now = df.parse(endTime);
            Date date=df.parse(startTime);
            long l=now.getTime()-date.getTime();       //获取时间差
            long day=l/(24*60*60*1000);
            long hour=(l/(60*60*1000)-day*24);
            long min=((l/(60*1000))-day*24*60-hour*60);
            long s=(l/1000-day*24*60*60-hour*60*60-min*60);
            //result=day+"天"+hour+"小时"+min+"分"+s+"秒";
            result=day+"天"+hour+"小时"+min+"分";
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

}
