package com.mobile.fuhome.app;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.EditText;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class NetService extends Service {

        int mStartMode; // indicates how to behave if the service is killed
        private final IBinder mBinder=new LocalBinder(); // interface for clients that bind
        boolean mAllowRebind; // indicates whether onRebind should be used

        private EditText E_name, E_psw;

    String s_reip;//目标IP
    String s_report;//目标端口
    String s_port;//本地端口

    DatagramSocket socket;
    char flag = 0;//1 已经打开
    char lo_flag = 0;//0，1失败，2登录
    char rem = 0;//记录密码

    byte[] getBuf = new byte[1024];//接收缓冲
    DatagramPacket getPacket = new DatagramPacket(getBuf, getBuf.length);//接收数据报
    DatagramPacket packet;//发送包
    InetAddress serverAddress;//服务器IP
    int port;//服务器端口

    String str_openid; //这是要传输的数据
    String str_userid; //这是要传输的数据
    String str_psw; //这是要传输的数据
    String str_ip;
    String str_port;


    public class  LocalBinder extends Binder{
        NetService getService(){
            return NetService.this;
        }

    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.i("Service", "onCreate~~~");
        //app读取userid psw

        ApplicationUtil appUtil =  (ApplicationUtil) NetService.this.getApplication();
        try {
            appUtil.init();
            socket = appUtil.Out_socket();
            str_openid =appUtil.Out_openid();
            str_userid =appUtil.Out_userid();
            str_psw=appUtil.Out_psw();
            str_ip=appUtil.Out_ip();
            str_port=appUtil.Out_port();

        } catch (Exception e1) {
            e1.printStackTrace();
        }


        // The service is being created

    }




    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        // The service is starting, due to a call to startService()
        Log.i("Service", "onStartCommand~~~");



        /* 定时任务 */
        new Thread() {
            @Override
            public void run() {
                // TODO Auto-generated method stub

                try {

                    while(true) {
                        //serverAddress = InetAddress.getByName("192.168.1.107");
                        serverAddress = InetAddress.getByName(str_ip);
                        port = Integer.parseInt(str_port);

                        //组合协议
                        byte[] data=new byte[100]; //把传输内容分解成字节
                        data[2]=2;
                        data[3]='B';
                        data[4]=2;

                        //userid
                       int temp =Integer.parseInt(str_userid);
                        for(int i = 0;i < 4;i++){
                            data[5+i] = (byte)(temp >> (24 - i * 8));
                        }
                        //密码
                       byte[] data_temp=str_psw.getBytes();
                        for(int i = 0;i < 32;i++){
                            data[9+i] = data_temp[i];
                        }
                        //open
                        data_temp=str_openid.getBytes();
                        for(int i = 0;i < 32;i++){
                            data[41+i] = data_temp[i];
                    }
                    data[73]=5;
                    data[0]=0;
                    data[1]=0x4a;


                    //创建一个DatagramPacket对象，并指定要讲这个数据包发送到网络当中的哪个、地址，以及端口号
                        DatagramPacket packet2 = new DatagramPacket(data, data[1], serverAddress, port);
                        socket.send(packet2);
                        Log.v("Service", "心跳包");
                        Thread.sleep(10000);// 线程暂停30秒，单位毫秒
                        //调用socket对象的send方法，发送数据
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }.start();




        return mStartMode;
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        // A client is binding to the service with bindService()
        Log.e("Service", "onBind~~~");
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent)
    {
        // All clients have unbound with unbindService()
        Log.e("Service", "onUnBind~~~");
        return mAllowRebind;
    }

    @Override
    public void onRebind(Intent intent)
    {
        // A client is binding to the service with bindService(),
        // after onUnbind() has already been called
        Log.e("Service", "onReBind~~~");
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        // The service is no longer used and is being destroyed
        Log.e("Service", "onDestroy~~~");
    }

    //发送命令函数
    public void Sendcommand(String com)
    {


    }


}
