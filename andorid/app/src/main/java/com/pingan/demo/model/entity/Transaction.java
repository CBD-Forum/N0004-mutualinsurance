package com.pingan.demo.model.entity;

/**
 * Created by guolidong752 on 17/5/9.
 */

public class Transaction {
    private int type;
    private String chaincodeID;//chaincode编号
    private String payload;//有效载荷
    private String uuid;
    private Timestamp timestamp;
    private String cert;
    private String signature;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getChaincodeID() {
        return chaincodeID;
    }

    public void setChaincodeID(String chaincodeID) {
        this.chaincodeID = chaincodeID;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getCert() {
        return cert;
    }

    public void setCert(String cert) {
        this.cert = cert;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
