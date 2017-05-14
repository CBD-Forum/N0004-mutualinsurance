package com.pingan.demo.model.entity;

/**
 * Created by guolidong752 on 17/5/9.
 */

public class InsuranceReq {
    private String id_insurance;//保险编号
    private String id_user;//用户编号
    private String name;//姓名
    private String idcard;//身份证
    private String mobile;//手机号
    private double amount;//充值金额
    private String medical;//病史确认
    private String id_driver;//驾驶证号
    private String id_driving;//行驶证号
    private String id_didi;//滴滴账号
    private String id_csa;//南航会员号
    private String reserved;//其它信息

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getMedical() {
        return medical;
    }

    public void setMedical(String medical) {
        this.medical = medical;
    }

    public String getId_driver() {
        return id_driver;
    }

    public void setId_driver(String id_driver) {
        this.id_driver = id_driver;
    }

    public String getId_driving() {
        return id_driving;
    }

    public void setId_driving(String id_driving) {
        this.id_driving = id_driving;
    }

    public String getId_didi() {
        return id_didi;
    }

    public void setId_didi(String id_didi) {
        this.id_didi = id_didi;
    }

    public String getId_csa() {
        return id_csa;
    }

    public void setId_csa(String id_csa) {
        this.id_csa = id_csa;
    }

    public String getReserved() {
        return reserved;
    }

    public void setReserved(String reserved) {
        this.reserved = reserved;
    }
}
