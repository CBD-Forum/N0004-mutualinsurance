package com.pingan.demo.model.entity;

import java.util.List;

/**
 * Created by guolidong752 on 17/5/9.
 */

public class Profile {
    private String id;//用户编号
    private double balance;//充值余额
    private int count_helped;//资助人数
    private double fee;//分摊金额
    private List<Insurance> insurances;//保险列表

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getCount_helped() {
        return count_helped;
    }

    public void setCount_helped(int count_helped) {
        this.count_helped = count_helped;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public List<Insurance> getInsurances() {
        return insurances;
    }

    public void setInsurances(List<Insurance> insurances) {
        this.insurances = insurances;
    }
}
