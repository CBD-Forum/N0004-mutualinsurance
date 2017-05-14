package com.pingan.demo.model.entity;

/**
 * Created by guolidong752 on 17/5/9.
 */

public class ApproveReq {
    private String id_insurance;//保险编号
    private String id_user;//用户编号
    private String id_claim;//理赔编号
    private String status;//理赔情况
    private double amount;//理赔金额
    private String report_third;//第三方报告
    private String report_visitor;//探望者报告
    private String receipt;//收款凭证
    private String time_approved;//筹款时间，实际理赔时间，用于模拟不同的时间

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

    public String getTime_approved() {
        return time_approved;
    }

    public void setTime_approved(String time_approved) {
        this.time_approved = time_approved;
    }
}
