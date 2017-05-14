package com.pingan.demo.model.entity;

/**
 * Created by guolidong752 on 17/5/9.
 */

public class Peer {
    private ID ID;
    private String address;
    private int type;
    private String pkiID;

    public Peer.ID getID() {
        return ID;
    }

    public void setID(Peer.ID ID) {
        this.ID = ID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPkiID() {
        return pkiID;
    }

    public void setPkiID(String pkiID) {
        this.pkiID = pkiID;
    }

    public static class ID {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
