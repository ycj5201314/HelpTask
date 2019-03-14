package com.frank.ycj.helpdesk.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.frank.ycj.helpdesk.R;
import com.frank.ycj.helpdesk.config.Global;
import com.frank.ycj.helpdesk.entity.TaskInfo;
import com.frank.ycj.helpdesk.utils.DateCompute;
import com.mic.etoast2.EToast2;
import com.mic.etoast2.Toast;

import java.sql.Blob;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class ComputeTaskActivity extends AppCompatActivity {
    private ListView lv_trip_info;
    private MyAdapter myAdapter;
    private TextView tv_allTask,tv_compeleteTask;
    private String tag="";
    private int unCompelteTask=0;
    List<TaskInfo> unCompeletelist=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compute_task);

        getUncompeleteInfo("taskState","已完成");
        tag=getIntent().getStringExtra("tag");
        lv_trip_info=findViewById(R.id.lv_had_compelete_task);
        tv_allTask=findViewById(R.id.textView11);
        tv_compeleteTask=findViewById(R.id.textView12);
        myAdapter=new MyAdapter(ComputeTaskActivity.this);
        //Log.i("图片url", "onCreate: "+SecondHInfo.sencondHandInfo.getPicUrl());
        lv_trip_info.setAdapter(myAdapter);
        lv_trip_info.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text ="";
                if (myAdapter.list.get(position).getTaskState().contains("已完成")) {

                     text = "任務執行用戶：" + myAdapter.list.get(position).getTaskSolvedMan() + "\n"+
                             "任務開始時間：" + myAdapter.list.get(position).getTaskExecuteDate() + "\n"
                            + "任務結束時間：" + myAdapter.list.get(position).getTaskSolvedDate() + "\n"
                            + "任務總共用時：" + DateCompute.getTimeDifference(myAdapter.list.get(position).getTaskExecuteDate(),
                            myAdapter.list.get(position).getTaskSolvedDate());

                }else{
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");//设置日期格式
                    String nowDate = df.format(new Date());// new Date()为获取当前系统时间
                     text =  "任務執行用戶：" + myAdapter.list.get(position).getTaskSolvedMan() + "\n"+
                             "任務開始時間：" + myAdapter.list.get(position).getTaskExecuteDate() + "\n"
                            + "任務結束時間：還未完成\n"
                            + "任務已經用時：" + DateCompute.getTimeDifference(myAdapter.list.get(position).getTaskExecuteDate(),
                             nowDate);
                }
                new AlertDialog.Builder(ComputeTaskActivity.this)
                        .setTitle("任務詳情")
                        .setMessage(text)
                        .setPositiveButton("確定",null)
                        .setNegativeButton("取消",null)
                        .create().show();
            }
        });

        startActivityForResult(new Intent(ComputeTaskActivity.this,SelectTimeActivity.class),11);

    }

    /**查询多条数据,查询的数据条数最多500**/
    private void getTaskInfo(String columName,String columValue){
        BmobQuery<TaskInfo> query = new BmobQuery<TaskInfo>();

        if (tag.contains("109")){
            query.addWhereEqualTo("taskSolvedMan",Global.excuteMan);
        }

        query.addWhereGreaterThanOrEqualTo(columName, columValue);
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(500);
        //执行查询方法
        query.findObjects(new FindListener<TaskInfo>() {
            @Override
            public void done(List<TaskInfo> list, BmobException e) {
                if (e==null){
                    myAdapter.list=list;
                    //增加未完成的
                    myAdapter.list.addAll(unCompeletelist);

                    int hadCompeleteTask=0;
                    for (int i=0;i<list.size();i++){
                        if (list.get(i).getTaskState().contains("已完成")){
                            hadCompeleteTask++;
                        }
                    }
                    if (tag.contains("109")){
                        tv_allTask.setText("今日已接受任務："+list.size());
                        tv_compeleteTask.setText("今日已完成任務："+hadCompeleteTask);
                    }else {
                        tv_allTask.setText("今日所有的任務："+(list.size()+unCompelteTask));
                        tv_compeleteTask.setText("今日已完成任務："+hadCompeleteTask);
                    }

                    myAdapter.notifyDataSetChanged();
                }else {
                    e.printStackTrace();
                    Toast.makeText(ComputeTaskActivity.this,"網絡或服務器異常",EToast2.LENGTH_LONG).show();
                }
            }
        });
    }


    private void getUncompeleteInfo(String columName,String columValue){
        BmobQuery<TaskInfo> query = new BmobQuery<TaskInfo>();
        query.addWhereNotEqualTo(columName, columValue);
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(500);
        //执行查询方法
        query.findObjects(new FindListener<TaskInfo>() {
            @Override
            public void done(List<TaskInfo> list, BmobException e) {
                if (e==null){
                    unCompeletelist=list;
                    unCompelteTask=list.size();
                }else {
                    e.printStackTrace();
                    Toast.makeText(ComputeTaskActivity.this,"網絡或服務器異常",EToast2.LENGTH_LONG).show();
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
                convertView=layoutInflater.inflate(R.layout.item_task_info,null);
                holder=new ViewHolder();
                holder.tv_name=convertView.findViewById(R.id.tv_task_name);
                holder.tv_content=convertView.findViewById(R.id.tv_task_content);
                holder.tv_expect_date=convertView.findViewById(R.id.tv_task_expect_date);
                holder.tv_publish_date=convertView.findViewById(R.id.tv_task_publish_date);
                holder.tv_important=convertView.findViewById(R.id.tv_task_important);
                holder.tv_state=convertView.findViewById(R.id.tv_task_state);

                convertView.setTag(holder);
            }
            else {
                holder=(ViewHolder)convertView.getTag();
            }

            holder.tv_name.setText("任務名稱："+list.get(position).getTaskName());
            holder.tv_content.setText("任務內容："+list.get(position).getTaskContent());
            holder.tv_expect_date.setText("任務日期："+list.get(position).getTaskExpectDate());
            holder.tv_publish_date.setText("發佈時間："+list.get(position).getTaskPublishDate());
            holder.tv_important.setText("重要程度："+list.get(position).getImportantDegree());
            holder.tv_state.setText("任務狀態："+list.get(position).getTaskState());


            return convertView;
        }
    }

    public final class ViewHolder{
        public TextView tv_name,tv_content,tv_expect_date,tv_publish_date,tv_important,tv_state;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==11){
            if (Global.dateStr.length()>3){
                //getTaskInfo("taskPublishDate",Global.dateStr);
                getTaskInfo("taskSolvedDate",Global.dateStr);
            }else {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
                String nowDate = df.format(new Date());// new Date()为获取当前系统时间
                //getTaskInfo("taskPublishDate",nowDate);
                getTaskInfo("taskSolvedDate",nowDate);
            }

        }
    }
}
