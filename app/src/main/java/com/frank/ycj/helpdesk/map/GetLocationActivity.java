package com.frank.ycj.helpdesk.map;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.frank.ycj.helpdesk.R;
import com.mic.etoast2.EToast2;
import com.mic.etoast2.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class GetLocationActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static Context context;
    private static LocationManager m_locationManager;
    private static String m_provider;
    private static boolean isSuccess=false;

    private  LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                String string = "纬度为：" + location.getLatitude() + ",经度为：" + location.getLongitude();

                //getAddress(location);
                //getAddress(location.getLatitude(),location.getLongitude());
                if (isSuccess){
                    onActivityStoped();
                }else {
                    startLocation();
                }
                onActivityStoped();
            }
        }

        @Override
        public void onProviderDisabled(String arg0) {
        }

        @Override
        public void onProviderEnabled(String arg0) {
        }

        @Override
        public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
        }
    };

    public void startLocation() {

        //获取定位服务
        m_locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        //获取当前可用的位置控制器
        List<String> list = m_locationManager.getProviders(true);
        if (list.contains(LocationManager.GPS_PROVIDER)) {
            //是否为GPS位置控制器
            m_provider = LocationManager.NETWORK_PROVIDER;//NETWORK_PROVIDER GPS_PROVIDER

        } else if (list.contains(LocationManager.NETWORK_PROVIDER)) {
            //是否为网络位置控制器
            m_provider = LocationManager.NETWORK_PROVIDER;

        }
        if (m_provider != null) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location location = m_locationManager.getLastKnownLocation(m_provider);
            if(location!=null&&!isSuccess){
                //getAddress(location);//直接获取
                String address=getAddress(location.getLatitude(),location.getLongitude());//直接获取
               /* LocationInfo locationInfo = new LocationInfo();
                locationInfo.setUserName(PersonInfo.userInfo.getUserName());
                locationInfo.setAddr(address);
                locationInfo.setLatitue(location.getLatitude());
                locationInfo.setLongitude(location.getLongitude());
                locationInfo.setUploadTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                locationInfo.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if(e==null){
                            //添加成功
                            isSuccess=true;
                            //currentLocation.setText("current location is: "+entity.address);
                            com.mic.etoast2.Toast.makeText(GetLocationActivity.this,"Send successful",EToast2.LENGTH_LONG).show();
                            startActivity(new Intent(GetLocationActivity.this,HomeActivity.class));
                            onActivityStoped();
                            onStop();
                            finish();
                        }else{
                            //添加失败
                            e.printStackTrace();
                            com.mic.etoast2.Toast.makeText(GetLocationActivity.this,"server is error",EToast2.LENGTH_LONG).show();
                        }
                    }
                });*/

            }else{
                //没有需要加监听等待获取
                m_locationManager.requestLocationUpdates(m_provider, 300, 1, locationListener);
            }
        }
    }

    public  void onActivityStoped(){
        if(locationListener != null){
            m_locationManager.removeUpdates(locationListener);
            locationListener = null;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_location);

        context=GetLocationActivity.this;
        initPermission();
        startLocation();

       /* String serviceString = Context.LOCATION_SERVICE;// 获取的是位置服务
         m_locationManager = (LocationManager) getSystemService(serviceString);// 调用getSystemService()方法来获取LocationManager对象
        String provider = LocationManager.GPS_PROVIDER;// 指定LocationManager的定位方法
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);// 调用getLastKnownLocation()方法获取当前的位置信息
        double lat = location.getLatitude();//获取纬度
        double lng = location.getLongitude();//获取经度
        locationManager.requestLocationUpdates(provider, 2000, 10,locationListener);// 产生位置改变事件的条件设定为距离改变10米，时间间隔为2秒，设定监听位置变化
        */

    }


    public String getAddress(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude,
                    longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                /*String data = address.toString();
                int startCity = data.indexOf("1:\"") + "1:\"".length();
                int endCity = data.indexOf("\"", startCity);
                String city = data.substring(startCity, endCity);
                int startPlace = data.indexOf("feature=") + "feature=".length();
                int endplace = data.indexOf(",", startPlace);
                String place = data.substring(startPlace, endplace);*/
                String result="";

                if (address.getCountryName()!=null&&!address.getCountryName().equals("null")){
                    result=result+address.getCountryName()+" ";
                }
                if (address.getAdminArea()!=null&&!address.getAdminArea().equals("null")){
                    result=result+address.getAdminArea()+" ";
                }
                if (address.getLocality()!=null&&!address.getLocality().equals("null")){
                    result=result+address.getLocality()+" ";
                }
                if (address.getFeatureName()!=null&&!address.getFeatureName().equals("null")){
                    result=result + address.getFeatureName();
                }
                Log.i("currentlocation", "getAddress: "+result);
                return result ;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "获取失败";
    }


    private  String getAddress(Location location){
        //用来接收位置的详细信息
        List<Address> result = null;
        String addressLine = "";
        try {
            if (location != null) {
                Geocoder gc = new Geocoder(context, Locale.getDefault());
                result = gc.getFromLocation(location.getLatitude(),
                        location.getLongitude(), 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(result != null && result.get(0) != null){
            //这块获取到的是个数组我们取一个就好 下面是具体的方法查查API就能知道自己要什么
            addressLine=result.get(0).getCountryName()+"=="+result.get(0).getAddressLine(0);

            Log.i("addressLocation",addressLine);
            //Toast.makeText(mContext,result.get(0).toString(),Toast.LENGTH_LONG).show();
        }

        return addressLine;
    }

    public  void initPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //请求权限
                ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }else{
                startLocation();
            }
        }else{
            startLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        if (requestCode==101){
            startLocation();
        }
    }

}
