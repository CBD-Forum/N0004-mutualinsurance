package com.pingan.http.framework.network;

import okhttp3.Headers;
import okhttp3.Request;

/**
 * Created by iceman on 16/10/27 16:49
 * 邮箱：xubin865@pingan.com.cn
 * 普通get请求
 */

public class GetServiceParams<T> extends ServiceParams<T> {


    public GetServiceParams(String url, Class<T> responsType) {
        super(url, responsType);
    }

    @Override
    public Request getRequest(String tag) {
        return new Request.Builder().url(getUrl()).headers(Headers.of(headers)).tag(tag).build();
    }
}
