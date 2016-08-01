package com.mobile.fuhome.app.bean;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryan on 2016/8/1.
 */
public class FeelBean {

    /**
     * dis : f=6
     * log : ok
     * userid : 1020398130
     * id : 1176
     * com : 2
     * sbcom : [{"comname":"打开全部","comstring":"11"},{"comname":"关闭全部","comstring":"00"}]
     * feel : 2
     * sbfeel : [{"feelnum":"124","feelname":"湿度测量","feelunit":"RH","feelstyle":"1"},{"feelnum":"123","feelname":"温度测量","feelunit":"度","feelstyle":"1"}]
     */

    private String dis;
    private String log;
    private String userid;
    private String id;
    private String com;
    private String feel;
    /**
     * comname : 打开全部
     * comstring : 11
     */

    private List<SbcomBean> sbcom;
    /**
     * feelnum : 124
     * feelname : 湿度测量
     * feelunit : RH
     * feelstyle : 1
     */

    private List<SbfeelBean> sbfeel;

    public static FeelBean objectFromData(String str) {

        return new com.google.gson.Gson().fromJson(str, FeelBean.class);
    }

    public static FeelBean objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new com.google.gson.Gson().fromJson(jsonObject.getString(str), FeelBean.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<FeelBean> arrayFeelBeanFromData(String str) {

        Type listType = new com.google.gson.reflect.TypeToken<ArrayList<FeelBean>>() {
        }.getType();

        return new com.google.gson.Gson().fromJson(str, listType);
    }

    public static List<FeelBean> arrayFeelBeanFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new com.google.gson.reflect.TypeToken<ArrayList<FeelBean>>() {
            }.getType();

            return new com.google.gson.Gson().fromJson(jsonObject.getString(str), listType);

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCom() {
        return com;
    }

    public void setCom(String com) {
        this.com = com;
    }

    public String getFeel() {
        return feel;
    }

    public void setFeel(String feel) {
        this.feel = feel;
    }

    public List<SbcomBean> getSbcom() {
        return sbcom;
    }

    public void setSbcom(List<SbcomBean> sbcom) {
        this.sbcom = sbcom;
    }

    public List<SbfeelBean> getSbfeel() {
        return sbfeel;
    }

    public void setSbfeel(List<SbfeelBean> sbfeel) {
        this.sbfeel = sbfeel;
    }

    public static class SbcomBean {
        private String comname;
        private String comstring;

        public static SbcomBean objectFromData(String str) {

            return new com.google.gson.Gson().fromJson(str, SbcomBean.class);
        }

        public static SbcomBean objectFromData(String str, String key) {

            try {
                JSONObject jsonObject = new JSONObject(str);

                return new com.google.gson.Gson().fromJson(jsonObject.getString(str), SbcomBean.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        public static List<SbcomBean> arraySbcomBeanFromData(String str) {

            Type listType = new com.google.gson.reflect.TypeToken<ArrayList<SbcomBean>>() {
            }.getType();

            return new com.google.gson.Gson().fromJson(str, listType);
        }

        public static List<SbcomBean> arraySbcomBeanFromData(String str, String key) {

            try {
                JSONObject jsonObject = new JSONObject(str);
                Type listType = new com.google.gson.reflect.TypeToken<ArrayList<SbcomBean>>() {
                }.getType();

                return new com.google.gson.Gson().fromJson(jsonObject.getString(str), listType);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return new ArrayList();


        }

        public String getComname() {
            return comname;
        }

        public void setComname(String comname) {
            this.comname = comname;
        }

        public String getComstring() {
            return comstring;
        }

        public void setComstring(String comstring) {
            this.comstring = comstring;
        }
    }

    public static class SbfeelBean {
        private String feelnum;
        private String feelname;
        private String feelunit;
        private String feelstyle;

        public static SbfeelBean objectFromData(String str) {

            return new com.google.gson.Gson().fromJson(str, SbfeelBean.class);
        }

        public static SbfeelBean objectFromData(String str, String key) {

            try {
                JSONObject jsonObject = new JSONObject(str);

                return new com.google.gson.Gson().fromJson(jsonObject.getString(str), SbfeelBean.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        public static List<SbfeelBean> arraySbfeelBeanFromData(String str) {

            Type listType = new com.google.gson.reflect.TypeToken<ArrayList<SbfeelBean>>() {
            }.getType();

            return new com.google.gson.Gson().fromJson(str, listType);
        }

        public static List<SbfeelBean> arraySbfeelBeanFromData(String str, String key) {

            try {
                JSONObject jsonObject = new JSONObject(str);
                Type listType = new com.google.gson.reflect.TypeToken<ArrayList<SbfeelBean>>() {
                }.getType();

                return new com.google.gson.Gson().fromJson(jsonObject.getString(str), listType);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return new ArrayList();


        }

        public String getFeelnum() {
            return feelnum;
        }

        public void setFeelnum(String feelnum) {
            this.feelnum = feelnum;
        }

        public String getFeelname() {
            return feelname;
        }

        public void setFeelname(String feelname) {
            this.feelname = feelname;
        }

        public String getFeelunit() {
            return feelunit;
        }

        public void setFeelunit(String feelunit) {
            this.feelunit = feelunit;
        }

        public String getFeelstyle() {
            return feelstyle;
        }

        public void setFeelstyle(String feelstyle) {
            this.feelstyle = feelstyle;
        }
    }
}
