package com.example.task15.Classes;

public class CategoryClick {

        private String imageUrl, Name;
        private int url , price , counter;

        public CategoryClick() {
        }


        public CategoryClick(String imageUrl, String name, int price, int counter) {
            this.imageUrl = imageUrl;
            Name = name;
            this.price = price;
            this.counter = counter;
        }

        public CategoryClick(String name, int url, int price, int counter) {
            Name = name;
            this.url = url;
            this.price = price;
            this.counter = counter;
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

        public int getUrl() {
            return url;
        }

        public void setUrl(int url) {
            this.url = url;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getCounter() {
            return counter;
        }

        public void setCounter(int counter) {
            this.counter = counter;
        }
}



