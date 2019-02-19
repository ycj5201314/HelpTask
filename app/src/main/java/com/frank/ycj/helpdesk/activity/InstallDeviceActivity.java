package com.frank.ycj.helpdesk.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.frank.ycj.helpdesk.R;
import com.frank.ycj.helpdesk.config.Global;
import com.frank.ycj.helpdesk.entity.TaskInfo;
import com.mic.etoast2.EToast2;
import com.mic.etoast2.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class InstallDeviceActivity extends AppCompatActivity {

    private ListView lv_trip_info;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_install_device);

        lv_trip_info=findViewById(R.id.lv_install_device);
        myAdapter=new MyAdapter(InstallDeviceActivity.this);
        //Log.i("图片url", "onCreate: "+SecondHInfo.sencondHandInfo.getPicUrl());
        lv_trip_info.setAdapter(myAdapter);
        lv_trip_info.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        getTaskInfo("taskSolvedMan",Global.userInfo.getUserName(),"installState","待安装");
    }



    /**查询多条数据,查询的数据条数最多500**/
    private void getTaskInfo(String columName1,String columValue1,String columName2,String columValue2){
        BmobQuery<TaskInfo> query = new BmobQuery<TaskInfo>();
        query.addWhereEqualTo(columName1, columValue1);
        query.addWhereEqualTo(columName2, columValue2);

        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(100);
        //执行查询方法
        query.findObjects(new FindListener<TaskInfo>() {
            @Override
            public void done(List<TaskInfo> list, BmobException e) {
                if (e==null){
                    myAdapter.list=list;
                    myAdapter.notifyDataSetChanged();
                }else {
                    e.printStackTrace();
                    Toast.makeText(InstallDeviceActivity.this,"網絡或服務器異常",EToast2.LENGTH_LONG).show();
                }
            }
        });
    }

    private class MyAdapter extends BaseAdapter {

        private LayoutInflater layoutInflater;
        List<TaskInfo> list=new ArrayList<>();

        public MyAdapter(Context context){
            this.layoutInflater=LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView==null){
                convertView=layoutInflater.inflate(R.layout.item_receive_task_info,null);
                holder=new ViewHolder();
                holder.tv_name=convertView.findViewById(R.id.tv_task_name1);
                holder.tv_content=convertView.findViewById(R.id.tv_task_content1);
                holder.tv_expect_date=convertView.findViewById(R.id.tv_task_expect_date1);
                holder.tv_publish_date=convertView.findViewById(R.id.tv_task_publish_date1);
                holder.tv_important=convertView.findViewById(R.id.tv_task_important1);
                holder.tv_state=convertView.findViewById(R.id.tv_task_state1);
                holder.btn_look=convertView.findViewById(R.id.btn_look_history);
                holder.btn_receive=convertView.findViewById(R.id.btn_receive_task);

                convertView.setTag(holder);
            }
            else {
                holder=(ViewHolder)convertView.getTag();
            }

            holder.tv_name.setText("任務名稱："+list.get(position).getTaskName());
            holder.tv_content.setText("任務內容："+list.get(position).getTaskContent());
            holder.tv_expect_date.setText("解決時間："+list.get(position).getTaskSolvedDate());
            holder.tv_publish_date.setText("解決方案："+list.get(position).getTaskSolvedMethod());
            holder.tv_important.setText("安裝時間："+list.get(position).getInstallDate());
            holder.tv_state.setText("安裝狀態："+list.get(position).getInstallState());


            holder.btn_look.setText("查看歷史任務");
            holder.btn_receive.setText("安裝完成");

            holder.btn_look.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Global.taskName=list.get(position).getTaskName();
                    startActivity(new Intent(InstallDeviceActivity.this,SimilarTaskActivity.class));

                }
            });

            holder.btn_receive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TaskInfo taskInfo=list.get(position);
                    taskInfo.setInstallState("已完成");
                    taskInfo.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e==null){
                                Toast.makeText(InstallDeviceActivity.this,"操作成功",EToast2.LENGTH_LONG).show();
                                list.remove(position);
                                myAdapter.notifyDataSetChanged();
                            }else {
                                Toast.makeText(InstallDeviceActivity.this,"操作失敗，請檢查網絡或者刷新數據",EToast2.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            });

            return convertView;
        }
    }

    public final class ViewHolder{
        public TextView tv_name,tv_content,tv_expect_date,tv_publish_date,tv_important,tv_state;
        public Button btn_look,btn_receive;
    }
}
