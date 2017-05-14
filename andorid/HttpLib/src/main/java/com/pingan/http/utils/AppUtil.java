package com.pingan.http.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class AppUtil {

    /**
     * 获取版本号
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context)//获取版本号
    {
        try {
            PackageInfo pi = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "未知版本号";
        }
    }

    /**
     * 获取渠道编号
     * 外部公共渠道	A01	AppStore
     * A02	小米市场
     * A03	360市场
     * A04	百度市场
     * A05	腾讯应用宝
     * 外部推广渠道	B01	多盟
     * 内部渠道	    I01	平安科技
     *
     * @param context
     * @return
     */
    public static String getChannelCode(Context context) {

        String code = getMetaData(context, "TD_CHANNEL_ID");

        if (code != null) {

            return code;
        }
        return "I01";

    }

    /**
     * 根据key（meta-data对应name）获取AndroidManifest中对应的value
     *
     * @param context 上下文对象
     * @param key     获取value对应的key
     * @return
     */
    private static String getMetaData(Context context, String key) {

        try {

            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(

                    context.getPackageName(), PackageManager.GET_META_DATA);

            Object value = ai.metaData.get(key);

            if (value != null) {

                return value.toString();

            }

        } catch (Exception e) {
        }

        return null;

    }
}
