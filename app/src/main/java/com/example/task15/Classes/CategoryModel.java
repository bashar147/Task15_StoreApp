package com.example.task15.Classes;

public class CategoryModel {

    private String imageUrl, Name;
    private long  PRICE ;
    private long COUNTER;
    private String madIn;
    private String description;

    public CategoryModel() {

    }

    public CategoryModel(String imageUrl, String name,long COUNTER , long PRICE) {
        this.imageUrl = imageUrl;
        this.Name = name;
        this.PRICE = PRICE;
        this.COUNTER = COUNTER;
    }

    public CategoryModel(String imageUrl, String Name, long PRICE , long COUNTER , String madeIn , String description) {
        this.imageUrl = imageUrl;
        this.Name = Name;
        this.PRICE = PRICE;
        this.COUNTER = COUNTER;
        this.madIn = madeIn;
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public long getPRICE() {
        return PRICE;
    }

    public void setPRICE(long PRICE) {
        this.PRICE = PRICE;
    }

    public long getCOUNTER() {
        return COUNTER;
    }

    public void setCOUNTER(long COUNTER) {
        this.COUNTER = COUNTER;
    }

    public String getMadIn() {
        return madIn;
    }

    public void setMadIn(String madIn) {
        this.madIn = madIn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
