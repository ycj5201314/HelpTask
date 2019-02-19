package com.frank.ycj.helpdesk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.DatePicker;

import com.frank.ycj.helpdesk.R;
import com.mic.etoast2.EToast2;
import com.mic.etoast2.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DataSelectActivity extends AppCompatActivity {

    private DatePicker datePicker;
    private static String slecetDate="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_select);


        datePicker=findViewById(R.id.data_select_for_date);

        datePicker.init(2018, 11, 11, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // 获取一个日历对象，并初始化为当前选中的时间
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日  HH:mm:ss");
                Toast.makeText(DataSelectActivity.this,
                        format.format(calendar.getTime()), EToast2.LENGTH_LONG)
                        .show();
                slecetDate=format.format(calendar.getTime());
            }
        });

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode==KeyEvent.KEYCODE_BACK ) {
            //添加给第一个Activity的返回值，并设置resultCode
            Intent intent = new Intent();
            intent.putExtra("date", slecetDate);
            setResult(RESULT_OK, intent);
            finish();
        }

        return false;
    }
}
