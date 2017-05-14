package com.pingan.demo.model.entity;

/**
 * Created by guolidong752 on 17/5/9.
 */

public class ClaimRes {
    private Status status;
    private Claim data;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Claim getData() {
        return data;
    }

    public void setData(Claim data) {
        this.data = data;
    }
}
