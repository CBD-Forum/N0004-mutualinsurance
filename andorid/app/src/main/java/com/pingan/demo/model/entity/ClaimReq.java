package com.pingan.demo.model.entity;

/**
 * Created by guolidong752 on 17/5/9.
 */

public class ClaimReq {
    private String id_insurance;//保险编号
    private String id_user;//用户编号
    private String name;//用户姓名
    private String mobile;//联系方式
    private String cardnum;//银行卡号
    private String time_claimed;//公示时间，申请理赔时间，用于模拟不同的时间

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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCardnum() {
        return cardnum;
    }

    public void setCardnum(String cardnum) {
        this.cardnum = cardnum;
    }

    public String getTime_claimed() {
        return time_claimed;
    }

    public void setTime_claimed(String time_claimed) {
        this.time_claimed = time_claimed;
    }
}
