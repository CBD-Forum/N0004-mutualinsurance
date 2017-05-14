package com.pingan.demo;

/**
 * Created by guolidong752 on 17/5/12.
 */

public class NumUtil {
    public static int changeToInt(String amount_max) {
        int value = 0;
        try {
            value = Integer.valueOf(amount_max);
        } catch (Exception e) {
            return 0;
        }
        return value;

    }
}
