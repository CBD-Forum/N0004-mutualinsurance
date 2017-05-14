package com.pingan.http.framework.network;

import com.pingan.http.framework.task.BaseTask;
import com.pingan.http.framework.task.ServiceCallback;
import com.pingan.http.framework.task.TagConfig;

/**
 * Created by iceman on 16/11/21.
 */

public class ServiceTask extends BaseTask {
    public ServiceParams serviceParams;
    public DataConfig dataConfig;
    private ServiceCallback callback;


    public ServiceTask(ServiceParams serviceParams) {
        this.serviceParams = serviceParams;
        tagConfig.primaryTag = serviceParams.serviceTag;
        dataConfig = new DataConfig();
    }

    public ServiceTask setUseVirtualData(boolean useVirtualData) {
        dataConfig.useVirtualData = useVirtualData;
        return this;
    }

    public TagConfig getTagConfig() {
        return tagConfig;
    }

    public void setTagConfig(TagConfig tagConfig) {
        this.tagConfig = tagConfig;
    }

    public ServiceParams getServiceParams() {
        return serviceParams;
    }

    public ServiceCallback getCallback() {
        return callback;
    }

    public ServiceTask setCallback(ServiceCallback callback) {
        this.callback = callback;
        return this;
    }
}
