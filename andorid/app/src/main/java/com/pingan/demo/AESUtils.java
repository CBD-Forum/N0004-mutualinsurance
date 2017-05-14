package com.pingan.demo;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by guolidong752 on 16/3/9.
 */

public class AESUtils {
    private static final String TAG = "AESUtils";
    private static final String TYPE = "AES/ECB/PKCS5Padding";
    private static Cipher cipher;
    private static boolean isInited = false;
    private static SecretKeySpec finalKey;

    /**
     * 加密
     */
    public static String encode(Context context,String content) {
        //加密之后的字节数组,转成16进制的字符串形式输出
        return doJob(context,content, true);
    }

    /**
     * 解密
     */
    public static String decode(Context context,String content) {
        //解密之前,先将输入的字符串按照16进制转成二进制的字节数组,作为待解密的内容输入
        return doJob(context,content, false);
    }


    private static void init(Context context) {

        try {
            finalKey = getKey(context);
            // 生成一个实现指定转换的 Cipher 对象。
            cipher = Cipher.getInstance(TYPE);
        } catch (Exception e) {
        }
        //标识已经初始化过了的字段
        isInited = true;
    }

    private static SecretKeySpec getKey(Context context) {
        StringBuffer source = new StringBuffer();
        PackageManager packageManager = context.getApplicationContext()
                .getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getApplicationContext().getPackageName(), 0);
            source.append(String.valueOf(packageInfo.firstInstallTime));
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            source.append(applicationInfo.packageName);
        } catch (Exception e) {
        }

        TelephonyManager tm = (TelephonyManager) context.getApplicationContext()
                .getSystemService(Context.TELEPHONY_SERVICE);
        source.append(tm.getDeviceId());
        return new SecretKeySpec(getBytes(source), "AES");
    }

    private static byte[] getBytes(StringBuffer source) {
        String last = source.append(getTotalInternalMemorySize()).append(getTotalMemorySize())
                .toString();
        int a = last.length() % 16;
        int b = last.length() / 16;
        byte[] keyBytes = new byte[16];
        for (int i = 0; i < 16; i++) {
            keyBytes[i] = last.getBytes()[i * b + a / 2];
        }
        return keyBytes;
    }


    private static String doJob(Context context,String content, boolean type) {
        if (!isInited) {
            init(context);
        }

        if (content == null || content.equals("")) {
            return null;
        }

        byte[] result = new byte[0];
        try {
            int mode = type ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE;
            cipher.init(mode, finalKey);
            byte[] byteContent;
            if (type) {
                byteContent = content.getBytes("utf-8");
            } else {
                byteContent = parseHexStr2Byte(content);
            }
            result = cipher.doFinal(byteContent);
        } catch (Exception e) {
        }
        if (type) {
            return parseByte2HexStr(result);
        } else {
            return new String(result);
        }
    }


    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr
     * @return
     */
    private static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /**
     * 获取系统总内存
     *
     * @return 总内存大单位为B。
     */
    private static long getTotalMemorySize() {
        String dir = "/proc/meminfo";
        FileReader fr = null;
        BufferedReader br = null;
        String subMemoryLine = null;
        try {
            fr = new FileReader(dir);
            br = new BufferedReader(fr, 2048);
            String memoryLine = br.readLine();
            subMemoryLine = memoryLine.substring(memoryLine.indexOf("MemTotal:"));
        } catch (IOException e) {
        } finally {
            if (fr != null && br != null) {
                try {
                    fr.close();
                    br.close();
                } catch (IOException ie) {
                }
            }
        }
        return Integer.parseInt(subMemoryLine != null ? subMemoryLine.replaceAll("\\D+", "") : "0")
                * 1024l;
    }

    /**
     * 获取手机内部总的存储空间
     *
     * @return
     */

    private static long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }
}