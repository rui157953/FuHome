package com.mobile.fuhome.app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;


public class EnlistActivity extends Activity {


    DatagramSocket socket;
    private char flag = 0;//socket标志
    private char lo_flag = 0;//请求成功标志
    private String str_name, str_psw;


    private TextView tv_review;
    byte[] getBuf = new byte[1024];//接收缓冲
    DatagramPacket getPacket = new DatagramPacket(getBuf, getBuf.length);//接收数据报
    InetAddress serverAddress;
    int port;
   /* 列表 */

    public String[] sb_id = new String[10];
    public String[] sb_name = new String[10];
    //通过程序生成我们的ListView
    private ListView lv;
    public int Count_List = 0;
    private int j, k;

    char re_flag = 0;

    private long mExitTime;//时间间隔


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enlist);

        //读取设备列表值
        //保存自定义菜单信息
        SharedPreferences userInfo;
        userInfo = getSharedPreferences("sblist", 0);
        String str=userInfo.getString("sb_num", "");
        if(str.equals("") || str== null)
        {
            str="0";
        }
        int num=Integer.parseInt(str);
        Count_List=num;
        //此处是获取按键表的
        for(int i=0;i<num;i++) {
            sb_id[i]=userInfo.getString("sb_id" + i,"");
            sb_name[i]=userInfo.getString("sb_name" + i,"" );
        }

        /*  显示设备列表 */
         Creat_List();


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
        lv.setOnItemClickListener(new

                                          AdapterView.OnItemClickListener() {

                                              @Override
                                              public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                                                  // setTitle("你点击了第" + arg2 + "行");//设置标题栏显示点击的行

                 /* 新建一个Intent对象 */
                                                  Intent intent = new Intent();

                                                 // intent.putExtra("id", id[arg2]);
                                                  intent.putExtra("sb_id", sb_id[arg2]);
                                                  intent.putExtra("sb_name", sb_name[arg2]);
                /* 指定intent要启动的类 */
                                                  intent.setClass(EnlistActivity.this, EncontrolActivity.class);
                /* 启动一个新的Activity */
                                                  EnlistActivity.this.startActivity(intent);


                                              }
                                          }
        );
    }

    //后退按键
    public void onBackPressed() {

        EnlistActivity.this.finish();
        //code......
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.enlist, menu);
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
