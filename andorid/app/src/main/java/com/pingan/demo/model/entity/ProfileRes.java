package com.pingan.demo.model.entity;

/**
 * Created by guolidong752 on 17/5/9.
 */

public class ProfileRes {
    private Status status;
    private Profile data;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Profile getData() {
        return data;
    }

    public void setData(Profile data) {
        this.data = data;
    }
}
