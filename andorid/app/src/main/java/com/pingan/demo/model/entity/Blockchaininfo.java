package com.pingan.demo.model.entity;

/**
 * Created by guolidong752 on 17/5/9.
 */

public class Blockchaininfo {
    private int height;//区块高度
    private String currentBlockHash;//当前区块的哈希
    private String previousBlockHash;//上一个区块哈希

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getCurrentBlockHash() {
        return currentBlockHash;
    }

    public void setCurrentBlockHash(String currentBlockHash) {
        this.currentBlockHash = currentBlockHash;
    }

    public String getPreviousBlockHash() {
        return previousBlockHash;
    }

    public void setPreviousBlockHash(String previousBlockHash) {
        this.previousBlockHash = previousBlockHash;
    }
}
