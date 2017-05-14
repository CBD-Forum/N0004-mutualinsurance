package com.pingan.demo;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;



public class SpUtils {
    public static final String SHAREDPREFERENCES_NAME = "my_profile";

    private static SpUtils instance;

    private static Application application;

    private SpUtils(Application application) {
        this.application = application;
    }

    public static SpUtils getInstance() {
        return instance;
    }

    public static void init(Application application) {
        instance = new SpUtils(application);
    }

    private SharedPreferences getPreference() {
        SharedPreferences sp = application
                .getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
        return sp;
    }

    private SharedPreferences.Editor getEditor() {
        return getPreference().edit();
    }

    /**
     * 保存数据的方法，无论保存的是哪种类型，都要转化为string类型加密之后再保存
     *
     * @param key
     * @param object
     */
    public String setParam(String key, Object object) {
        if (object==null){
            return null;
        }
        setString(key, object.toString());
        return key;
    }

    /**
     * 获取保存的数据
     *
     * @param key
     * @param defaultObject
     * @return
     */
    public Object getParam(String key, Object defaultObject) {
        String result = String.valueOf(defaultObject);

        if (getPreference().contains(key)) {
            result = AESUtils.decode(application, getPreference().getString(key, ""));
        }
        String type = defaultObject.getClass().getSimpleName();
        Object value = null;
        if ("String".equals(type)) {
            value = result;
        } else if ("Integer".equals(type)) {
            value = Integer.valueOf(result);
        } else if ("Boolean".equals(type)) {
            value = Boolean.valueOf(result);
        } else if ("Float".equals(type)) {
            value = Float.valueOf(result);
        } else if ("Long".equals(type)) {
            value = Long.valueOf(result);
        }
        return value;
    }

    private void setString(String keyName, Object value) {
        SharedPreferences.Editor editor = getEditor();
        String encodeString = AESUtils.encode(application, String.valueOf(value));
        editor.putString(keyName, encodeString);
        editor.commit();
    }

}
