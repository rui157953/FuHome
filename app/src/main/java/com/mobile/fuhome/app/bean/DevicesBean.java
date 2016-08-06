package com.mobile.fuhome.app.bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryan on 2016/8/6.
 */
public class DevicesBean {

    /**
     * dis : f=5
     * log : ok
     * userid : 1020398130
     * c : 6
     * sb : [{"shuxing":"00a","id":"1188","sbid":"1287372025","sbname":"","sbtime":"1970-01-01 08:00:00","sbadd":"","statename":"","statevalue":"","imgurl":"1.jpg"},{"shuxing":"00a","id":"1176","sbid":"1287371971","sbname":"WIFI水闸控制器-ESP8266","sbtime":"2016-08-02 16:16:52","sbadd":"深圳市","statename":"开机时间","statevalue":"00:02:34:03","imgurl":"1176.jpg"},{"shuxing":"00a","id":"1175","sbid":"1287371970","sbname":"WIFI房间温湿度-ESP8266","sbtime":"2016-08-05 07:39:33","sbadd":"深圳市","statename":"","statevalue":"00:00:00:15","imgurl":"1175.jpg"},{"shuxing":"00a","id":"438","sbid":"1287370017","sbname":"ESP8266 wifi Arduino编程","sbtime":"2016-01-20 20:09:21","sbadd":"深圳市","statename":"开机时长S","statevalue":"00:00:00:02","imgurl":"438.jpg"},{"shuxing":"00a","id":"32","sbid":"1287369210","sbname":"WiFI两路继电器","sbtime":"2016-05-21 01:09:49","sbadd":"上海市","statename":"温度","statevalue":"nihao","imgurl":"32.png"},{"shuxing":"00a","id":"9","sbid":"1287369152","sbname":"深圳电饭锅","sbtime":"2016-08-03 17:53:48","sbadd":"深圳市","statename":"温度","statevalue":"W5500","imgurl":"9.jpg"}]
     */

    private String dis;
    private String log;
    private String userid;
    private String c;
    /**
     * shuxing : 00a
     * id : 1188
     * sbid : 1287372025
     * sbname :
     * sbtime : 1970-01-01 08:00:00
     * sbadd :
     * statename :
     * statevalue :
     * imgurl : 1.jpg
     */

    private List<SbBean> sb;

    public static DevicesBean objectFromData(String str) {

        return new Gson().fromJson(str, DevicesBean.class);
    }

    public static DevicesBean objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), DevicesBean.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<DevicesBean> arrayDevicesBeanFromData(String str) {

        Type listType = new TypeToken<ArrayList<DevicesBean>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<DevicesBean> arrayDevicesBeanFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<DevicesBean>>() {
            }.getType();

            return new Gson().fromJson(jsonObject.getString(str), listType);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList();


    }

    public String getDis() {
        return dis;
    }

    public void setDis(String dis) {
        this.dis = dis;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public List<SbBean> getSb() {
        return sb;
    }

    public void setSb(List<SbBean> sb) {
        this.sb = sb;
    }

    public static class SbBean {
        private String shuxing;
        private String id;
        private String sbid;
        private String sbname;
        private String sbtime;
        private String sbadd;
        private String statename;
        private String statevalue;
        private String imgurl;

        public static SbBean objectFromData(String str) {

            return new Gson().fromJson(str, SbBean.class);
        }

        public static SbBean objectFromData(String str, String key) {

            try {
                JSONObject jsonObject = new JSONObject(str);

                return new Gson().fromJson(jsonObject.getString(str), SbBean.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        public static List<SbBean> arraySbBeanFromData(String str) {

            Type listType = new TypeToken<ArrayList<SbBean>>() {
            }.getType();

            return new Gson().fromJson(str, listType);
        }

        public static List<SbBean> arraySbBeanFromData(String str, String key) {

            try {
                JSONObject jsonObject = new JSONObject(str);
                Type listType = new TypeToken<ArrayList<SbBean>>() {
                }.getType();

                return new Gson().fromJson(jsonObject.getString(str), listType);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return new ArrayList();


        }

        public String getShuxing() {
            return shuxing;
        }

        public void setShuxing(String shuxing) {
            this.shuxing = shuxing;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSbid() {
            return sbid;
        }

        public void setSbid(String sbid) {
            this.sbid = sbid;
        }

        public String getSbname() {
            return sbname;
        }

        public void setSbname(String sbname) {
            this.sbname = sbname;
        }

        public String getSbtime() {
            return sbtime;
        }

        public void setSbtime(String sbtime) {
            this.sbtime = sbtime;
        }

        public String getSbadd() {
            return sbadd;
        }

        public void setSbadd(String sbadd) {
            this.sbadd = sbadd;
        }

        public String getStatename() {
            return statename;
        }

        public void setStatename(String statename) {
            this.statename = statename;
        }

        public String getStatevalue() {
            return statevalue;
        }

        public void setStatevalue(String statevalue) {
            this.statevalue = statevalue;
        }

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }
    }
}
