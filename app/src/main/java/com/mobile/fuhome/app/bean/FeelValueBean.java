package com.mobile.fuhome.app.bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ryanchen on 16/8/8.
 */
public class FeelValueBean {
    /**
     * dis : f=10
     * log : ok
     * userid : 1020398130
     * id : 9
     * sbtime : 2016-08-03 17:53:48
     * statename : 温度
     * statevalue : W5500
     * feel : 2
     * feelvalue : [{"feelnum":"201","feelvalue1":"27.5","feelvalue2":"","feelvalue3":"","feeltime":"2016-08-03 17:53:58"},{"feelnum":"200","feelvalue1":"29.7","feelvalue2":"","feelvalue3":"","feeltime":"2016-08-03 17:53:19"}]
     */

    private String dis;
    private String log;
    private String userid;
    private String id;
    private String sbtime;
    private String statename;
    private String statevalue;
    private String feel;
    /**
     * feelnum : 201
     * feelvalue1 : 27.5
     * feelvalue2 :
     * feelvalue3 :
     * feeltime : 2016-08-03 17:53:58
     */

    private List<FeelvalueBean> feelvalue;

    public static FeelValueBean objectFromData(String str) {

        return new Gson().fromJson(str, FeelValueBean.class);
    }

    public static FeelValueBean objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), FeelValueBean.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<FeelValueBean> arrayFeelValueBeanFromData(String str) {

        Type listType = new TypeToken<ArrayList<FeelValueBean>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<FeelValueBean> arrayFeelValueBeanFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<FeelValueBean>>() {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSbtime() {
        return sbtime;
    }

    public void setSbtime(String sbtime) {
        this.sbtime = sbtime;
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

    public String getFeel() {
        return feel;
    }

    public void setFeel(String feel) {
        this.feel = feel;
    }

    public List<FeelvalueBean> getFeelvalue() {
        return feelvalue;
    }

    public void setFeelvalue(List<FeelvalueBean> feelvalue) {
        this.feelvalue = feelvalue;
    }

    public static class FeelvalueBean {
        private String feelnum;
        private String feelvalue1;
        private String feelvalue2;
        private String feelvalue3;
        private String feeltime;

        public static FeelvalueBean objectFromData(String str) {

            return new Gson().fromJson(str, FeelvalueBean.class);
        }

        public static FeelvalueBean objectFromData(String str, String key) {

            try {
                JSONObject jsonObject = new JSONObject(str);

                return new Gson().fromJson(jsonObject.getString(str), FeelvalueBean.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        public static List<FeelvalueBean> arrayFeelvalueBeanFromData(String str) {

            Type listType = new TypeToken<ArrayList<FeelvalueBean>>() {
            }.getType();

            return new Gson().fromJson(str, listType);
        }

        public static List<FeelvalueBean> arrayFeelvalueBeanFromData(String str, String key) {

            try {
                JSONObject jsonObject = new JSONObject(str);
                Type listType = new TypeToken<ArrayList<FeelvalueBean>>() {
                }.getType();

                return new Gson().fromJson(jsonObject.getString(str), listType);

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

        public String getFeelvalue1() {
            return feelvalue1;
        }

        public void setFeelvalue1(String feelvalue1) {
            this.feelvalue1 = feelvalue1;
        }

        public String getFeelvalue2() {
            return feelvalue2;
        }

        public void setFeelvalue2(String feelvalue2) {
            this.feelvalue2 = feelvalue2;
        }

        public String getFeelvalue3() {
            return feelvalue3;
        }

        public void setFeelvalue3(String feelvalue3) {
            this.feelvalue3 = feelvalue3;
        }

        public String getFeeltime() {
            return feeltime;
        }

        public void setFeeltime(String feeltime) {
            this.feeltime = feeltime;
        }
    }
    /*


{"dis":"f=10",
			"log":"ok",
			"userid":"1020398130",
			"id":"9","sbtime":"2016-08-03 17:53:48",
			"statename":"温度",
			"statevalue":"W5500","feel":"2",
			"feelvalue":[{"feelnum":"201",
					  "feelvalue1":"27.5",
					  "feelvalue2":"",
					  "feelvalue3":"",
					  "feeltime":"2016-08-03 17:53:58"},{"feelnum":"200",
					  "feelvalue1":"29.7",
					  "feelvalue2":"",
					  "feelvalue3":"",
					  "feeltime":"2016-08-03 17:53:19"}]} */



}
