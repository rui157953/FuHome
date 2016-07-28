package com.mobile.fuhome.app;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import java.util.Map;
import java.util.ResourceBundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.text.format.Time;
import android.view.KeyEvent;

import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import java.text.SimpleDateFormat;
import  java.util.Date;
public class ControlActivity extends Activity {

    private String str_openid,str_userid, str_psw;
    private String id,sb_id,sb_name;
    private String ztn,ztl;
    private Button B_open,B_close,B_send;
    private TextView Tv_txt,Tv_ztl;
    private LinearLayout L_feel1,L_feel2,L_feel3;
    private TextView Tv_feel1_name,Tv_feel1_value,Tv_feel1_time;
    private TextView Tv_feel2_name,Tv_feel2_value,Tv_feel2_time;
    private TextView Tv_feel3_name,Tv_feel3_value,Tv_feel3_time;
    private EditText E_com;
    private Button B_com1,B_com2,B_com3,B_com4,B_com5,B_com6,B_com7,B_com8,B_com9;
    private ButtonListener listener;
    private ButtonLongListener longlistener;

    public String[] com_name=new String[30];
    public String[] com_string=new String[30];
    public String[] feel_num=new String[30];
    public String[] feel_name=new String[30];
    public String[] feel_danwei=new String[30];
    public String[] feel_value=new String[30];
    public String[] feel_time=new String[30];
    public String feel_zhi;
    public int feel_nums;
    //通过程序生成我们的ListView
    private ListView lv;

    //通过程序生成我们的ListView
    public int Count_List=0;
    public int Feel_List=0;
    int keynum;

    DatagramSocket socket;

    byte data[];
    DatagramPacket packet;
    byte[] getBuf = new byte[1024];//接收缓冲
    DatagramPacket getPacket = new DatagramPacket(getBuf, getBuf.length);//接收数据报

    private char flag = 0;//socket标志
    private char lo_flag = 0;//自定义按键请求成功标志
    private char feel_flag = 0;//feel传感器请求成功标志
    private char re_flag = 0;//接收



    //设备的socket

    String str_ip;
    InetAddress sbAddress,serverAddress;
    int port;
    String str_port;

    private int zaici=0;//再次进入的标志 数值
    private int zaici1=0;//再次进入的标志 传感器和菜单
    int i;

    private NotificationManager manager;
    private Notification notification;
    private Intent intent;
    private PendingIntent contentIntent;
    private NotificationManager nm;
    private Notification n;

    private Handler Handler;
    /* 列表 */
    private NetService binderService;
    char appflag=0;//0需要重新初始化
    SimpleAdapter mSimpleAdapter;
    ArrayList<HashMap<String, Object>> listItem;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        Log.e("", "start onCreate~~~");
        Tv_txt = (TextView) findViewById(R.id.Tv_txt);
        Tv_ztl = (TextView) findViewById(R.id.Tv_ztl);
        E_com= (EditText) findViewById(R.id.E_com);
        B_send = (Button) findViewById(R.id.B_send);
        B_open = (Button) findViewById(R.id.B_open);
        B_close = (Button) findViewById(R.id.B_close);

        lv = (ListView) findViewById(R.id.feel_list);//得到ListView对象的引用 /*为ListView设置Adapter来绑定数据*/


      /*  L_feel1=(LinearLayout) findViewById(R.id.L_feel1);
        L_feel2=(LinearLayout) findViewById(R.id.L_feel2);
        L_feel3=(LinearLayout) findViewById(R.id.L_feel3);

        Tv_feel1_name = (TextView) findViewById(R.id.Tv_feel1_name);
        Tv_feel1_value = (TextView) findViewById(R.id.Tv_feel1_value);
        Tv_feel1_time = (TextView) findViewById(R.id.Tv_feel1_time);
        Tv_feel2_name = (TextView) findViewById(R.id.Tv_feel2_name);
        Tv_feel2_value = (TextView) findViewById(R.id.Tv_feel2_value);
        Tv_feel2_time = (TextView) findViewById(R.id.Tv_feel2_time);
        Tv_feel3_name = (TextView) findViewById(R.id.Tv_feel3_name);
        Tv_feel3_value = (TextView) findViewById(R.id.Tv_feel3_value);
        Tv_feel3_time = (TextView) findViewById(R.id.Tv_feel3_time);

*/
        listener=new ButtonListener();
        longlistener=new ButtonLongListener();
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

