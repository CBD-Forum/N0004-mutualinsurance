package com.pingan.http.framework.task;

/**
 * Created by iceman on 15/6/22.
 */
public interface ServiceCallback {
    void onTaskStart(String serverTag);

    void onTaskSuccess(String serverTag);

    void onTaskFail(NetwrokTaskError error, String serverTag);
}
