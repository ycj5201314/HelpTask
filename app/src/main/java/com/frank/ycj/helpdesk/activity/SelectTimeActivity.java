package com.frank.ycj.helpdesk.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.frank.ycj.helpdesk.R;
import com.frank.ycj.helpdesk.config.Global;
import com.mic.etoast2.EToast2;
import com.mic.etoast2.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SelectTimeActivity extends AppCompatActivity {

    private DatePicker datePicker;
    private TimePicker timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_time);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String nowDate = df.format(new Date());// new Date()为获取当前系统时间

        datePicker = (DatePicker) findViewById(R.id.dpPicker);
        timePicker = (TimePicker) findViewById(R.id.tpPicker);
        String dd[]=nowDate.split("-");

        datePicker.init(Integer.parseInt(dd[0]), Integer.parseInt(dd[1]), Integer.parseInt(dd[2]), new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                // 获取一个日历对象，并初始化为当前选中的时间
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Global.dateStr=format.format(calendar.getTime());
                Toast.makeText(SelectTimeActivity.this, format.format(calendar.getTime()),EToast2.LENGTH_LONG).show();
            }
        });

        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay,
                                      int minute) {
                Global.timeStr=hourOfDay+":"+minute;
                Toast.makeText(SelectTimeActivity.this, hourOfDay + "小時" + minute + "分鐘"+"",EToast2.LENGTH_LONG).show();
            }
        });
    }
}
