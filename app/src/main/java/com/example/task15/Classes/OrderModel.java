package com.example.task15.Classes;



public class OrderModel {
    private String  product,price, comp;

    public OrderModel(String product, String price, String comp) {
        this.product = product;
        this.price = price;
        this.comp = comp;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getComp() {
        return comp;
    }

    public void setComp(String comp) {
        this.comp = comp;
    }
}
