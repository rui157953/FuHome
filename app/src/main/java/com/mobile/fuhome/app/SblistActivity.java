package com.mobile.fuhome.app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.fuhome.app.service.NetService;
import com.mobile.fuhome.app.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SblistActivity extends Activity {

    DatagramSocket socket;
    private char flag = 0;//socket标志
    private char lo_flag = 0;//请求成功标志
    private String str_openid, str_userid, str_psw;


    private TextView tv_review;
    byte[] getBuf = new byte[1024];//接收缓冲
    DatagramPacket getPacket = new DatagramPacket(getBuf, getBuf.length);//接收数据报
    InetAddress serverAddress;
    int port;
   /* 列表 */

    public String[] id = new String[10];
    public String[] sb_id = new String[10];
    public String[] sb_name = new String[10];
    public String[] sb_staname = new String[10];
    public String[] sb_stavalue = new String[10];
    public String[] sb_imgurl = new String[10];
    //通过程序生成我们的ListView
    private ListView lv;
    public int Count_List = 0;
    private int j, k;

    private int zaici = 0;//再次进入的标志
    char re_flag = 0;
    private NetService binderService;
    char appflag = 0;//0需要重新初始化

    private long mExitTime;//时间间隔

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sblist);

        /*首先刷新设备列表  */
        Log.e("sblist", "start onCreate~~~");

        //app

    }


    //用于加载动态列表
    protected void onStart() {
        super.onStart();
        Log.e("sblist", "start onStart~~~" + zaici);

        if (zaici == 0) {
            zaici = 1;


            ApplicationUtil appUtil = (ApplicationUtil) SblistActivity.this.getApplication();
            try {
                str_openid = appUtil.Out_openid();
                str_userid = appUtil.Out_userid();
                str_psw = appUtil.Out_psw();

            } catch (Exception e1) {
                e1.printStackTrace();
            }

     /* 任务 */
            lo_flag = 0;
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
                            params.put("f", "5");
                            //服务器请求路径
                            String strUrlPath = "http://fuhome.net/api/sblist/";
                            String strResult = HttpUtils.submitPostData(strUrlPath, params, "utf-8");

                            Log.v("sblist_http_post", strResult.replaceAll(" ", ""));

                            JSONObject obj = new JSONObject(strResult);
                            String dis = obj.getString("dis");
                            String log = obj.getString("log");
                            //String userid = obj.getString("userid");

                            Log.v("dis", dis);
                            Log.v("log", log);

                            if (log.equals("ok")) {
                                Log.v("login", "right");
                                lo_flag = 1;
                                /* 生成设备列表 */
                                parseJson(strResult);

                                //显示设备列表
                                Message msg1 = Message.obtain();
                                msg1.what = 3;
                                mHandler_re.sendMessage(msg1);
                                Log.v("sblist_creat", "设备列表");


                            } else {

                                lo_flag = 1;
                                Log.v("login", "wrong");
                            }

                            // Thread.sleep(3000);// 线程暂停30秒，单位毫秒
                        }//while

                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }.start();

        }//第一次进来加载
        else {


        }
    }

    //加载动态页面
    protected void onRestart() {
        super.onRestart();
        Log.e("sblist", "start onRestart~~~");
    }

    @Override
    protected void onResume() {

        super.onResume();
        Log.e("sblist", "start onResume~~~");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("sblist", "start onPause~~~");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("sblist", "start onStop~~~");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("sblist", "start onDestroy~~~");
    }




    /* 分拆返回列表数据 */

    // 普通Json数据解析

    private void parseJson(String strResult) {

        try {

            if (strResult != null && strResult.startsWith("\ufeff")) {
                strResult = strResult.substring(1);
            }

            //保存自定义菜单信息
            SharedPreferences userInfo;
            userInfo = getSharedPreferences("sblist", 0);

            JSONObject jsonObj = new JSONObject(strResult);
            Count_List = jsonObj.getInt("c");
            JSONArray jsonObjs = new JSONObject(strResult).getJSONArray("sb");
            int num = jsonObjs.length();
            //此处是存储按键表的
            userInfo.edit().putString("sb_num", "" + num).commit();


            for (int i = 0; i < num; i++) {

                JSONObject jsonObja = ((JSONObject) jsonObjs.opt(i));
                id[i] = jsonObja.getString("id");
                sb_id[i] = jsonObja.getString("sbid");
                sb_name[i] = jsonObja.getString("sbname");
                sb_staname[i] = jsonObja.getString("statename");
                sb_stavalue[i] = jsonObja.getString("statevalue");
                sb_imgurl[i] = jsonObja.getString("imgurl");

                //此处是存储设备列表的
                userInfo.edit().putString("id", id[i]).commit();
                userInfo.edit().putString("sb_id" + i, sb_id[i]).commit();
                userInfo.edit().putString("sb_name" + i, sb_name[i]).commit();
                userInfo.edit().putString("sb_staname" + i, sb_staname[i]).commit();
                userInfo.edit().putString("sb_stavalue" + i, sb_stavalue[i]).commit();


            }
            Log.v("" + Count_List, "count");


        } catch (JSONException e) {

            System.out.println("Json parse error");

            e.printStackTrace();

        }

    }


    /*  显示设备列表 */
    void Creat_List() {

        lv = (ListView) findViewById(R.id.lv);//得到ListView对象的引用 /*为ListView设置Adapter来绑定数据*/

        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();/*在数组中存放数据*/
        for (int i = 0; i < Count_List; i++)

        {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemImage", R.drawable.w1);//加入图片
            map.put("ItemTitle", sb_name[i]);
            map.put("ItemText", "ID:" + sb_id[i]);
            listItem.add(map);
        }

        SimpleAdapter mSimpleAdapter = new SimpleAdapter(this, listItem,//需要绑定的数据
                R.layout.item,//每一行的布局//动态数组中的数据源的键对应到定义布局的View中
                new String[]{"ItemTitle", "ItemText"}, new int[]{R.id.ItemTitle, R.id.ItemText}
        );

        lv.setAdapter(mSimpleAdapter);

        //为ListView绑定适配器
        lv.setOnItemClickListener(new OnItemClickListener() {

                                      @Override
                                      public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                                          // setTitle("你点击了第" + arg2 + "行");//设置标题栏显示点击的行

                 /* 新建一个Intent对象 */
                                          Intent intent = new Intent();

                                          intent.putExtra("id", id[arg2]);
                                          intent.putExtra("sb_id", sb_id[arg2]);
                                          intent.putExtra("sb_name", sb_name[arg2]);
                                          intent.putExtra("sb_staname", sb_staname[arg2]);
                                          intent.putExtra("sb_stavalue", sb_stavalue[arg2]);
                                          //保存
                                                 /* SharedPreferences userInfo;
                                                  userInfo = getSharedPreferences("Control", 0);
                                                  userInfo.edit().putString("sb_name", sb_name[arg2]).commit();
                                                  userInfo.edit().putString("sb_id", sb_id[arg2]).commit();
                                                  userInfo.edit().putString("id",  id[arg2]).commit();*/
                /* 指定intent要启动的类 */
                                          intent.setClass(SblistActivity.this, ControlDeviceActivity.class);
                /* 启动一个新的Activity */
                                          SblistActivity.this.startActivity(intent);


                                      }
                                  }
        );
    }


    /* 处理接收到数据UI */
    Handler mHandler_re = new Handler() {
        // 注意：在各个case后面不能做太耗时的操作，否则出现ANR对话框
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1://显示传输的内容
                    String str = (String) msg.obj;
                    //tv_review.setText(str);
                    //Toast.makeText(SblistActivity.this, str, Toast.LENGTH_LONG).show();

                    // ed_reedit.setText(str0+str);
                    Log.v(str, "sblist_case1");
                    break;

                case 2:
                    String str1 = (String) msg.obj;

                    Log.v(str1, "sblist_case2");
                    break;
                case 3:
                    Creat_List();
                    break;


            }
            super.handleMessage(msg);
        }
    };


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                // Object mHelperUtils;
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();

            } else {
                // ApplicationUtil appUtil =  (ApplicationUtil) SblistActivity.this.getApplication();
                //try {
                // appUtil.Close_socket();

                SblistActivity.this.finish();
                int nPid = android.os.Process.myPid();
                android.os.Process.killProcess(nPid);

                //} catch (Exception e1) {
                //  e1.printStackTrace();
                //}
                // finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sblist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.about:
            /* 新建一个Intent对象 */
                Intent intent0 = new Intent(this, AboutActivity.class);
                startActivity(intent0);
                break;
            // return true;
        }
        return super.onOptionsItemSelected(item);

    }
}