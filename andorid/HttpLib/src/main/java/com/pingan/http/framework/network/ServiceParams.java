package com.pingan.http.framework.network;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by iceman on 16/10/27 16:47
 * 邮箱：xubin865@pingan.com.cn
 * 网络请求服务基类
 */

public abstract class ServiceParams<T> {
    /**
     * header部分
     */
    protected Map<String, String> headers = new HashMap<>();
    /**
     * 请求url
     */
    protected String url;
    /**
     * 业务解析类
     */
    protected BusinessParser<T> mBusinessParser;
    /**
     * 返回数据类型
     */
    protected Class<T> responseType;
    /**
     * 是否需要缓存数据
     */
    protected boolean needCacheData = false;
    /**
     * 代表本次网络请求的tag标识
     */
    protected String serviceTag = "default";

    public ServiceParams(String url, Class<T> responsType) {
        this.url = url;
        this.responseType = responsType;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public ServiceParams addHeader(String key, String value) {
        this.headers.put(key, value);
        return this;
    }

    public String getUrl() {
        return url;
    }

    /**
     * 对httpclient做定制.用于某些接口需要对网络请求进行配置的情况
     *
     * @param okHttpClient
     * @return
     */
    public OkHttpClient modifyNetworkExecutor(OkHttpClient okHttpClient) {
        return okHttpClient;
    }

    public abstract Request getRequest(String tag);

    public boolean isNeedCacheData() {
        return needCacheData;
    }

    public ServiceParams setNeedCacheData(boolean needCacheData) {
        this.needCacheData = needCacheData;
        return this;
    }

    public Class<T> getResponseType() {

        return responseType;
    }

    public ServiceParams setResponseType(Class<T> responseType) {
        this.responseType = responseType;

        return this;
    }

    public BusinessParser<T> getBusinessParser() {
        return mBusinessParser;
    }

    public void setBusinessParser(BusinessParser parser) {
        this.mBusinessParser = parser;
    }

    public String getServiceTag() {
        return serviceTag;
    }

    public ServiceParams setServiceTag(String serviceTag) {
        this.serviceTag = serviceTag;
        return this;
    }
}
