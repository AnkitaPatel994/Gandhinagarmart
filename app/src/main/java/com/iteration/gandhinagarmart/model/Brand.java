package com.iteration.gandhinagarmart.model;

import com.google.gson.annotations.SerializedName;

public class Brand {

    @SerializedName("brand_id")
    private String brand_id;
    @SerializedName("brand_name")
    private String brand_name;
    @SerializedName("brand_img")
    private String brand_img;

    public Brand(String brand_id, String brand_name, String brand_img) {
        this.brand_id = brand_id;
        this.brand_name = brand_name;
        this.brand_img = brand_img;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getBrand_img() {
        return brand_img;
    }

    public void setBrand_img(String brand_img) {
        this.brand_img = brand_img;
    }
}
