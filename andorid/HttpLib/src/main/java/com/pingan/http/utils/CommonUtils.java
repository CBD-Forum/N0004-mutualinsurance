package com.pingan.http.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.pingan.http.framework.BaseConfig;

public class CommonUtils {
    /**
     * 判断字符串是否有值，如果为null或者是空字符串或者只有空格或者为"null"字符串，则返回true，否则则返回false
     */
    public static boolean isEmpty(String value) {
        if (value != null && !"".equalsIgnoreCase(value.trim()) && !"null"
                .equalsIgnoreCase(value.trim())) {
            return false;
        } else {
            return true;
        }
    }


    /**
     * 获取设备的IMEI
     */
    public static String getIMEI() {
        Context context = BaseConfig.getAppContext();
        if (null == context) {
            return "";
        }
        String imei = "";
        try {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            imei = tm.getDeviceId();
        } catch (Exception e) {
        }
        return imei;
    }
}