package com.mobile.fuhome.app;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.mobile.fuhome.app.adapter.MyAdapter;
import com.mobile.fuhome.app.adapter.UniversalBaseAdapter;
import com.mobile.fuhome.app.adapter.ViewHolder;
import com.mobile.fuhome.app.application.ApplicationUtil;
import com.mobile.fuhome.app.application.BaseActivity;
import com.mobile.fuhome.app.application.Constants;
import com.mobile.fuhome.app.bean.FeelBean;
import com.mobile.fuhome.app.bean.FeelValueBean;
import com.mobile.fuhome.app.utils.HttpUtils;
import com.mobile.fuhome.app.utils.SharedPreferenceUtils;
import com.mobile.fuhome.app.utils.TextUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Ryan on 2016/7/29.
 */
public class ControlDeviceActivity extends BaseActivity {
    private static final String TAG = "ryan";
    private String id, sb_id, sb_name;
    private String ztn, ztl;
    private TextView Tv_txt, Tv_ztl;
    private EditText E_com;
    private Button B_open, B_close, B_send;
    private ListView lv;
    private List<Map<String, String>> feelDataList;
    private RecyclerView recyclerView;

    private DatagramSocket socket;
    private String str_openid, str_userid, str_psw;
    //设备的socket
    private String str_ip, str_port;
    private InetAddress sbAddress, serverAddress;
    private int port;

    private char lo_flag = 0;//自定义按键请求成功标志
    private char feel_flag = 0;//feel传感器请求成功标志
    private char re_flag = 0;//接收

    private int zaici = 0;//再次进入的标志 数值

    private DatagramPacket packet;
    private byte[] getBuf = new byte[1024];//接收缓冲
    private DatagramPacket getPacket = new DatagramPacket(getBuf, getBuf.length);//接收数据报

    private NotificationManager nm;
    private Notification n;


    /* 处理接收到数据UI */
    Handler mHandler_re = new Handler() {
        // 注意：在各个case后面不能做太耗时的操作，否则出现ANR对话框
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1://显示传输的内容
                    String str = (String) msg.obj;
                    //tv_review.setText(str);
                    //Toast.makeText(ControlActivity.this, str, Toast.LENGTH_LONG).show();
                    // ed_reedit.setText(str0+str);
                    Log.v(str, "control_case1");
                    break;
                case 2://回复反馈
                    String str1 = (String) msg.obj;
                    Tv_txt.setText(str1);
                    Log.v(str1, "control_case2");
                    break;
                case 3:
                    if (mAdapter != null) {
                        mAdapter.notifyDataSetChanged();
                    }
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case 4:
                    String str4 = (String) msg.obj;
                    //Toast.makeText(ControlActivity.this, "已发送:"+str4, Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    // String str5=(String)msg.obj;
                    Tv_ztl.setText(ztn + ":" + ztl);
                    // Toast.makeText(ControlActivity.this, "已发送:"+str5, Toast.LENGTH_SHORT).show();
                    break;
                case 6:
                    Log.v("ok", "control_case6");
                    break;
                case 7:
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                    requestFeelValue();
//
//                     /*保存传感器值*/
//                    SharedPreferences userInfo;
//                    userInfo = getSharedPreferences("sbfeel" + sb_id, 0);
//                    for (int i = 0; i < Feel_List; i++) {
//                        userInfo.edit().putString("feel_value" + i, feel_zhi).commit();
//                        userInfo.edit().putString("feel_time" + i, times).commit();
//                        Log.v(feel_zhi, "control_case7-0");
//                    }
//                    // listItem = new ArrayList<HashMap<String, Object>>();/*在数组中存放数据*/
//                    listItem.clear();
//                    for (int i = 0; i < Feel_List; i++)
//                    {
//                        HashMap<String, Object> map = new HashMap<String, Object>();
//                        // map.put("ItemImage", R.drawable.w1);//加入图片
//                        map.put("ItemTitle", feel_name[i] + ":" + feel_danwei[i]);
//                        if (i == feel_nums) {
//                            map.put("ItemValue", feel_zhi);
//                            map.put("ItemTime", times);
//                        } else {
//                            map.put("ItemValue", feel_value[i]);
//                            map.put("ItemTime", feel_time[i]);
//                        }
//                        listItem.add(map);
//                    }
//                    mSimpleAdapter.notifyDataSetChanged();
                    break;
                case 8:
                    //加载存储的传感器参数，显示
                   /* SharedPreferences userInfos;
                    userInfos = getSharedPreferences("sbfeel" + sb_id, 0);
                    String strs = userInfos.getString("feel_value0", "");
                    Log.v(strs, "control_case8-0");
                    Tv_feel1_value.setText(strs);
                    strs = userInfos.getString("feel_time0", "");
                    Tv_feel1_time.setText(strs);
                    strs = userInfos.getString("feel_value1", "");
                    Log.v(strs, "control_case8-1");
                    Tv_feel2_value.setText(strs);
                    strs = userInfos.getString("feel_time1", "");
                    Tv_feel2_time.setText(strs);
                    strs = userInfos.getString("feel_value2", "");
                    Log.v(strs, "control_case8-2");
                    Tv_feel3_value.setText(strs);
                    strs = userInfos.getString("feel_time2", "");
                    Tv_feel3_time.setText(strs);
                    Log.v("ok", "control_case8");*/
                    break;
                case 9:
                    String str9 = (String) msg.obj;
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
                    Intent intent = new Intent(ControlDeviceActivity.this, ControlDeviceActivity.class);
                    PendingIntent pi = PendingIntent.getActivity(ControlDeviceActivity.this, 0, intent, 0);
//                        n.setLatestEventInfo(getApplicationContext(), "通知标题", "通知内容", pi);
                    // 4.发送通知
                    n.defaults |= Notification.DEFAULT_SOUND;
                    nm.notify(0, n);
                    //notification.tickerText = "Hello Notification";
                    Log.v(str9, "control_case9");
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private UniversalBaseAdapter adapter;
    private LinearLayoutManager mLayoutManager;
    private MyAdapter mAdapter;
    private List<Map<String, String>> stateList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_device);

