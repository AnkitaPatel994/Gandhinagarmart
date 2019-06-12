package com.iteration.gandhinagarmart.model;

import com.google.gson.annotations.SerializedName;

public class ProductImg {

    @SerializedName("img_id")
    private String img_id;
    @SerializedName("img_pro_id")
    private String img_pro_id;
    @SerializedName("pro_img_name")
    private String pro_img_name;

    public ProductImg(String img_id, String img_pro_id, String pro_img_name) {
        this.img_id = img_id;
        this.img_pro_id = img_pro_id;
        this.pro_img_name = pro_img_name;
    }

    public String getImg_id() {
        return img_id;
    }

    public void setImg_id(String img_id) {
        this.img_id = img_id;
    }

    public String getImg_pro_id() {
        return img_pro_id;
    }

    public void setImg_pro_id(String img_pro_id) {
        this.img_pro_id = img_pro_id;
    }

    public String getPro_img_name() {
        return pro_img_name;
    }

    public void setPro_img_name(String pro_img_name) {
        this.pro_img_name = pro_img_name;
    }
}
