package com.ecom.ecommerceapp.model;

public class Category {

    public String category_name;
    public String image;
    public String category_id;

    public Category() {
    }

    public Category(String category_name, String image,String category_id) {
        this.category_name = category_name;
        this.image = image;
        this.category_id = category_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
