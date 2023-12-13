package com.poly.hopa.models.ServerRequest;

import com.google.gson.annotations.SerializedName;

import retrofit2.http.Field;

public class ServerReqChangePass {
    private String oldPassWord;
    private String newPassWord;

    public String getOldPassWord() {
        return oldPassWord;
    }

    public void setOldPassWord(String oldPassWord) {
        this.oldPassWord = oldPassWord;
    }

    public String getNewPassWord() {
        return newPassWord;
    }

    public void setNewPassWord(String newPassWord) {
        this.newPassWord = newPassWord;
    }
}
