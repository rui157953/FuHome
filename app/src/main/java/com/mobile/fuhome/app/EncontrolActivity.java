package com.mobile.fuhome.app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;


import android.view.KeyEvent;

import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;


public class EncontrolActivity extends Activity {


    private String str_userid,str_psw;
    private String sb_id,sb_name;
    private String ztn,ztl;
    private Button B_open,B_close,B_send,B_saveIP,B_clear;
    private TextView Tv_txt,Tv_ztl;
    private EditText E_com;
    private Button B_com1,B_com2,B_com3,B_com4,B_com5,B_com6,B_com7,B_com8,B_com9;
    private ButtonListener listener;

    public String username,id;

    public String[] com_name=new String[30];
    public String[] com_string=new String[30];

    //通过程序生成我们的ListView
    private ListView lv;
    public int Count_List=0;
    private int j,k;
    int keynum;

    DatagramSocket socket;

    private char flag = 0;//socket标志
    private char lo_flag = 0;//请求成功标志
    private char re_flag = 0;//接收

    byte[] getBuf = new byte[1024];//接收缓冲
    DatagramPacket getPacket = new DatagramPacket(getBuf, getBuf.length);//接收数据报

    //设备的socket

    InetAddress sbAddress,serverAddress;
    public String Sadd;
    int port;

