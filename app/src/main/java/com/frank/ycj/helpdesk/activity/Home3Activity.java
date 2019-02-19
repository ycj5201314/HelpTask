package com.frank.ycj.helpdesk.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.frank.ycj.helpdesk.R;
import com.frank.ycj.helpdesk.config.Global;
import com.frank.ycj.helpdesk.entity.TaskInfo;
import com.mic.etoast2.EToast2;
import com.mic.etoast2.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class Home3Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private int message=0;
    private TextView messageTip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home3);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        messageTip= findViewById(R.id.textView10);;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getTaskInfo("taskState","等待");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.home3, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent intent=new Intent(Home3Activity.this,ReceiVeTaskActivity.class);
            intent.putExtra("tag","101");
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            Intent intent=new Intent(Home3Activity.this,ReceiVeTaskActivity.class);
            intent.putExtra("tag","102");
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {
            startActivity(new Intent(Home3Activity.this,InstallDeviceActivity.class));
        }else if (id == R.id.nav_camera1) {
            Intent intent=new Intent(Home3Activity.this,ReceiVeTaskActivity.class);
            intent.putExtra("tag","103");
            startActivity(intent);
        } else if (id == R.id.nav_gallery1) {
            startActivity(new Intent(Home3Activity.this,ComputePersonActivity.class));
        } else if (id == R.id.nav_slideshow1) {
            Intent intent=new Intent(Home3Activity.this,ComputeTaskActivity.class);
            intent.putExtra("tag","108");
            startActivity(intent);
        } else if (id == R.id.nav_manage) {
            startActivity(new Intent(Home3Activity.this,PersonInfoActivity.class));
        } else if (id == R.id.nav_share) {
            startActivity(new Intent(Home3Activity.this,LoginActivity.class));
            finish();
        } else if (id == R.id.nav_send) {
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**查询多条数据,查询的数据条数最多500**/
    private void getTaskInfo(String columName,String columValue){
        BmobQuery<TaskInfo> query = new BmobQuery<TaskInfo>();
        query.addWhereEqualTo(columName, columValue);

        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(100);
        //执行查询方法
        query.findObjects(new FindListener<TaskInfo>() {
            @Override
            public void done(List<TaskInfo> list, BmobException e) {
                if (e==null){
                    List<TaskInfo> listImportant=new ArrayList<>();

                        for (TaskInfo taskInfo:list){
                            if (taskInfo.getImportantDegree().contains("特别任务")||taskInfo.getImportantDegree().contains("特別任務")||taskInfo.getImportantPosition().contains("經理")||taskInfo.getImportantPosition().contains("经理")){
                                listImportant.add(taskInfo);
                                message++;
                            }
                        }
                }else {
                    e.printStackTrace();
                    Toast.makeText(Home3Activity.this,"網絡或服務器異常",EToast2.LENGTH_LONG).show();
                }

                if (message>0){
                   messageTip.setText(messageTip.getText()+"\n\n\n您有待處理事項：\n\n重要通知任務："+message);
                }

            }
        });
    }
}