        B_com1.setOnLongClickListener(longlistener);
        B_com2.setOnLongClickListener(longlistener);
        B_com3.setOnLongClickListener(longlistener);
        B_com4.setOnLongClickListener(longlistener);
        B_com5.setOnLongClickListener(longlistener);
        B_com6.setOnLongClickListener(longlistener);
        B_com7.setOnLongClickListener(longlistener);
        B_com8.setOnLongClickListener(longlistener);
        B_com9.setOnLongClickListener(longlistener);



        //读取账户密码

        //取得启动该Activity的Intent对象
        Intent intent =getIntent();
        /*取出Intent中附加的数据*/

        ztn = intent.getStringExtra("sb_staname");
        ztl = intent.getStringExtra("sb_stavalue");
        sb_name = intent.getStringExtra("sb_name");
        sb_id = intent.getStringExtra("sb_id");
        id = intent.getStringExtra("id");


       // SharedPreferences userInfo;
       // userInfo = getSharedPreferences("Control", 0);
       // sb_name= userInfo.getString("sb_name","");
       // sb_id=userInfo.getString("sb_id","");
      //  id=userInfo.getString("id","");
        setTitle(sb_name);
        //4、手机通知服务器要对指定设备打洞

        //f=4&d=[10]&n=[n]&from=mobile->server


        //app

      ApplicationUtil appUtil =  (ApplicationUtil) ControlActivity.this.getApplication();
        try {
            socket=appUtil.Out_socket();
            str_openid =appUtil.Out_openid();
            str_userid =appUtil.Out_userid();
            str_psw=appUtil.Out_psw();
            str_ip=appUtil.Out_ip();
            str_port=appUtil.Out_port();
        } catch (Exception e1) {
            e1.printStackTrace();
        }


      //------------------------------------------------------------------------

