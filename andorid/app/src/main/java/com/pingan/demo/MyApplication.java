package com.pingan.demo;

import android.app.Application;
import android.os.Handler;

import com.pingan.http.framework.BaseConfig;

/**
 * Created by guolidong752 on 16/1/1.
 */

public class MyApplication extends Application {
    private static MyApplication context;
    private Handler mHandler;

    public static MyApplication getAppContext() {
        return context;
    }

    public Handler getHandler() {
        return mHandler;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        BaseConfig.init(this);
        SpUtils.init(this);
        context = this;
        mHandler = new Handler(getMainLooper());
    }
}