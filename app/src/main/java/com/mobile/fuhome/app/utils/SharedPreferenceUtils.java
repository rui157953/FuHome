package com.mobile.fuhome.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Ryan on 2016/8/3.
 */
public class SharedPreferenceUtils {
    public static final String USER_INFO = "fuhome user info";


    public static void setStringData(Context context,String key,String value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_INFO,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.apply();
    }
    public static void setBooleanData(Context context,String key,boolean value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_INFO,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }
    public static void setIntData(Context context,String key,int value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_INFO,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key,value);
        editor.apply();
    }

    public static String getString(Context context,String key,String defValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_INFO,Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,defValue);
    }
    public static boolean getBoolean(Context context,String key,boolean defValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_INFO,Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, defValue);
    }
    public static int getInt(Context context,String key,int defValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_INFO,Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key,defValue);
    }

}