        re_flag=0;
         /* 加载菜单和传感器列表 */
        new Thread() {
            @Override
            public void run() {
                // TODO Auto-generated method stub

                try {

                    if (lo_flag == 0) {
                        Log.v("sblist_进入http", "try");

                        //建立一个NameValuePair数组，用于存储欲传递的参数
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("openid", str_openid);
                        params.put("userid", str_userid);
                        params.put("psw", str_psw);
                        params.put("f", "6");
                        params.put("id", id);
                        //服务器请求路径
                        String strUrlPath = "http://fuhome.net/api/sblist/";
                        String strResult = HttpUtil.submitPostData(strUrlPath, params, "utf-8");

                        Log.v("sbcontrol_http_post", strResult.replaceAll(" ", ""));

                        JSONObject obj = new JSONObject(strResult);
                        String dis = obj.getString("dis");
                        String log = obj.getString("log");
                        //String userid = obj.getString("userid");

                        Log.v("dis", dis);
                        Log.v("log", log);

                        if (log.equals("ok")) {
                            Log.v("login", "right");
                            lo_flag = 1;
                                /* 生成菜单列表 */
                            parseJson(strResult);

                            //显示菜单列表
                            Message msg1 = Message.obtain();
                            msg1.what = 3;
                            mHandler_re.sendMessage(msg1);
                            Log.v("sblist_creat", "设备列表");


                        } else {

                            lo_flag = 1;
                            Log.v("login", "wrong");
                        }

                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();



        //接收UDP数据线程
          /* */new Thread(){

            public  void run() {
                try {


                    while(re_flag==0) {

                        socket.receive(getPacket);
                        Log.v("Control_udp_Rx", "收到信息:"+byte2HexStr(getBuf));

                        //符合协议
                        if(getBuf[2]==2&&getBuf[3]=='S')
                        {
                            if(getBuf[4]==0x0A) {
                                //状态量
                                if (getBuf[13] == 'S') {
                                    //从14开始 总长度-15

                                    byte[] strby = new byte[100];

                                    for(int i=0;i<(int)getBuf[1]-15;i++)
                                    {
                                        strby[i]=getBuf[14+i];
                                    }

                                    String res = new String(strby,"UTF-8");
                                    Log.v("状态量：",res);
                                    ztl=res;
                                    Message msg1 = Message.obtain();
                                    msg1.what = 5;
                                    msg1.obj = res;
                                    mHandler_re.sendMessage(msg1);
                                    Log.v("ztl_creat", "" + lo_flag);
                                }
                                //回复反馈
                                else if (getBuf[13] == 'M') {

                                    //从14开始 总长度-15

                                    byte[] strby = new byte[100];

                                    for(int i=0;i<(int)getBuf[1]-15;i++)
                                    {
                                        strby[i]=getBuf[14+i];
                                    }

                                    String res = new String(strby,"UTF-8");
                                    Log.v("反馈信息：",res);
                                    Message msg1 = Message.obtain();
                                    msg1.obj = res;
                                    msg1.what = 2;
                                    mHandler_re.sendMessage(msg1);
                                }
                            }
                            //接到传感器值
                            else if(getBuf[4]==0x0C) {
                                Log.v("com", "手机收到传感器值");
                                //传感器转发到手机

                                //找到num
                                int jj_num = (int) getBuf[13] * 256 + (int) getBuf[14];
                                String str_num = Integer.toString(jj_num);

                                int jj_aa = (int) getBuf[16];//找到aa后面的
                                byte[] aa = new byte[10];
                                for (int i = 0; i < jj_aa; i++)
                                {
                                    aa[i]=getBuf[17+i];
                                }
                                String str_aa=new String(aa,"UTF-8");;;

                                for(int i=0;i<Feel_List;i++) {
                                    Log.v(""+i,"feel_num[s]");
                                    if (str_num.equals(feel_num[i])) {
                                        Log.v(""+i,"feel_num[]");
                                        feel_nums=i;//用于标号
                                        feel_zhi=str_aa;//值
                                        Message msg1 = Message.obtain();
                                        msg1.obj = str_num;
                                        msg1.what = 7;
                                        mHandler_re.sendMessage(msg1);
                                    }
                                }


                            }


                        }

                        //报警信息API

                    //清空接收缓冲区
                        for (int i = 0; i < getBuf.length; i++) {
                            getBuf[i] = 0;

                        }


                        Log.v(Integer.toString(lo_flag), "收到lo_flag");
                        Log.v(Integer.toString(feel_flag), "收到feel_flag");
                        Log.v(Integer.toString(re_flag), "收到re_flag");
                        // 关闭套接字
                        // socket.close();
                    }//flag=1的
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start(); //接收UDP数据线程


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
                            data[3]='B';
                            data[4]=6;

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
                            //openid
                            data_temp=str_openid.getBytes();
                            for(int i = 0;i < 32;i++){
                                data[41+i] = data_temp[i];
                            }
                            //sbid
                            temp =Integer.parseInt(sb_id);
                            for(int i = 0;i < 4;i++){
                                data[73+i] = (byte)(temp >> (24 - i * 8));
                            }
                            String str_com=E_com.getText().toString();
                            int str_com_length=str_com.length();
                            data_temp=str_com.getBytes();

                            for(int i = 0;i < str_com_length;i++){
                                data[77+i] = data_temp[i];
                            }
                            data[77+str_com_length]=5;
                            data[0]=0;
                            data[1]=(byte)(0x4e+str_com_length);


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
               /* */
                new Thread() {

                    public void run() {
                        try {

                            serverAddress = InetAddress.getByName(str_ip);
                            port = Integer.parseInt(str_port);

                            //组合协议2
                            byte[] data = new byte[120]; //把传输内容分解成字节
                            data[2] = 2;
                            data[3] = 'B';
                            data[4] = 6;

                            //userid
                            int temp = Integer.parseInt(str_userid);
                            for (int i = 0; i < 4; i++) {
                                data[5 + i] = (byte) (temp >> (24 - i * 8));
                            }
                            //密码
                            byte[] data_temp = str_psw.getBytes();
                            for (int i = 0; i < 32; i++) {
                                data[9 + i] = data_temp[i];
                            }
                            //openid
                            data_temp = str_openid.getBytes();
                            for (int i = 0; i < 32; i++) {
                                data[41 + i] = data_temp[i];
                            }
                            //sbid
                            temp = Integer.parseInt(sb_id);
                            for (int i = 0; i < 4; i++) {
                                data[73 + i] = (byte) (temp >> (24 - i * 8));
                            }
                            String str_com = "open";
                            int str_com_length = str_com.length();
                            data_temp = str_com.getBytes();

                            for (int i = 0; i < str_com_length; i++) {
                                data[77 + i] = data_temp[i];
                            }
                            data[77 + str_com_length] = 5;
                            data[0] = 0;
                            data[1] = (byte) (0x4e + str_com_length);

                            //创建一个DatagramPacket对象，并指定要讲这个数据包发送到网络当中的哪个、地址，以及端口号
                            packet = new DatagramPacket(data, data[1], serverAddress, port);
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

                            serverAddress = InetAddress.getByName(str_ip);
                            port = Integer.parseInt(str_port);

                            //组合协议2
                            byte[] data=new byte[120]; //把传输内容分解成字节
                            data[2]=2;
                            data[3]='B';
                            data[4]=6;

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
                            //openid
                            data_temp=str_openid.getBytes();
                            for(int i = 0;i < 32;i++){
                                data[41+i] = data_temp[i];
                            }
                            //sbid
                            temp =Integer.parseInt(sb_id);
                            for(int i = 0;i < 4;i++){
                                data[73+i] = (byte)(temp >> (24 - i * 8));
                            }
                            String str_com="close";
                            int str_com_length=str_com.length();
                            data_temp=str_com.getBytes();

                            for(int i = 0;i < str_com_length;i++){
                                data[77+i] = data_temp[i];
                            }
                            data[77+str_com_length]=5;
                            data[0]=0;
                            data[1]=(byte)(0x4e+str_com_length);


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


    protected void onStart()
        {
            Log.e("Control", "start onStart~~~");
            super.onStart();

    }
    //加载动态页面
    protected void onRestart() {
        super.onRestart();
        zaici=0;
        Log.e("Control", "start onRestart~~~");
    }
    @Override
    protected void onResume() {

        super.onResume();
        Log.e("Control", "start onResume~~~");
        //3、查询设备传感器列表
        if(zaici==1) {
            zaici=0;
            new Thread() {

                public void run() {
                    try {
                        Thread.sleep(100);// 线程暂停1秒，单位毫秒
                        Log.v("ok", "加载存储的传感器参数进行显示");
                        Message msg1 = Message.obtain();
                        msg1.obj = "value";
                        msg1.what = 8;
                        mHandler_re.sendMessage(msg1);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }.start();

            /*Handler.post(new Runnable() {
                @Override
                public void run() {
                    try {

                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });*/


        }



    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.e("Control", "start onPause~~~");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.e("Control", "start onStop~~~");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("Control", "start onDestroy~~~");
        lo_flag=1;
        feel_flag=1;
        re_flag=1;
    }

    //操作可是界面用的



     /* 分拆返回列表数据 */

    //解析状态量
    private void ztl_Json(String strResult) {

        try {

            if(strResult != null && strResult.startsWith("\ufeff"))
            {
                strResult =  strResult.substring(1);
            }
            //JSONObject json = new JSONObject(strResult);

            JSONObject jsonObj = new JSONObject(strResult);
            Count_List = jsonObj.getInt("c");
            ztn=jsonObj.getString("ztn");
            ztl=jsonObj.getString("ztl");
            Log.v(""+Count_List,"ztl_count");


        } catch (JSONException e) {

            System.out.println("Json parse error");

            e.printStackTrace();

        }

    }

    // 普通Json数据解析-分析自定义菜单
    private void parseJson(String strResult) {

        try {

            if(strResult != null && strResult.startsWith("\ufeff"))
            {
                strResult =  strResult.substring(1);
            }



            JSONObject jsonObj = new JSONObject(strResult);
            Count_List = jsonObj.getInt("com");
            JSONArray jsonObjs = new JSONObject(strResult).getJSONArray("sbcom");
            int num=jsonObjs.length();

            //保存自定义菜单信息
            SharedPreferences userInfo;
            userInfo = getSharedPreferences("sbcom"+sb_id, 0);
            userInfo.edit().putString("com_num", ""+num).commit();

            for(int i = 0; i <num ;i++) {

                JSONObject jsonObja = ((JSONObject) jsonObjs.opt(i)) ;

                com_name[i] = jsonObja.getString("comname");
                com_string[i] = jsonObja.getString("comstring");

                //此处是存储按键表的
                userInfo.edit().putString("com_name"+i, com_name[i]).commit();
                userInfo.edit().putString("com_string"+i,com_string[i]).commit();


            }
            Log.v("Menu_list","caidan_count");

            if(strResult != null && strResult.startsWith("\ufeff"))
            {
                strResult =  strResult.substring(1);
            }

            Feel_List = jsonObj.getInt("feel");
            jsonObjs = new JSONObject(strResult).getJSONArray("sbfeel");
            num=jsonObjs.length();

            //保存传感器信息
            //SharedPreferences userInfo;
            userInfo = getSharedPreferences("sbfeel"+sb_id, 0);
            userInfo.edit().putString("feel_count", ""+num).commit();

            for(int i = 0; i <num ;i++) {

                JSONObject jsonObja = ((JSONObject) jsonObjs.opt(i)) ;
                feel_num[i] = jsonObja.getString("feelnum");
                feel_name[i] = jsonObja.getString("feelname");
                feel_danwei[i] = jsonObja.getString("feelunit");

                //此处是存储传感器表
                userInfo.edit().putString("feel_num"+i, feel_num[i]).commit();
                userInfo.edit().putString("feel_name"+i, feel_name[i]).commit();
                userInfo.edit().putString("feel_danwei"+i,feel_danwei[i]).commit();

            }




        } catch (JSONException e) {

            System.out.println("Json parse error");

            e.printStackTrace();

        }

    }
    /*  显示自定义菜单列表 */
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


    /*  显示自定义菜单列表 */
    void Creat_feel_Lists()
    {
        if(Feel_List>3)
            Feel_List=3;


        for( int i = 0;i<Feel_List;i++) {

            Log.v("" + com_name[i], "caidan_creat");
            if (i==0)
            {
                Tv_feel1_name.setText(feel_name[i]+":"+feel_danwei[i]);
                Tv_feel1_value.setText("");
                Tv_feel1_time.setText("");
                L_feel1.setVisibility(View.VISIBLE);
            }
            if (i==1)
            {
                Tv_feel2_name.setText(feel_name[i]+":"+feel_danwei[i]);
                Tv_feel2_value.setText("");
                Tv_feel2_time.setText("");
                L_feel2.setVisibility(View.VISIBLE);
            }
            if (i==2)
            {
                Tv_feel3_name.setText(feel_name[i]+":"+feel_danwei[i]);
                Tv_feel3_value.setText("");
                Tv_feel3_time.setText("");
                L_feel3.setVisibility(View.VISIBLE);
            }


            //map.put("ItemText", "命令:" + com_string[i]);
            // listItem.add(map);
        }

        // setTitle("你点击了第" + arg2 + "行");//设置标题栏显示点击的行




    }

    /*  显示设备列表 */
    void Creat_feel_List() {


        listItem = new ArrayList<HashMap<String, Object>>();/*在数组中存放数据*/
        for (int i = 0; i <Feel_List; i++)

        {
            HashMap<String, Object> map = new HashMap<String, Object>();
           // map.put("ItemImage", R.drawable.w1);//加入图片
            map.put("ItemTitle",  feel_name[i]+":"+feel_danwei[i]);
            map.put("ItemValue", "");
            map.put("ItemTime","");
            listItem.add(map);
        }

        mSimpleAdapter = new SimpleAdapter(this, listItem,//需要绑定的数据
                R.layout.item_feel,//每一行的布局//动态数组中的数据源的键对应到定义布局的View中
                new String[]{"ItemTitle", "ItemValue","ItemTime"}, new int[]{R.id.ItemTitle, R.id.ItemValue,R.id.ItemTime}
        );

        lv.setAdapter(mSimpleAdapter);
        //为ListView绑定适配器
        lv.setOnItemClickListener(new

                                          AdapterView.OnItemClickListener() {

                                              @Override
                                              public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {



                                              }
                                          }
        );
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
    private class ButtonLongListener implements OnLongClickListener{
        public boolean onLongClick(View view){

            /*switch (view.getId()){
                case R.id.bt_key1:
                    keynum=1;
                    dilogout();//弹出对话框
                    break;
                case R.id.bt_key2:
                    keynum=2;
                    dilogout();//弹出对话框
                    break;
                case R.id.bt_key3:
                    keynum=3;
                    dilogout();//弹出对话框
                    break;
                case R.id.bt_key4:
                    keynum=4;
                    dilogout();//弹出对话框
                    break;
                case R.id.bt_key5:
                    keynum=5;
                    dilogout();//弹出对话框
                    break;
                case R.id.bt_key6:
                    keynum=6;
                    dilogout();//弹出对话框
                    break;
                case R.id.bt_key7:
                    keynum=7;
                    dilogout();//弹出对话框
                    break;
                case R.id.bt_key8:
                    keynum=8;
                    dilogout();//弹出对话框
                    break;
                case R.id.bt_key9:
                    keynum=9;
                    dilogout();//弹出对话框
                    break;

            }*/
            return true;
        }
    }
    private void sendout(){



        //控制1
               /* */new Thread(){

            public  void run() {
                try {

                    serverAddress = InetAddress.getByName(str_ip);
                    port = Integer.parseInt(str_port);

                    //组合协议2
                    byte[] data=new byte[120]; //把传输内容分解成字节
                    data[2]=2;
                    data[3]='B';
                    data[4]=6;

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
                    //openid
                    data_temp=str_openid.getBytes();
                    for(int i = 0;i < 32;i++){
                        data[41+i] = data_temp[i];
                    }
                    //sbid
                    temp =Integer.parseInt(sb_id);
                    for(int i = 0;i < 4;i++){
                        data[73+i] = (byte)(temp >> (24 - i * 8));
                    }
                    String str_com=com_string[keynum];

                    int str_com_length=str_com.length();
                    Log.v("control",str_com+"..."+str_com_length);
                    data_temp=str_com.getBytes("UTF-8");

                    for(int i = 0;i < str_com_length;i++){
                        data[77+i] = data_temp[i];
                    }
                    data[77+str_com_length]=5;
                    data[0]=0;
                    data[1]=(byte)(0x4e+str_com_length);

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


    public static String byte2HexStr(byte[] b)
    {
        String stmp="";
        StringBuilder sb = new StringBuilder("");
        for (int n=0;n<b.length;n++)
        {
            stmp = Integer.toHexString(b[n] & 0xFF);
            sb.append((stmp.length()==1)? "0"+stmp : stmp);
            sb.append(" ");
        }
        return sb.toString().toUpperCase().trim();
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
                    //Toast.makeText(ControlActivity.this, str, Toast.LENGTH_LONG).show();

                    // ed_reedit.setText(str0+str);
                    Log.v(str, "control_case1");
                    break;

                case 2://回复反馈
                    String str1=(String)msg.obj;
                    Tv_txt.setText(str1);
                    Log.v(str1, "control_case2");
                    break;

                case 3:
                    Creat_List();
                    Creat_feel_List();
                    Log.v("ok", "control_case3");
                    break;
                case 4:
                    String str4=(String)msg.obj;
                   //Toast.makeText(ControlActivity.this, "已发送:"+str4, Toast.LENGTH_SHORT).show();

                    break;
                case 5:
                   // String str5=(String)msg.obj;
                    Tv_ztl.setText(ztn+":"+ztl);

                   // Toast.makeText(ControlActivity.this, "已发送:"+str5, Toast.LENGTH_SHORT).show();
                    break;
                case 6:

                    Log.v("ok", "control_case6");
                    break;
                case 7:

                    SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
                    Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                    String times = formatter.format(curDate);

                    feel_value[feel_nums]=feel_zhi;//保存数值
                    feel_time[feel_nums]=times;//保存数值

                     /*保存传感器值*/
                    SharedPreferences userInfo;
                    userInfo = getSharedPreferences("sbfeel"+sb_id, 0);

                    for (int i=0;i<Feel_List;i++)
                    {
                        userInfo.edit().putString("feel_value"+i, feel_zhi).commit();
                        userInfo.edit().putString("feel_time"+i, times).commit();
                        Log.v(feel_zhi, "control_case7-0");
                    }
                    // listItem = new ArrayList<HashMap<String, Object>>();/*在数组中存放数据*/
                    listItem.clear();
                    for (int i = 0; i <Feel_List; i++)

                    {
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        // map.put("ItemImage", R.drawable.w1);//加入图片
                        map.put("ItemTitle",  feel_name[i]+":"+feel_danwei[i]);
                        if(i==feel_nums)
                        {
                            map.put("ItemValue", feel_zhi);
                            map.put("ItemTime",times);
                        }
                        else
                        {
                            map.put("ItemValue", feel_value[i]);
                            map.put("ItemTime", feel_time[i]);
                        }

                        listItem.add(map);
                    }
                    mSimpleAdapter .notifyDataSetChanged();




                    break;
                case 8:
                    //加载存储的传感器参数，显示
                    SharedPreferences userInfos;
                    userInfos = getSharedPreferences("sbfeel"+sb_id, 0);
                    String strs =userInfos.getString("feel_value0","");
                    Log.v(strs, "control_case8-0");

                    Tv_feel1_value.setText(strs);
                    strs =userInfos.getString("feel_time0","");

                    Tv_feel1_time.setText(strs);

                    strs =userInfos.getString("feel_value1","");
                    Log.v(strs, "control_case8-1");
                    Tv_feel2_value.setText(strs);
                    strs =userInfos.getString("feel_time1","");
                    Tv_feel2_time.setText(strs);

                    strs =userInfos.getString("feel_value2","");
                    Log.v(strs, "control_case8-2");
                    Tv_feel3_value.setText(strs);
                    strs =userInfos.getString("feel_time2","");
                    Tv_feel3_time.setText(strs);

                    Log.v("ok", "control_case8");
                    break;

                    case 9:
                        String str9=(String)msg.obj;
                        // 1.获取NotificationManager对象
                        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        // 2.初始化Notification对象
                        n = new Notification();
                        // 设置通知的icon
                       // n.icon = R.drawable.notify;
                        // 设置通知在状态栏上显示的滚动信息
                        //n.tickerText = "一个通知";
                        // 设置通知的时间
                       // n.when = System.currentTimeMillis();

                        // 3.设置通知的显示参数

                        Intent intent = new Intent(ControlActivity.this, ControlActivity.class);
                        PendingIntent pi = PendingIntent.getActivity(ControlActivity.this, 0, intent, 0);
//                        n.setLatestEventInfo(getApplicationContext(), "通知标题", "通知内容", pi);
                        // 4.发送通知
                        n.defaults|= Notification.DEFAULT_SOUND;
                        nm.notify(0, n);

                        //notification.tickerText = "Hello Notification";

                        Log.v(str9, "control_case9");
                    break;

            }
            super.handleMessage(msg);
        }
    };
    //后退按键
    public void onBackPressed() {

        lo_flag=1;
        feel_flag=1;
        re_flag=1;

        ControlActivity.this.finish();
        //code......
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.control, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
         //automatically handle clicks on the Home/Up button, so long
         //as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.about) {
            ControlActivity.this.finish();
            //return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
