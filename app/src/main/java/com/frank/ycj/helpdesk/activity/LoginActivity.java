package com.frank.ycj.helpdesk.activity;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.frank.ycj.helpdesk.R;
import com.frank.ycj.helpdesk.config.Global;
import com.frank.ycj.helpdesk.entity.UserInfo;
import com.frank.ycj.helpdesk.utils.SharePreferenceSave;
import com.mic.etoast2.EToast2;
import com.mic.etoast2.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;


import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin,btnRegister;
    private EditText username,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().
                detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().
                detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());

        btnLogin=findViewById(R.id.button2);
        btnRegister=findViewById(R.id.button3);
        username=findViewById(R.id.editText);
        password=findViewById(R.id.editText2);


        String myString = "2029-02-01";
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String nowDate = df.format(new Date());// new Date()为获取当前系统时间
        Date d = null,nowd=null;
        try {
            d = df.parse(myString);
            nowd=df.parse(nowDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        boolean flag = nowd.before(d);
        if(flag){

        }
        else{
            Toast.makeText(LoginActivity.this,"使用時間已到期",EToast2.LENGTH_LONG).show();
            finish();
            System.exit(0);
        }


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnLogin.setEnabled(false);
                checkUserInfo("userName",username.getText().toString());
            }
        });

        Map<String, String> userInfo= SharePreferenceSave.getUserInfo ( this );
        if (userInfo!=null){
            username.setText ( userInfo.get ( "username" ) );
            password.setText ( userInfo.get ( "password" ) );
        }
    }

    private void updatePassword(final String userP){
        Global.userInfo.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    try {
                        Log.i("邮箱：",Global.userInfo.getEmail());
                        sendEmail(Global.userInfo.getEmail(),"重置密碼郵件!", "您的登錄密碼為："+userP);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }else {
                    e.printStackTrace();
                }
            }
        });
    }

    /**查询多条数据,查询的数据条数最多500**/
    private void checkUserInfo(String columName,String columValue){
        if (Global.errorSecond>5){
            Random random=new Random();
            String userP=Global.userInfo.getUserName()+random.nextInt(1000);
            Global.userInfo.setError(Global.errorSecond);
            Global.userInfo.setPassWord(userP);
            updatePassword(userP);
            Toast.makeText(LoginActivity.this,"密碼錯誤5次，重置密碼已發到公司電郵，請檢查再登錄\n",EToast2.LENGTH_LONG).show();
            return;
        }
        BmobQuery<UserInfo> query = new BmobQuery<UserInfo>();
        query.addWhereEqualTo(columName, columValue);
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(5);
        //执行查询方法
        query.findObjects(new FindListener<UserInfo>() {
            @Override
            public void done(List<UserInfo> object, BmobException e) {
                if(e==null){
                    if (object.size()>0){
                        UserInfo userInfo=object.get(0);
                        Global.userInfo=userInfo;
                        if (userInfo.getPassWord().equals(password.getText().toString().trim())){
                            boolean isSaveSucess = SharePreferenceSave.saveUserInfo (LoginActivity.this, username.getText().toString().trim(),password.getText().toString().trim() );
                            Toast.makeText(LoginActivity.this,"登錄成功",EToast2.LENGTH_LONG).show();
                            if (userInfo.getRole().contains("普通用戶")){
                                startActivity(new Intent(LoginActivity.this,Home1Activity.class));
                            }else if (userInfo.getRole().contains("IT同事")){
                                startActivity(new Intent(LoginActivity.this,Home2Activity.class));
                            }else if (userInfo.getRole().contains("IT經理")){
                                startActivity(new Intent(LoginActivity.this,Home3Activity.class));
                            }

                            //startActivity(new Intent(LoginActivity.this,GetLocationActivity.class));
                            finish();
                        }else {
                            Global.errorSecond++;
                            btnLogin.setEnabled(true);
                            Toast.makeText(LoginActivity.this,"密碼錯誤",EToast2.LENGTH_LONG).show();
                        }
                    }else {

                        btnLogin.setEnabled(true);
                        System.out.println("用户名不存在");
                        Toast.makeText(LoginActivity.this,"用戶不存在",EToast2.LENGTH_LONG).show();
                    }
                }else{
                    btnLogin.setEnabled(true);
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this,"網絡或服務器異常",EToast2.LENGTH_LONG).show();
                }
            }
        });
    }


    /**
     * 邮件发送程序
     *
     * @param to
     *            接受人
     * @param subject
     *            邮件主题
     * @param content
     *            邮件内容
     * @throws Exception
     * @throws MessagingException
     */
    public void sendEmail(String to, String subject, String content) throws Exception, MessagingException {
        String host = "smtp.qq.com";
        String address = "1320259466@qq.com";
        String from = "1320259466@qq.com";
        String password = "fcucszzgvpodhiff";
        if ("".equals(to) || to == null) {
            to = "1272275196@qq.com";
        }
        String port = "25";
        SendEmaill(host, address, from, password, to, port, subject, content);
    }

    /**
     * 邮件发送程序
     *
     * @param host
     *            邮件服务器 如：smtp.qq.com
     * @param address
     *            发送邮件的地址 如：545099227@qq.com
     * @param from
     *            来自： wsx2miao@qq.com
     * @param password
     *            您的邮箱密码
     * @param to
     *            接收人
     * @param port
     *            端口（QQ:25）
     * @param subject
     *            邮件主题
     * @param content
     *            邮件内容
     * @throws Exception
     */
    public void SendEmaill(String host, String address, String from, String password, String to, String port, String subject, String content) throws Exception {
        Multipart multiPart;
        String finalString = "";

        Properties props = System.getProperties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", address);
        props.put("mail.smtp.password", password);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", "true");
        Log.i("Check", "done pops");
        Session session = Session.getDefaultInstance(props, null);
        DataHandler handler = new DataHandler(new ByteArrayDataSource(finalString.getBytes(), "text/plain"));
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setDataHandler(handler);
        Log.i("Check", "done sessions");

        multiPart = new MimeMultipart();
        InternetAddress toAddress;
        toAddress = new InternetAddress(to);
        message.addRecipient(Message.RecipientType.TO, toAddress);
        Log.i("Check", "added recipient");
        message.setSubject(subject);
        message.setContent(multiPart);
        message.setText(content);

        Log.i("check", "transport");
        Transport transport = session.getTransport("smtp");
        Log.i("check", "connecting");
        transport.connect(host, address, password);
        Log.i("check", "wana send");
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
        Log.i("check", "sent");

    }




}
