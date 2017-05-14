package com.pingan.demo.controller;

import com.pingan.demo.model.entity.Profile;

/**
 * Created by guolidong752 on 17/5/8.
 */

public class UserController {
    private static UserController instance;

    private String user;
    private String pass;
    private Profile profile;

    private UserController() {
    }

    public static UserController getInstance() {
        if (instance == null) {
            instance = new UserController();
        }
        return instance;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }


    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
