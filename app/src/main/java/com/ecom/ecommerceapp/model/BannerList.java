package com.ecom.ecommerceapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Procure on 18-01-2018.
 */

public class BannerList {

    @SerializedName("ImgUrl")
    @Expose
    private String imgUrl;

    public BannerList(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

}