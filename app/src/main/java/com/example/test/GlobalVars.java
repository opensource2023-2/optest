package com.example.test;

import android.app.Application;

public class GlobalVars extends Application {
    private String userID;

    public String getUserID(){
        return userID;
    }

    public void setUserID(String userID){
        this.userID = userID;
    }
}
