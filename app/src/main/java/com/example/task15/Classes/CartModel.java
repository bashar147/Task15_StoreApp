package com.example.task15.Classes;

public class CartModel {

    private String imageUrl, name;
    private long cartPrice;
    private long cartCounter;

    public CartModel() {

    }

    public CartModel(String imageUrl, String name, long CART_COUNTER , long CART_PRICE) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.cartCounter =CART_COUNTER;
        this.cartPrice = CART_PRICE;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCART_PRICE() {
        return cartPrice;
    }

    public void setCART_PRICE(long CART_PRICE) {
        this.cartPrice = CART_PRICE;
    }

    public long getCartCounter() {
        return cartCounter;
    }

    public void setCartCounter(long cartCounter) {
        this.cartCounter = cartCounter;
    }
}
