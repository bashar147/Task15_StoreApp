package com.example.task15.Classes;

public class SignUpModel {

    private String name , email , pass , repass;
    private long order;
    private String imageUrl;
    public SignUpModel() {
    }

    public SignUpModel(String name, String email, String pass, String repass,long order, String imageUrl) {
        this.name = name;
        this.email = email;
        this.pass = pass;
        this.repass = repass;
        this.order = order;
        this.imageUrl = imageUrl;
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
}