    int i;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encontrol);

        Tv_txt = (TextView) findViewById(R.id.Tv_txt);
        Tv_ztl = (TextView) findViewById(R.id.Tv_ztl);
        E_com= (EditText) findViewById(R.id.E_com);

        B_saveIP = (Button) findViewById(R.id.B_saveIP);
        B_clear = (Button) findViewById(R.id.B_clear);
        B_send = (Button) findViewById(R.id.B_send);
        B_open = (Button) findViewById(R.id.B_open);
        B_close = (Button) findViewById(R.id.B_close);

        listener=new ButtonListener();
        B_com1 = (Button) findViewById(R.id.B_com1);
        B_com2 = (Button) findViewById(R.id.B_com2);
        B_com3 = (Button) findViewById(R.id.B_com3);
        B_com4 = (Button) findViewById(R.id.B_com4);
        B_com5 = (Button) findViewById(R.id.B_com5);
        B_com6 = (Button) findViewById(R.id.B_com6);
        B_com7 = (Button) findViewById(R.id.B_com7);
        B_com8 = (Button) findViewById(R.id.B_com8);
        B_com9 = (Button) findViewById(R.id.B_com9);

        B_com1.setOnClickListener(listener);
        B_com2.setOnClickListener(listener);
        B_com3.setOnClickListener(listener);
        B_com4.setOnClickListener(listener);
        B_com5.setOnClickListener(listener);
        B_com6.setOnClickListener(listener);
        B_com7.setOnClickListener(listener);
        B_com8.setOnClickListener(listener);
        B_com9.setOnClickListener(listener);


        //读取账户密码

        //取得启动该Activity的Intent对象
        Intent intent =getIntent();
        /*取出Intent中附加的数据*/
        //设备名称 设备ID
        sb_name = intent.getStringExtra("sb_name");
        sb_id = intent.getStringExtra("sb_id");
        setTitle(sb_name);

        //读取局域网IP
        SharedPreferences userInfo;
        userInfo = getSharedPreferences("login", 0);
        Sadd =userInfo.getString("IP"+sb_id,"");
        E_com.setText(Sadd);

        str_userid=userInfo.getString("userid","");
        // 保存IP：


        //app

        ApplicationUtil appUtil =  (ApplicationUtil) EncontrolActivity.this.getApplication();
        try {
            socket = appUtil.Out_socket();
            //str_name = appUtil.Out_name();
            //str_psw = appUtil.Out_psw();
            //读取IP
            serverAddress = InetAddress.getByName(Sadd);
            port = Integer.parseInt("7001");
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        //读取设备列表值
        //保存自定义菜单信息
        //SharedPreferences userInfo;
        userInfo = getSharedPreferences("sbcom"+sb_id, 0);
        String str=userInfo.getString("com_num", "");
        if(str.equals("") || str== null)
        {
        str="0";
        }
        int num=Integer.parseInt(str);

        Count_List=num;
        //此处是获取按键表的
        for(int i=0;i<num;i++) {
            com_name[i]=userInfo.getString("com_name" + i,"");
            com_string[i]=userInfo.getString("com_string" + i,"" );
        }

        /*  显示设备列表 */
        Creat_List();
        //进入循环接收
        re_flag=0;
        //接收线程
          /* */new Thread(){

            public  void run() {
                try {


                    while(re_flag==0) {

                        socket.receive(getPacket);
                        //符合协议
                        if(getBuf[2]==2&&getBuf[3]=='S') {
                            if (getBuf[4] == 0x0A) {
                                //状态量

                                //回复反馈
                                if (getBuf[13] == 'M') {

                                    //从14开始 总长度-15

                                    byte[] strby = new byte[100];

                                    for (int i = 0; i < (int) getBuf[1] - 15; i++) {
                                        strby[i] = getBuf[14 + i];
                                    }

                                    String res = new String(strby, "UTF-8");
                                    Log.v("反馈信息：", res);
                                    Message msg1 = Message.obtain();
                                    msg1.obj = res;
                                    msg1.what = 2;
                                    mHandler_re.sendMessage(msg1);
                                }
                            }
                        }



                        //清空接收缓冲区
                        for (int i = 0; i < getBuf.length; i++) {
                            getBuf[i] = 0;

                        }


                        Log.v(Integer.toString(lo_flag), "收到lo_flag");
                        Log.v(Integer.toString(re_flag), "收到re_flag");
                        // 关闭套接字
                        // socket.close();
                    }//flag=1的
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();
     //接收线程

     //saveIP按钮

        B_saveIP.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                //控制1
               /* */new Thread(){

                    public  void run() {
                        try {
                            Sadd=E_com.getText().toString();
                            // 保存IP：
                            serverAddress = InetAddress.getByName(Sadd);
                            //保存IP
                            SharedPreferences userInfoIP;
                            userInfoIP = getSharedPreferences("login", 0);
                            userInfoIP.edit().putString("IP"+sb_id,Sadd).commit();
                            Message msg1 = Message.obtain();
                            msg1.obj = "保存ok!";
                            msg1.what = 1;
                            mHandler_re.sendMessage(msg1);



                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                } .start();


            }
        });//saveIP按钮
        //clear
        B_clear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                //控制1
               /* */new Thread(){

                    public  void run() {
                        try {

                            Message msg2 = Message.obtain();
                            msg2.obj = "清空!";
                            msg2.what = 2;
                            mHandler_re.sendMessage(msg2);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                } .start();


            }
        });//clear按钮

//send按钮

        B_send.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                //控制1
               /* */new Thread(){

                    public  void run() {
                        try {

                            //组合协议2
                            byte[] data=new byte[120]; //把传输内容分解成字节
                            data[2]=2;
                            data[3]='S';
                            data[4]=8;

                            //userid
                            int temp =Integer.parseInt(str_userid);
                            for(int i = 0;i < 4;i++){
                                data[5+i] = (byte)(temp >> (24 - i * 8));
                            }

                            //sbid
                            temp =Integer.parseInt(sb_id);
                            for(int i = 0;i < 4;i++){
                                data[9+i] = (byte)(temp >> (24 - i * 8));
                            }
                            String str_com=E_com.getText().toString();
                            int str_com_length=str_com.length();
                            byte[] data_temp=str_com.getBytes();

                            for(int i = 0;i < str_com_length;i++){
                                data[13+i] = data_temp[i];
                            }
                            data[13+str_com_length]=5;
                            data[0]=0;
                            data[1]=(byte)(0x0e+str_com_length);


                            //创建一个DatagramPacket对象，并指定要讲这个数据包发送到网络当中的哪个、地址，以及端口号
                            DatagramPacket packet = new DatagramPacket(data, data[1], serverAddress, port);
                            //调用socket对象的send方法，发送数据
                            socket.send(packet);

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }.start();


            }
        });//send按钮


        //open按钮

        B_open.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                //控制1
               /* */new Thread(){

                    public  void run() {
                        try {

                            //组合协议2
                            byte[] data=new byte[120]; //把传输内容分解成字节
                            data[2]=2;
                            data[3]='S';
                            data[4]=8;

                            //userid
                            int temp =Integer.parseInt(str_userid);
                            for(int i = 0;i < 4;i++){
                                data[5+i] = (byte)(temp >> (24 - i * 8));
                            }

                            //sbid
                            temp =Integer.parseInt(sb_id);
                            for(int i = 0;i < 4;i++){
                                data[9+i] = (byte)(temp >> (24 - i * 8));
                            }
                            String str_com="open";
                            int str_com_length=str_com.length();
                            byte[] data_temp=str_com.getBytes();

                            for(int i = 0;i < str_com_length;i++){
                                data[13+i] = data_temp[i];
                            }
                            data[13+str_com_length]=5;
                            data[0]=0;
                            data[1]=(byte)(0x0e+str_com_length);


                            //创建一个DatagramPacket对象，并指定要讲这个数据包发送到网络当中的哪个、地址，以及端口号
                            DatagramPacket packet = new DatagramPacket(data, data[1], serverAddress, port);
                            //调用socket对象的send方法，发送数据
                            socket.send(packet);

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }.start();


            }
        });//open按钮


        //close
        B_close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                //控制1
               /* */new Thread(){

                    public  void run() {
                        try {


                            //组合协议2
                            byte[] data=new byte[120]; //把传输内容分解成字节
                            data[2]=2;
                            data[3]='S';
                            data[4]=8;

                            //userid
                            int temp =Integer.parseInt(str_userid);
                            for(int i = 0;i < 4;i++){
                                data[5+i] = (byte)(temp >> (24 - i * 8));
                            }

                            //sbid
                            temp =Integer.parseInt(sb_id);
                            for(int i = 0;i < 4;i++){
                                data[9+i] = (byte)(temp >> (24 - i * 8));
                            }
                            String str_com="close";
                            int str_com_length=str_com.length();
                            byte[] data_temp=str_com.getBytes();

                            for(int i = 0;i < str_com_length;i++){
                                data[13+i] = data_temp[i];
                            }
                            data[13+str_com_length]=5;
                            data[0]=0;
                            data[1]=(byte)(0x0e+str_com_length);


                            //创建一个DatagramPacket对象，并指定要讲这个数据包发送到网络当中的哪个、地址，以及端口号
                            DatagramPacket packet = new DatagramPacket(data, data[1], serverAddress, port);
                            //调用socket对象的send方法，发送数据
                            socket.send(packet);

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }.start();

            }
        });//close按钮


    }


    /*  显示设备列表 */
    void Creat_List()
    {
        if(Count_List>9)
            Count_List=9;


        for( int i = 0;i<Count_List;i++) {

            Log.v("" + com_name[i], "caidan_creat");
            if (i==0)
            {
                B_com1.setText(com_name[i]);
                B_com1.setVisibility(View.VISIBLE);
            }
            if (i==1)
            {
                B_com2.setText(com_name[i]);
                B_com2.setVisibility(View.VISIBLE);
            }
            if (i==2)
            {
                B_com3.setText(com_name[i]);
                B_com3.setVisibility(View.VISIBLE);
            }
            if (i==3)
            {
                B_com4.setText(com_name[i]);
                B_com4.setVisibility(View.VISIBLE);
            }
            if (i==4)
            {
                B_com5.setText(com_name[i]);
                B_com5.setVisibility(View.VISIBLE);
            }
            if (i==5)
            {
                B_com6.setText(com_name[i]);
                B_com6.setVisibility(View.VISIBLE);
            }
            if (i==6)
            {
                B_com7.setText(com_name[i]);
                B_com7.setVisibility(View.VISIBLE);
            }
            if (i==7)
            {
                B_com8.setText(com_name[i]);
                B_com8.setVisibility(View.VISIBLE);
            }
            if (i==8)
            {
                B_com9.setText(com_name[i]);
                B_com9.setVisibility(View.VISIBLE);
            }


            //map.put("ItemText", "命令:" + com_string[i]);
            // listItem.add(map);
        }

        // setTitle("你点击了第" + arg2 + "行");//设置标题栏显示点击的行




    }

    //button
    private class ButtonListener implements OnClickListener{
        public void onClick(View view){
            switch (view.getId()){
                case R.id.B_com1:
                    keynum=0;
                    sendout();
                    break;
                case R.id.B_com2:
                    keynum=1;
                    sendout();
                    break;
                case R.id.B_com3:
                    keynum=2;
                    sendout();
                    break;
                case R.id.B_com4:
                    keynum=3;
                    sendout();
                    break;
                case R.id.B_com5:
                    keynum=4;
                    sendout();
                    break;
                case R.id.B_com6:
                    keynum=5;
                    sendout();
                    break;
                case R.id.B_com7:
                    keynum=6;
                    sendout();
                    break;
                case R.id.B_com8:
                    keynum=7;
                    sendout();
                    break;
                case R.id.B_com9:
                    keynum=8;
                    sendout();
                    break;

            }
        }
    }

    private void sendout(){



        //控制1
               /* */new Thread(){

            public  void run() {
                try {



                    //组合协议2
                    byte[] data=new byte[120]; //把传输内容分解成字节
                    data[2]=2;
                    data[3]='S';
                    data[4]=8;

                    //userid
                    int temp =Integer.parseInt(str_userid);
                    for(int i = 0;i < 4;i++){
                        data[5+i] = (byte)(temp >> (24 - i * 8));
                    }

                    //sbid
                    temp =Integer.parseInt(sb_id);
                    for(int i = 0;i < 4;i++){
                        data[9+i] = (byte)(temp >> (24 - i * 8));
                    }
                    String str_com=com_string[keynum];
                    int str_com_length=str_com.length();
                    byte[] data_temp=str_com.getBytes();

                    for(int i = 0;i < str_com_length;i++){
                        data[13+i] = data_temp[i];
                    }
                    data[13+str_com_length]=5;
                    data[0]=0;
                    data[1]=(byte)(0x0e+str_com_length);


                    //创建一个DatagramPacket对象，并指定要讲这个数据包发送到网络当中的哪个、地址，以及端口号
                    DatagramPacket packet = new DatagramPacket(data, data[1], serverAddress, port);
                    //调用socket对象的send方法，发送数据
                    socket.send(packet);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();

        Message msg1 = Message.obtain();
        msg1.obj = com_string[keynum];
        msg1.what = 4;
        mHandler_re.sendMessage(msg1);
    }

    /* 处理接收到数据UI */
    Handler mHandler_re = new Handler() {
        // 注意：在各个case后面不能做太耗时的操作，否则出现ANR对话框
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1://显示传输的内容
                    String str=(String)msg.obj;
                    //tv_review.setText(str);
                    Toast.makeText(EncontrolActivity.this, str, Toast.LENGTH_SHORT).show();

                    // ed_reedit.setText(str0+str);
                    Log.v(str, "control_case1");
                    break;

                case 2:
                    String str1=(String)msg.obj;
                    E_com.setText("");
                    Log.v(str1, "control_case2");
                    break;

                case 3:
                    String str2=(String)msg.obj;
                    Tv_txt.setText(str2);
                    Log.v("ok", "control_case3");
                    break;
                case 4:
                    String str4=(String)msg.obj;
                    Toast.makeText(EncontrolActivity.this, "已发送:"+str4, Toast.LENGTH_SHORT).show();

                    break;
                case 5:
                    // String str5=(String)msg.obj;
                    Tv_ztl.setText(ztn+":"+ztl);

                    // Toast.makeText(ControlActivity.this, "已发送:"+str5, Toast.LENGTH_SHORT).show();
                    break;



            }
            super.handleMessage(msg);
        }
    };


    //后退按键
    public void onBackPressed() {
        re_flag=1;

        EncontrolActivity.this.finish();
        //code......
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.encontrol, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.about) {

           /* 新建一个Intent对象 */
            Intent intent0 = new Intent(this, AboutActivity.class);
            startActivity(intent0);
            // return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
