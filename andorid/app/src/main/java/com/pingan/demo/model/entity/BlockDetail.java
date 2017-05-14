package com.pingan.demo.model.entity;

/**
 * Created by guolidong752 on 17/5/14.
 */

public class BlockDetail {
    private int height;
    private String previousBlockHash;
    private String trans_uuid;

    public BlockDetail(int cusorHeight, String previousBlockHash, String trans_uuid) {
        height = cusorHeight;
        this.previousBlockHash = previousBlockHash;
        this.trans_uuid = trans_uuid;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getPreviousBlockHash() {
        return previousBlockHash;
    }

    public void setPreviousBlockHash(String previousBlockHash) {
        this.previousBlockHash = previousBlockHash;
    }

    public String getTrans_uuid() {
        return trans_uuid;
    }

    public void setTrans_uuid(String trans_uuid) {
        this.trans_uuid = trans_uuid;
    }
}
