package com.pingan.demo.model.entity;

/**
 * Created by guolidong752 on 17/5/9.
 */

public class Claim {
    private String id_insurance;//保险编号
    private String id_user;//用户编号
    private String id_claim;//理赔编号
    private String name;//理赔人姓名
    private String mobile;//联系方式
    private String status;//理赔进度
    private double amount;//理赔金额
    private String report_third;//第三方报告
    private String report_visitor;//探望者报告
    private String receipt;//收款凭证
    private String time_claimed;//公示时间，申请理赔时间
    private String time_approved;//筹款时间，实际理赔时间

    public String getId_insurance() {
        return id_insurance;
    }

    public void setId_insurance(String id_insurance) {
        this.id_insurance = id_insurance;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getId_claim() {
        return id_claim;
    }

    public void setId_claim(String id_claim) {
        this.id_claim = id_claim;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getReport_third() {
        return report_third;
    }

    public void setReport_third(String report_third) {
        this.report_third = report_third;
    }

    public String getReport_visitor() {
        return report_visitor;
    }

    public void setReport_visitor(String report_visitor) {
        this.report_visitor = report_visitor;
    }

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }

    public String getTime_claimed() {
        return time_claimed;
    }

    public void setTime_claimed(String time_claimed) {
        this.time_claimed = time_claimed;
    }

    public String getTime_approved() {
        return time_approved;
    }

    public void setTime_approved(String time_approved) {
        this.time_approved = time_approved;
    }
}
