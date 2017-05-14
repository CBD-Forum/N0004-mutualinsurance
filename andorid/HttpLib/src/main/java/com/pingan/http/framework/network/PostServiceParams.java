package com.pingan.http.framework.network;

import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by iceman on 16/10/27 16:50
 * 邮箱：xubin865@pingan.com.cn
 * 普通post请求
 */

public class PostServiceParams<G> extends ServiceParams {
    protected HashMap<String, String> postMap;
    protected JSONObject jsonObject;

    public PostServiceParams(String url, Class responsType) {
        super(url, responsType);
    }


    public PostServiceParams setPostMap(HashMap<String, String> postmap) {
        this.postMap = postmap;
        return this;
    }

    public PostServiceParams setJsonObject(JSONObject jsonObject1) {
        this.jsonObject = jsonObject1;
        return this;
    }

    @Override
    public Request getRequest(String tag) {

        if (postMap != null) {
            FormBody.Builder builder = new FormBody.Builder();
            for (Map.Entry<String, String> param : postMap.entrySet()) {
                builder.add(param.getKey(), param.getValue());
                Log.e("guolidong", param.getKey() + "  " + param.getValue());
            }
            return new Request.Builder().url(url).headers(Headers.of(headers)).post(builder.build())
                    .tag(tag).build();
        } else if (jsonObject != null) {
            MediaType mediaType = MediaType.parse("application/json;charset=utf-8");
            RequestBody requestBody = RequestBody.create(mediaType, String.valueOf(jsonObject));
            return new Request.Builder().url(url).headers(Headers.of(headers)).post(requestBody)
                    .tag(tag).build();
        }
        return null;

    }
}
