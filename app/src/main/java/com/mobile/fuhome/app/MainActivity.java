package com.mobile.fuhome.app;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.fuhome.app.bean.UpdataInfo;
import com.mobile.fuhome.app.service.NetService;
import com.mobile.fuhome.app.utils.HttpUtils;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends Activity {


    private EditText E_name, E_psw;
    private Button B_login;
    private CheckBox C_rem;
    String str_name; //这是要传输的数据
    String str_openid = "83762687e6694006f6d1161864164d0f"; //这是要传输的数据
    String str_psw; //这是要传输的数据

    DatagramSocket socket;
    char lo_flag = 0;//0，1失败，2登录
    char flag = 0;//建立socket

    char appflag = 0;//0需要重新初始化

    private TextView Tv_banben;
    byte[] getBuf = new byte[1024];//接收缓冲
    DatagramPacket getPacket = new DatagramPacket(getBuf, getBuf.length);//接收数据报

    //版本更新
    private UpdataInfo info;
    private String localVersion;
    private final String TAG = this.getClass().getName();

    private NetService binderService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Tv_banben = (TextView) findViewById(R.id.Tv_banben);
        E_name = (EditText) findViewById(R.id.E_name);
        E_psw = (EditText) findViewById(R.id.E_psw);
        C_rem = (CheckBox) findViewById(R.id.C_rem);
        B_login = (Button) findViewById(R.id.B_login);

        //读取保存的账号，密码，记住密码否
        SharedPreferences userInfo;
        userInfo = getSharedPreferences("login", 0);
        String str = userInfo.getString("name", "");
        E_name.setText(str);
        str = userInfo.getString("psw", "");
        E_psw.setText(str);
        str_psw = str;


        //记住密码
        str = userInfo.getString("rem", "");

        if (str.equals("yes")) {
            C_rem.setChecked(true);

        } else {
            C_rem.setChecked(false);

        }


        //app socket

        ApplicationUtil appUtil = (ApplicationUtil) MainActivity.this.getApplication();
        try {
            appUtil.init();
            socket = appUtil.Out_socket();
        } catch (Exception e1) {
            e1.printStackTrace();
        }


        //检查版本
        try {
            localVersion = getVersionName();
            CheckVersionTask cv = new CheckVersionTask();
            new Thread(cv).start();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //Log.e("","绑定服务");
        //绑定服务
        // Intent serviceIntent =new Intent(MainActivity.this,NetService.class);
        //bindService(serviceIntent,mConnection,Context.BIND_AUTO_CREATE);

        //登录

        B_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //记录下账号和密码
                str_name = E_name.getText().toString(); //这是要传输的数据
                String str_psws = E_psw.getText().toString();
                if (str_psws.length() != 32)
                    str_psw = MD5(E_psw.getText().toString());

                if (C_rem.isChecked()) {
                    SharedPreferences userInfo;
                    userInfo = getSharedPreferences("login", 0);
                    userInfo.edit().putString("name", str_name).apply();
                    userInfo.edit().putString("psw", str_psw).apply();
                    userInfo.edit().putString("psws", str_psw).apply();
                    userInfo.edit().putString("rem", "yes").apply();
                } else {
                    SharedPreferences userInfo;
                    userInfo = getSharedPreferences("login", 0);
                    userInfo.edit().putString("name", str_name).apply();
                    userInfo.edit().putString("psw", "").apply();
                    userInfo.edit().putString("psws", str_psw).apply();
                    userInfo.edit().putString("rem", "").apply();

                }


                if (str_name.equals("") | str_psw.equals("")) {
                    Log.v("name_psw", "空");
                } else {
                    Log.v("进入http", "准备");
                    //登录

                    // socket=binderService.Out_socket();
                    // Log.e("","socket");
                    //binderService.Set_name( str_name, str_psw);
                    //Log.e("","mm");

                              /*  InetAddress serverAddress = InetAddress.getByName("115.28.93.201");
                                // InetAddress serverAddress = InetAddress.getByName("192.168.1.104");
                                int port = Integer.parseInt("7001");

                                // Log.v(Integer.toString(port), "send_port");
                                //首先创建一个DatagramSocket对象

                                //组合协议f2
                                //f=2&n=lovelife&p=123456&from=mobile->server
                                String strs = "f=2&n=" + str_name + "&p=" + str_psw + "&d=1287369152&from=mobile->server";
                                String str; //这是要传输的数据

                                byte data[] = strs.getBytes(); //把传输内容分解成字节
                                //创建一个DatagramPacket对象，并指定要讲这个数据包发送到网络当中的哪个、地址，以及端口号
                                DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, port);
                                //调用socket对象的send方法，发送数据*/


                          /* 任务 */
                    lo_flag = 0;
                    new Thread() {
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub

                            try {


                                if (lo_flag == 0) {
                                    Log.v("进入http", "try");

                                    //建立一个NameValuePair数组，用于存储欲传递的参数
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("openid", str_openid);
                                    params.put("username", str_name);
                                    params.put("psw", str_psw);
                                    params.put("f", "2");
                                    //服务器请求路径
                                    String strUrlPath = "http://fuhome.net/api/sblist/";
                                    String strResult = HttpUtils.submitPostData(strUrlPath, params, "utf-8");


                                    Log.v("http_post", strResult.replaceAll(" ", ""));


                                    //String rev = EntityUtils.toString(response.getEntity());//返回json格式： {"id": "27JpL~j4vsL0LX00E00005","version": "abc"}
                                    JSONObject obj = new JSONObject(strResult);
                                    String dis = obj.getString("dis");
                                    String log = obj.getString("log");
                                    //String userid = obj.getString("userid");

                                    Log.v("dis", dis);
                                    Log.v("log", log);


                                    if (log.equals("ok")) {

                                        lo_flag = 1;
                                        String userid = obj.getString("userid");
                                        Log.v("userid", userid);


                                        SharedPreferences userInfo;
                                        userInfo = getSharedPreferences("login", 0);
                                        userInfo.edit().putString("userid", userid).apply();

                                        //设置
                                        ApplicationUtil appUtil = (ApplicationUtil) MainActivity.this.getApplication();
                                        try {
                                            //设置userid
                                            appUtil.Set_userid(str_openid, userid, str_psw);

                                        } catch (Exception e1) {
                                            e1.printStackTrace();
                                        }


                                        Message msg1 = Message.obtain();
                                        msg1.obj = "登录成功";
                                        msg1.what = 1;
                                        mHandler_re.sendMessage(msg1);

                                        //建立service

                                        Intent startIntent = new Intent(MainActivity.this, NetService.class);
                                        startService(startIntent);


                                            /* 新建一个Intent对象 */
                                        Intent intent = new Intent();
                                        //intent.putExtra("r_name",str_name);
                                        //intent.putExtra("r_psw",str_psw);
                                            /* 指定intent要启动的类 */
                                        intent.setClass(MainActivity.this, SblistActivity.class);
                                             /* 启动一个新的Activity */
                                        MainActivity.this.startActivity(intent);
                                             /* 关闭当前的Activity */
                                        MainActivity.this.finish();

                                        Log.v("login", "right");
                                    } else {

                                        lo_flag = 1;

                                        Log.v("login", "wrong");

                                        Message msg2 = Message.obtain();
                                        String res = "账号或者密码错误";
                                        msg2.obj = res;
                                        msg2.what = 1;
                                        mHandler_re.sendMessage(msg2);
                                        //Toast.makeText(MainActivity.this, "w", Toast.LENGTH_SHORT).show();

                                    }

                                    // Thread.sleep(3000);// 线程暂停30秒，单位毫秒
                                }//while

                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }.start();

                }


            }


        });//登录按钮


        //while(binderService==null);


    }//onCreate

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            binderService = ((NetService.LocalBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

            binderService = null;
        }
    };

    //获取当前的版本号
    private String getVersionName() throws Exception {
        //getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageManager packageManager = getPackageManager();
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),
                0);
        return packInfo.versionName;
    }

    //检查是否有新版本
    public class CheckVersionTask implements Runnable {
        InputStream is;

        public void run() {
            try {
                String path = getResources().getString(R.string.url_server);
                URL url = new URL(path);
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("GET");
                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    // 从服务器获得一个输入流
                    is = conn.getInputStream();
                }
                info = UpdataInfoParser.getUpdataInfo(is);


                Log.i(TAG, "版本更新 =" + info.getVersion() + "-- 服务器版本" + localVersion);
                if (info.getVersion().equals(localVersion)) {
                    Log.i(TAG, "版本号相同");

                } else {
                    Log.i(TAG, "版本号不相同 ");
                    Message msg3 = Message.obtain();
                    String res = "已有新版本，请到:菜单-关于-检查更新";
                    msg3.obj = res;
                    msg3.what = 3;
                    mHandler_re.sendMessage(msg3);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    //处理接收到数据UI
    Handler mHandler_re = new Handler() {
        // 注意：在各个case后面不能做太耗时的操作，否则出现ANR对话框
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1://显示传输的内容
                    String str = (String) msg.obj;
                    //tv_review.setText(str);
                    Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();

                    // ed_reedit.setText(str0+str);
                    Log.v(str, "handler_login_case1");
                    break;

                case 2:
                    String str1 = (String) msg.obj;

                    Log.v(str1, "handler_login_case2");
                    break;

                case 3:
                    String str2 = (String) msg.obj;
                    Toast.makeText(MainActivity.this, str2, Toast.LENGTH_LONG).show();
                    Log.v(str2, "handler_login_case3");
                    Tv_banben.setText(str2);
                    Tv_banben.setVisibility(View.VISIBLE);
                    break;

            }
            super.handleMessage(msg);
        }
    };

    //后退按键
    public void onBackPressed() {

        //app

        ApplicationUtil appUtil = (ApplicationUtil) MainActivity.this.getApplication();
        try {
            // appUtil.Close_socket();
            MainActivity.this.finish();

        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }

    public static String MD5(String str) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        char[] charArray = str.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);

        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

        if (id == R.id.enlist) {

           /* 新建一个Intent对象 */
            Intent intent0 = new Intent(this, EnlistActivity.class);
            startActivity(intent0);
            // return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
