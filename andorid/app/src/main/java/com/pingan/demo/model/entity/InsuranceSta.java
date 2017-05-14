package com.pingan.demo.model.entity;

/**
 * Created by guolidong752 on 17/5/9.
 */

public class InsuranceSta {
    private String month;//某月 2017-05
    private int count_new;//本月新增人数
    private int count_quit;//本月减少人数
    private int count_helped;//本月资助人数
    private double amount;//本月资助金额
    private double cost;//本月运营成本
    private double fee;//本月分摊

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getCount_new() {
        return count_new;
    }

    public void setCount_new(int count_new) {
        this.count_new = count_new;
    }

    public int getCount_quit() {
        return count_quit;
    }

    public void setCount_quit(int count_quit) {
        this.count_quit = count_quit;
    }

    public int getCount_helped() {
        return count_helped;
    }

    public void setCount_helped(int count_helped) {
        this.count_helped = count_helped;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getCost() {
        cost = ((int) (cost * 100)) / 100.0;
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getFee() {
        fee = ((int) (fee * 100)) / 100.0;
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }
}
