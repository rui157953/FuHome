package com.mobile.fuhome.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.mobile.fuhome.app.adapter.UniversalBaseAdapter;
import com.mobile.fuhome.app.adapter.ViewHolder;
import com.mobile.fuhome.app.application.BaseActivity;
import com.mobile.fuhome.app.application.Constants;
import com.mobile.fuhome.app.bean.DevicesBean;
import com.mobile.fuhome.app.service.NetService;
import com.mobile.fuhome.app.utils.HttpUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class DevicesListActivity extends BaseActivity implements HttpUtils.ResultCallback {

    private static final String TAG = "ryan";
    private SwipeRefreshLayout mRefreshLayout;
    private View mView;
    private Button mAddDeviceBtn;
    private ListView mListView;
    private List<Map<String,String>> mDeviceList;
    private UniversalBaseAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices_list);
        Intent startIntent = new Intent(this, NetService.class);
        startService(startIntent);
        initView();
        initModle();
        loadData();
    }

    private void initView() {
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.devices_list_refresh);
        mView = findViewById(R.id.empty_list_view);
        mAddDeviceBtn = (Button) findViewById(R.id.empty_list_add_btn);
        mListView = (ListView) findViewById(R.id.devices_list_lv);

        mAddDeviceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                Map<String, String> map = mDeviceList.get(position);
                bundle.putString("sb_staname",map.get("statename"));
                bundle.putString("sb_stavalue",map.get("statevalue"));
                bundle.putString("sb_name",map.get("name"));
                bundle.putString("sb_id",map.get("sbid"));
                bundle.putString("id",map.get("id"));
                jumpToActivity(ControlDeviceActivity.class,bundle,false,0);
            }
        });
    }

    private void initModle() {
        mDeviceList = new ArrayList<>();
        mAdapter = new UniversalBaseAdapter<Map<String,String>>(this,mDeviceList,R.layout.device_list_item) {
            @Override
            protected void convert(ViewHolder viewHolder, Map<String, String> item) {
                viewHolder.setText(R.id.list_item_name_tv,item.get("name"));
                viewHolder.setText(R.id.list_item_time_tv,item.get("time"));
                viewHolder.setText(R.id.list_item_add_tv,item.get("add"));
                viewHolder.setText(R.id.list_item_state_tv,item.get("statevalue"));
                viewHolder.setImageByUrl(R.id.list_item_iv,item.get("imgurl"));
            }
        };
        mListView.setAdapter(mAdapter);
        mListView.setEmptyView(mView);

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
    }

    private void loadData() {
        Map<String,String>map = new HashMap<>();
        map.put("f","5");
        HttpUtils.addCommValue(this,map);
        HttpUtils.requestPost(Constants.HOST,map,this);
    }
    @Override
    public void onResponse(Call call, String response) {
        if (mRefreshLayout !=null){
            mRefreshLayout.setRefreshing(false);
        }
        DevicesBean devicesBean = DevicesBean.objectFromData(response);
        String log = devicesBean.getLog();
        if (TextUtils.equals(log,"ok")){
            mDeviceList.clear();
            List<DevicesBean.SbBean> sb = devicesBean.getSb();
            for (DevicesBean.SbBean sbBean : sb) {
                Map<String,String>map = new HashMap<>();
                map.put("id",sbBean.getId());
                String imgurl = Constants.IMGURL+sbBean.getImgurl();
                map.put("imgurl",imgurl);
                map.put("add",sbBean.getSbadd());
                map.put("sbid",sbBean.getSbid());
                map.put("name",sbBean.getSbname());
                map.put("time",sbBean.getSbtime());
                map.put("shuxing",sbBean.getShuxing());
                map.put("statename",sbBean.getStatename());
                map.put("statevalue",sbBean.getStatevalue());
                mDeviceList.add(map);
            }
            refreshUI();
        }else {
            Toast.makeText(DevicesListActivity.this, "获取设备列表失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void refreshUI(){
        mRefreshLayout.setRefreshing(false);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFailure(Call call, IOException e) {
        if (mRefreshLayout !=null){
            mRefreshLayout.setRefreshing(false);
        }
        Toast.makeText(DevicesListActivity.this, "获取设备列表失败", Toast.LENGTH_SHORT).show();
    }
}
