package com.example.task15.Classes;

public class ProfileModel {
    private String name , email , pass , repass , location , order , complete , phoneNumber;

    public ProfileModel(String name, String email, String pass, String repass, String location, String order, String complete, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.pass = pass;
        this.repass = repass;
        this.location = location;
        this.order = order;
        this.complete = complete;
        this.phoneNumber = phoneNumber;
    }

    public ProfileModel(String name, String email, String pass, String location, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.pass = pass;
        this.location = location;
        this.phoneNumber = phoneNumber;
    }

    public ProfileModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getRepass() {
        return repass;
    }

    public void setRepass(String repass) {
        this.repass = repass;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getComplete() {
        return complete;
    }

    public void setComplete(String complete) {
        this.complete = complete;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
