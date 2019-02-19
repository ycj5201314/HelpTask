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
import android.widget.ListView;
import android.widget.TextView;

import com.frank.ycj.helpdesk.R;
import com.frank.ycj.helpdesk.config.Global;
import com.frank.ycj.helpdesk.entity.TaskInfo;
import com.frank.ycj.helpdesk.entity.UserInfo;
import com.mic.etoast2.EToast2;
import com.mic.etoast2.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class ComputePersonActivity extends AppCompatActivity {
    private ListView lv_trip_info;
    private MyAdapter myAdapter;
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");//设置日期格式
    String nowDate = df.format(new Date());// new Date()为获取当前系统时间
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compute_person);

        lv_trip_info=findViewById(R.id.lv_list_person);
        myAdapter=new MyAdapter(ComputePersonActivity.this);
        //Log.i("图片url", "onCreate: "+SecondHInfo.sencondHandInfo.getPicUrl());
        lv_trip_info.setAdapter(myAdapter);
        lv_trip_info.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Global.excuteMan=myAdapter.list.get(position).getUserName();
                Intent intent=new Intent(ComputePersonActivity.this,ComputeTaskActivity.class);
                intent.putExtra("tag","109");
                startActivity(intent);
            }
        });

        getTaskInfo("role","IT同事");

    }


    /**查询多条数据,查询的数据条数最多500**/
    private void getTaskInfo(String columName,String columValue){
        BmobQuery<UserInfo> query = new BmobQuery<UserInfo>();
        query.addWhereEqualTo(columName, columValue);
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(100);
        //执行查询方法
        query.findObjects(new FindListener<UserInfo>() {
            @Override
            public void done(List<UserInfo> list, BmobException e) {
                if (e==null){
                    myAdapter.list=list;
                    myAdapter.notifyDataSetChanged();
                }else {
                    e.printStackTrace();
                    Toast.makeText(ComputePersonActivity.this,"網絡或服務器異常",EToast2.LENGTH_LONG).show();
                }
            }
        });
    }

    private class MyAdapter extends BaseAdapter {

        private LayoutInflater layoutInflater;
        List<UserInfo> list=new ArrayList<>();

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

            holder.tv_name.setText("用戶名稱："+list.get(position).getUserName());
            holder.tv_content.setText("用戶角色："+list.get(position).getRole());
            holder.tv_expect_date.setText("統計日期："+nowDate);
            holder.tv_publish_date.setVisibility(View.GONE);
            holder.tv_important.setVisibility(View.GONE);
            holder.tv_state.setVisibility(View.GONE);


            return convertView;
        }
    }

    public final class ViewHolder{
        public TextView tv_name,tv_content,tv_expect_date,tv_publish_date,tv_important,tv_state;


    }

}
