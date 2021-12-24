package com.example.task15.Classes;

public class Model {

private String imageUrl;
private int url;

    public Model() {

    }

    public Model(int url) {
        this.url = url;
    }


    public Model(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getUrl() {
        return url;
    }

    public void setUrl(int url) {
        this.url = url;
    }
}

