package com.prm392.estoreprm392.service.model;

public class BaseModel {
    private String uid;

    public BaseModel() {
    }

    public BaseModel(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
    
}