        ApplicationUtil appUtil = (ApplicationUtil) this.getApplication();
        try {
            socket = appUtil.Out_socket();
            str_openid = Constants.STR_OPENID;
            str_userid = SharedPreferenceUtils.getString(this,"userid","");
            str_psw=SharedPreferenceUtils.getString(this,"psw","");
            str_ip=Constants.STR_IP;
            str_port=Constants.STR_PORT;
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        initView();
        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //3、查询设备传感器列表
        if (zaici == 1) {
            zaici = 0;
            new Thread() {

                public void run() {
                    try {
                        Thread.sleep(1000);// 线程暂停1秒，单位毫秒
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
        }
    }

    //加载动态页面
    protected void onRestart() {
        super.onRestart();
        zaici = 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        lo_flag = 1;
        feel_flag = 1;
        re_flag = 1;
    }

    private void initView() {

        //读取账户密码

        //取得启动该Activity的Intent对象
        Intent intent = getIntent();
        /*取出Intent中附加的数据*/
        ztn = intent.getStringExtra("sb_staname");
        ztl = intent.getStringExtra("sb_stavalue");
        sb_name = intent.getStringExtra("sb_name");
        sb_id = intent.getStringExtra("sb_id");
        id = intent.getStringExtra("id");

        setTitle(sb_name);

        Tv_txt = (TextView) findViewById(R.id.Tv_txt);
        Tv_ztl = (TextView) findViewById(R.id.Tv_ztl);
        E_com = (EditText) findViewById(R.id.E_com);
        B_send = (Button) findViewById(R.id.B_send);
        B_open = (Button) findViewById(R.id.B_open);
        B_close = (Button) findViewById(R.id.B_close);

        recyclerView = (RecyclerView) findViewById(R.id.feel_control);
        mLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(mLayoutManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
//        recyclerView.setHasFixedSize(true);
        //创建并设置Adapter
        stateList = new ArrayList<>();
        mAdapter = new MyAdapter(stateList);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
//                Log.i("ryan","dianji" +position);
                sendout(position);
            }
        });

        setButtonListerner();

        feelDataList = new ArrayList<>();

        lv = (ListView) findViewById(R.id.feel_list);//得到ListView对象的引用 /*为ListView设置Adapter来绑定数据*/
        adapter = new UniversalBaseAdapter<Map<String, String>>(this, feelDataList, R.layout.feel_item) {
            @Override
            protected void convert(ViewHolder viewHolder, Map<String, String> item) {
                viewHolder.setText(R.id.name_tv, item.get("feelname"));
                viewHolder.setText(R.id.num_tv, item.get("feelvalue"));
                viewHolder.setText(R.id.cell_tv, item.get("feelunit"));
                viewHolder.setText(R.id.time_tv, item.get("feeltime"));
            }
        };
        lv.setAdapter(adapter);
    }


    private void sendout(final int position) {


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
                    String str_com = stateList.get(position).get("comstring");

                    int str_com_length = str_com.length();
                    Log.v("control", str_com + "..." + str_com_length);
                    data_temp = str_com.getBytes("UTF-8");

                    for (int i = 0; i < str_com_length; i++) {
                        data[77 + i] = data_temp[i];
                    }
                    data[77 + str_com_length] = 5;
                    data[0] = 0;
                    data[1] = (byte) (0x4e + str_com_length);

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
        msg1.obj = stateList.get(position).get("comstring");
        msg1.what = 4;
        mHandler_re.sendMessage(msg1);
    }

    private void setButtonListerner() {
        //send按钮
        B_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //控制1
               /* */
                new Thread() {
                    public void run() {
                        try {
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
                            String str_com = E_com.getText().toString();
                            int str_com_length = str_com.length();
                            data_temp = str_com.getBytes();

                            for (int i = 0; i < str_com_length; i++) {
                                data[77 + i] = data_temp[i];
                            }
                            data[77 + str_com_length] = 5;
                            data[0] = 0;
                            data[1] = (byte) (0x4e + str_com_length);

                            //创建一个DatagramPacket对象，并指定要讲这个数据包发送到网络当中的哪个、地址，以及端口号
                            DatagramPacket packet = new DatagramPacket(data, data[1], serverAddress, port);
                            //调用socket对象的send方法，发送数据
                            socket.send(packet);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });//send按钮

        //open按钮
        B_open.setOnClickListener(new View.OnClickListener() {
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
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });//open按钮

        //close
        B_close.setOnClickListener(new View.OnClickListener() {
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
                            String str_com = "close";
                            int str_com_length = str_com.length();
                            data_temp = str_com.getBytes();

                            for (int i = 0; i < str_com_length; i++) {
                                data[77 + i] = data_temp[i];
                            }
                            data[77 + str_com_length] = 5;
                            data[0] = 0;
                            data[1] = (byte) (0x4e + str_com_length);
                            //创建一个DatagramPacket对象，并指定要讲这个数据包发送到网络当中的哪个、地址，以及端口号
                            DatagramPacket packet = new DatagramPacket(data, data[1], serverAddress, port);
                            //调用socket对象的send方法，发送数据
                            socket.send(packet);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });//close按钮
    }

    private void loadData() {
        re_flag = 0;
        loadFeelInfo();///* 加载菜单和传感器列表 */
        requestFeelValue();
//        receiveUDPData(); //接收UDP数据线程
    }

    private void requestFeelValue() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("f", "10");
        params.put("id", id);
        HttpUtils.addCommValue(ControlDeviceActivity.this,params);
        HttpUtils.requestPost(Constants.HOST, params, new HttpUtils.ResultCallback() {
            @Override
            public void onResponse(Call call, String response) {
                FeelValueBean feelValueBean = FeelValueBean.objectFromData(response);//此处有bug
                String log = feelValueBean.getLog();
                if (TextUtils.equals(log,"ok")){
                    Tv_ztl.setText(feelValueBean.getStatename());
                    Tv_txt.setText(feelValueBean.getStatevalue());
                    List<FeelValueBean.FeelvalueBean> feelvalue = feelValueBean.getFeelvalue();
                    for (int i = 0; i < feelvalue.size(); i++) {
                        for (int j = 0; j <feelDataList.size(); j++) {
                            if (TextUtils.equals(feelvalue.get(i).getFeelnum(),feelDataList.get(j).get("feelnum"))){
                                Map<String, String> map = feelDataList.get(i);
                                map.put("feelvalue",feelvalue.get(i).getFeelvalue1());
                                map.put("feeltime",feelvalue.get(i).getFeeltime());
                            }
                        }
                    }
                    mHandler_re.sendEmptyMessageDelayed(7,2000);
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {

            }
        });
    }


    /**
     * 加载菜单和传感器列表
     */
    private void loadFeelInfo() {
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
                        HttpUtils.addCommValue(ControlDeviceActivity.this,params);
                        //服务器请求路径
                        String strUrlPath = "http://fuhome.net/api/sblist/";
                        String strResult = HttpUtils.submitPostData(strUrlPath, params, "utf-8");

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
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * //接收UDP数据线程
     */
    private void receiveUDPData() {
        new Thread() {

            public void run() {
                try {
                    while (re_flag == 0) {
                        socket.receive(getPacket);
                        Log.v("Control_udp_Rx", "收到信息:" + TextUtil.byte2HexStr(getBuf));

                        //符合协议
                        if (getBuf[2] == 2 && getBuf[3] == 'S') {
                            if (getBuf[4] == 0x0A) {
                                //状态量
                                if (getBuf[13] == 'S') {
                                    //从14开始 总长度-15

                                    byte[] strby = new byte[100];

                                    for (int i = 0; i < (int) getBuf[1] - 15; i++) {
                                        strby[i] = getBuf[14 + i];
                                    }

                                    String res = new String(strby, "UTF-8");
                                    Log.v("状态量：", res);
                                    ztl = res;
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
                            //接到传感器值
                            else if (getBuf[4] == 0x0C) {
                                Log.v("com", "手机收到传感器值");
                                //传感器转发到手机

                                //找到num
                                int jj_num = (int) getBuf[13] * 256 + (int) getBuf[14];
                                String str_num = Integer.toString(jj_num);

                                int jj_aa = (int) getBuf[16];//找到aa后面的
                                byte[] aa = new byte[10];
                                for (int i = 0; i < jj_aa; i++) {
                                    aa[i] = getBuf[17 + i];
                                }
                                String str_aa = new String(aa, "UTF-8");

                                for (int i = 0; i < feelDataList.size(); i++) {
                                    Log.i("ryan", "run: ----"+str_num+" -- "+feelDataList.get(i).get("feelnum"));
                                    if (str_num.equals(feelDataList.get(i).get("feelnum"))) {
                                        feelDataList.get(i).put("feelvalue", str_aa);

                                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                                        String times = formatter.format(curDate);
                                        feelDataList.get(i).put("feeltime", times);
                                        Log.i("ryan", "run1: ----"+str_aa+" -- "+times);
                                        Log.i("ryan", "map i: ----"+feelDataList.get(i).toString());
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
    }

    // 普通Json数据解析-分析自定义菜单
    private void parseJson(String strResult) {
        if (strResult != null && strResult.startsWith("\ufeff")) {
            strResult = strResult.substring(1);
        }
        FeelBean feelBean = FeelBean.objectFromData(strResult);
        List<FeelBean.SbfeelBean> sbfeel = feelBean.getSbfeel();
        List<FeelBean.SbcomBean> sbcom = feelBean.getSbcom();
        for (FeelBean.SbfeelBean sbfeelBean : sbfeel) {
            Map<String, String> map = new HashMap<>();
            map.put("feelname", sbfeelBean.getFeelname());
            map.put("feelnum", sbfeelBean.getFeelnum());
            map.put("feelstyle", sbfeelBean.getFeelstyle());
            map.put("feelunit", sbfeelBean.getFeelunit());
            map.put("feelvalue","");
            map.put("feeltime","");
            feelDataList.add(map);
        }
        for (FeelBean.SbcomBean sbcomBean : sbcom) {
            Map<String, String> map = new HashMap<>();
            map.put("comname", sbcomBean.getComname());
            map.put("comstring", sbcomBean.getComstring());
            stateList.add(map);
        }
//        try {
//
//            JSONObject jsonObj = new JSONObject(strResult);
//            Count_List = jsonObj.getInt("com");
//            JSONArray jsonObjs = new JSONObject(strResult).getJSONArray("sbcom");
//            int num = jsonObjs.length();
//
//            //保存自定义菜单信息
//            SharedPreferences userInfo;
//            userInfo = getSharedPreferences("sbcom" + sb_id, 0);
//            userInfo.edit().putString("com_num", "" + num).commit();
//
//            for (int i = 0; i < num; i++) {
//
//                JSONObject jsonObja = ((JSONObject) jsonObjs.opt(i));
//
//                com_name[i] = jsonObja.getString("comname");
//                com_string[i] = jsonObja.getString("comstring");
//
//                //此处是存储按键表的
//                userInfo.edit().putString("com_name" + i, com_name[i]).commit();
//                userInfo.edit().putString("com_string" + i, com_string[i]).commit();
//            }
//            Log.v("Menu_list", "caidan_count");
//
//            if (strResult != null && strResult.startsWith("\ufeff")) {
//                strResult = strResult.substring(1);
//            }
//
//            Feel_List = jsonObj.getInt("feel");
//            jsonObjs = new JSONObject(strResult).getJSONArray("sbfeel");
//            num = jsonObjs.length();
//
//            //保存传感器信息
//            //SharedPreferences userInfo;
//            userInfo = getSharedPreferences("sbfeel" + sb_id, 0);
//            userInfo.edit().putString("feel_count", "" + num).commit();
//
//            for (int i = 0; i < num; i++) {
//                JSONObject jsonObja = ((JSONObject) jsonObjs.opt(i));
//                feel_num[i] = jsonObja.getString("feelnum");
//                feel_name[i] = jsonObja.getString("feelname");
//                feel_danwei[i] = jsonObja.getString("feelunit");
//                //此处是存储传感器表
//                userInfo.edit().putString("feel_num" + i, feel_num[i]).commit();
//                userInfo.edit().putString("feel_name" + i, feel_name[i]).commit();
//                userInfo.edit().putString("feel_danwei" + i, feel_danwei[i]).commit();
//            }
//        } catch (JSONException e) {
//            System.out.println("Json parse error");
//            e.printStackTrace();
//        }
    }

    //后退按键
    public void onBackPressed() {

        lo_flag = 1;
        feel_flag = 1;
        re_flag = 1;

        finish();
        //code......
    }

}
