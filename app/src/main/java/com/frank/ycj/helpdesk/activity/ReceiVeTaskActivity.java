package com.frank.ycj.helpdesk.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
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

public class ReceiVeTaskActivity extends AppCompatActivity {

    private ListView lv_trip_info;
    private MyAdapter myAdapter;
    private String tag="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recei_ve_task);

        tag=getIntent().getStringExtra("tag");

        lv_trip_info=findViewById(R.id.lv_receive_task_info);
        myAdapter=new MyAdapter(ReceiVeTaskActivity.this);
        //Log.i("图片url", "onCreate: "+SecondHInfo.sencondHandInfo.getPicUrl());
        lv_trip_info.setAdapter(myAdapter);
        lv_trip_info.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TaskInfo taskInfo=myAdapter.list.get(position);
                String mess="任務發佈人："+taskInfo.getPublisherName()+
                        "\n發佈人職位："+taskInfo.getImportantPosition();
                new AlertDialog.Builder(ReceiVeTaskActivity.this).setTitle("任務信息")
                        .setMessage(mess)
                        .setPositiveButton("確定",null).show();
            }
        });

        if (tag.contains("101")){
            getTaskInfo("taskState","等待");
        }else if (tag.contains("102")){
            getTaskInfo("taskState","進行中,已完成");
        }else if (tag.contains("103")){
            getTaskInfo("taskState","等待");
        }

    }



    /**查询多条数据,查询的数据条数最多500**/
    private void getTaskInfo(String columName,String columValue){
        BmobQuery<TaskInfo> query = new BmobQuery<TaskInfo>();
        if (tag.contains("101")){
            query.addWhereEqualTo(columName, columValue);
        }else if (tag.contains("102")){
            String []keys=columValue.split(",");
            List<String> cValues=new ArrayList<>();
            for (int i=0;i<keys.length;i++){
                cValues.add(keys[i]);
            }
            query.addWhereContainedIn(columName, cValues);
            query.addWhereEqualTo("taskSolvedMan",Global.userInfo.getUserName());
        }else if (tag.contains("103")){
            query.addWhereEqualTo(columName, columValue);
            /*List<String> cValues=new ArrayList<>();
            cValues.add("特別任務");
            //cValues.add("重要人士");
            query.addWhereContainedIn("importantDegree",cValues);*/
           /* List<String> importantMan=new ArrayList<>();
            importantMan.add("經理");
            query.addWhereContainedIn("importantPosition",importantMan);*/

        }

        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(100);
        //执行查询方法
        query.findObjects(new FindListener<TaskInfo>() {
            @Override
            public void done(List<TaskInfo> list, BmobException e) {
                if (e==null){
                    List<TaskInfo> listImportant=new ArrayList<>();
                    if (tag.contains("103")){
                        for (TaskInfo taskInfo:list){
                            if (taskInfo.getImportantDegree().contains("特别任务")||taskInfo.getImportantDegree().contains("特別任務")||taskInfo.getImportantPosition().contains("經理")||taskInfo.getImportantPosition().contains("经理")){
                                listImportant.add(taskInfo);
                            }
                        }
                        myAdapter.list=listImportant;
                    }else {
                        myAdapter.list=list;
                    }

                    myAdapter.notifyDataSetChanged();
                }else {
                    e.printStackTrace();
                    Toast.makeText(ReceiVeTaskActivity.this,"網絡或服務器異常",EToast2.LENGTH_LONG).show();
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
            holder.tv_expect_date.setText("任務日期："+list.get(position).getTaskExpectDate());
            holder.tv_publish_date.setText("發佈時間："+list.get(position).getTaskPublishDate());
            holder.tv_important.setText("重要程度："+list.get(position).getImportantDegree());
            holder.tv_state.setText("任務狀態："+list.get(position).getTaskState());

            if (list.get(position).getTaskState().contains("已完成")){
                holder.btn_look.setEnabled(false);
                holder.btn_receive.setEnabled(false);
            }else {
                holder.btn_look.setEnabled(true);
                holder.btn_receive.setEnabled(true);
            }

            if (tag.contains("101")){
                holder.btn_look.setText("查看歷史任務");
                holder.btn_receive.setText("接受任務");
            }else if (tag.contains("102")){
                holder.btn_look.setText("置為初始任務");
                holder.btn_receive.setText("填寫解決方案");

            }else if (tag.contains("103")){
                holder.btn_look.setText("查看歷史任務");
                holder.btn_receive.setText("接受任務");
            }
            holder.btn_look.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tag.contains("101")){
                        Global.taskName=list.get(position).getTaskName();
                        startActivity(new Intent(ReceiVeTaskActivity.this,SimilarTaskActivity.class));
                    }else if (tag.contains("102")){
                        TaskInfo taskInfo=list.get(position);
                        taskInfo.setTaskState("等待");
                        taskInfo.setTaskSolvedMan("");
                        taskInfo.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e==null){
                                    list.remove(position);
                                    myAdapter.notifyDataSetChanged();
                                    Toast.makeText(ReceiVeTaskActivity.this,"更新成功",EToast2.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(ReceiVeTaskActivity.this,"更新失敗，請檢查網絡或者刷新數據",EToast2.LENGTH_LONG).show();
                                }
                            }
                        });
                    }else if (tag.contains("103")){
                        Global.taskName=list.get(position).getTaskName();
                        startActivity(new Intent(ReceiVeTaskActivity.this,SimilarTaskActivity.class));
                    }

                }
            });

            holder.btn_receive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tag.contains("101")){
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");//设置日期格式
                        String nowDate = df.format(new Date());// new Date()为获取当前系统时间
                        TaskInfo taskInfo=list.get(position);
                        taskInfo.setTaskState("進行中");
                        taskInfo.setTaskExecuteDate(nowDate);
                        taskInfo.setTaskSolvedMan(Global.userInfo.getUserName());
                        taskInfo.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e==null){
                                    list.remove(position);
                                    myAdapter.notifyDataSetChanged();
                                    Toast.makeText(ReceiVeTaskActivity.this,"接受成功",EToast2.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(ReceiVeTaskActivity.this,"接受失敗，請檢查網絡或者刷新數據",EToast2.LENGTH_LONG).show();
                                }
                            }
                        });
                    }else if (tag.contains("102")){
                        Global.taskInfo=list.get(position);
                        startActivityForResult(new Intent(ReceiVeTaskActivity.this,FillMethodActivity.class),13);
                    }else if (tag.contains("103")){
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");//设置日期格式
                        String nowDate = df.format(new Date());// new Date()为获取当前系统时间
                        TaskInfo taskInfo=list.get(position);
                        taskInfo.setTaskState("進行中");
                        taskInfo.setTaskExecuteDate(nowDate);
                        taskInfo.setTaskSolvedMan(Global.userInfo.getUserName());
                        taskInfo.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e==null){
                                    list.remove(position);
                                    myAdapter.notifyDataSetChanged();
                                    Toast.makeText(ReceiVeTaskActivity.this,"接受成功",EToast2.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(ReceiVeTaskActivity.this,"接受失敗，請檢查網絡或者刷新數據",EToast2.LENGTH_LONG).show();
                                }
                            }
                        });
                    }

                }
            });

            return convertView;
        }
    }

    public final class ViewHolder{
        public TextView tv_name,tv_content,tv_expect_date,tv_publish_date,tv_important,tv_state;
        public Button btn_look,btn_receive;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==13){
            if (tag.contains("101")){
                getTaskInfo("taskState","等待");
            }else if (tag.contains("102")){
                getTaskInfo("taskState","進行中,已完成");
            }else if (tag.contains("103")){
                getTaskInfo("taskState","等待");
            }
        }
    }
}
