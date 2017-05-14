package com.pingan.demo.model.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by guolidong752 on 17/5/9.
 */

public class Insurance implements Serializable {
    private String id;//保险编号
    private String name;//保险名称
    private String time_founded;//成立时间
    private List<String> description;//保险描述，10个字符串
    private String issuer;//发起人
    private String source;//资金来源
    private String amount_max;//最大保额
    private String count_bought;//参与人数，投保人数
    private String count_helped;//资助人数
    private String balance;//资金池余额
    private String amount;//总筹款金额
    private String fee;//每月分摊
    private boolean bought;//是否购买
    private List<Claim> claims;//理赔列表
    private List<InsuranceSta> statistics;//统计列表

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime_founded() {
        return time_founded;
    }

    public void setTime_founded(String time_founded) {
        this.time_founded = time_founded;
    }

    public List<String> getDescription() {
        return description;
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isBought() {
        return bought;
    }

    public void setBought(boolean bought) {
        this.bought = bought;
    }

    public List<Claim> getClaims() {
        return claims;
    }

    public void setClaims(List<Claim> claims) {
        this.claims = claims;
    }

    public List<InsuranceSta> getStatistics() {
        return statistics;
    }

    public void setStatistics(List<InsuranceSta> statistics) {
        this.statistics = statistics;
    }

    public String getAmount_max() {
        return amount_max;
    }

    public void setAmount_max(String amount_max) {
        this.amount_max = amount_max;
    }

    public String getCount_bought() {
        return count_bought;
    }

    public void setCount_bought(String count_bought) {
        this.count_bought = count_bought;
    }

    public String getCount_helped() {
        return count_helped;
    }

    public void setCount_helped(String count_helped) {
        this.count_helped = count_helped;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFee() {
        if (fee != null && fee.contains(".")) {
            int floatIndex = fee.indexOf(".");
            if (fee.length() >= floatIndex + 3) {
                fee = fee.substring(0, floatIndex + 3);
            }
        }
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }
}
