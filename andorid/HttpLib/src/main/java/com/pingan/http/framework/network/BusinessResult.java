package com.pingan.http.framework.network;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.pingan.http.framework.FrameworkConstants;
import com.pingan.http.framework.model.response.BaseResponse;

/**
 * Created by iceman on 15/5/14.
 * 业务数据解析时使用的结果封装
 * 非网络错误会在这里标记出来
 */
public class BusinessResult {
    public boolean isSuccess = true;
    public String errorString = "";

    public BusinessResult() {

    }

    public BusinessResult(boolean success) {
        isSuccess = success;
    }


    public BusinessResult(String s) {
        if (!TextUtils.isEmpty(s)) {
            try {
                BaseResponse response = new Gson().fromJson(s, BaseResponse.class);
                if (response == null) {
                    isSuccess = false;
                    return;
                }
                if (response.status.code == FrameworkConstants.CODE_BUSINESS_SUCCESS
                        || response.status.code == FrameworkConstants.CODE_CMS_SUCCESS) {
                    errorString = "";
                } else {
                    isSuccess = false;
                    errorString = response.status.message;
                    if (response.status.code == FrameworkConstants.CODE_BUSINESS_FAIL && !TextUtils
                            .isEmpty(response.status.message)) {
                        if (response.status.message.equals("请先在<个人信息>中绑定身份证号")) {

                        }
                    }
                }
            } catch (JsonSyntaxException e) {
            }
        }
    }

    public BusinessResult(BaseResponse response) {
        if (response == null) {
            isSuccess = false;
            return;
        }
        if ((response.status.code == FrameworkConstants.CODE_BUSINESS_SUCCESS) || (
                response.status.code == FrameworkConstants.CODE_CMS_SUCCESS)) {
            errorString = "";
        } else {
            isSuccess = false;
            errorString = response.status.message;
            if (response.status.code == FrameworkConstants.CODE_BUSINESS_FAIL && !TextUtils
                    .isEmpty(response.status.message)) {
                if (response.status.message.equals("请先在<个人信息>中绑定身份证号")) {

                }
            }
        }
    }
}
