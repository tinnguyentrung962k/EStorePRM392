package com.prm392.estoreprm392.service.model;

import java.util.Date;

public class User extends BaseModel {
    private String email;
    private String name;

    private String phoneNumber;

    private String protoUrl;

    public User() {
    }

    public User(String uid, String email, String name, String phoneNumber, String protoUrl) {
        super(uid);
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.protoUrl = protoUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProtoUrl() {
        return protoUrl;
    }

    public void setProtoUrl(String protoUrl) {
        this.protoUrl = protoUrl;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", protoUrl='" + protoUrl + '\'' +
                '}';
    }
}

