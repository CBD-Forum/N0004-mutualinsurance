package com.pingan.demo.model.entity;

import java.util.List;

/**
 * Created by guolidong752 on 17/5/9.
 */

public class InsurancesRes {
    private Status status;
    private List<Insurance> data;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Insurance> getData() {
        return data;
    }

    public void setData(List<Insurance> data) {
        this.data = data;
    }
}
