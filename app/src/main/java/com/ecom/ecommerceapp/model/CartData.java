package com.ecom.ecommerceapp.model;

public class CartData {

    String itemname;
    String price;
    String quantity;
    byte[] pimage;
    String description;

    public CartData(String itemname, String price, String quantity, byte[] pimage, String description) {
        this.itemname = itemname;
        this.price = price;
        this.quantity = quantity;
        this.pimage = pimage;
        this.description = description;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public byte[] getPimage() {
        return pimage;
    }

    public void setPimage(byte[] pimage) {
        this.pimage = pimage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
