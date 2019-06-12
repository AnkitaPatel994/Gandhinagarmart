package com.iteration.gandhinagarmart.model;

import com.google.gson.annotations.SerializedName;

public class ProductSize {

    @SerializedName("s_id")
    private String s_id;
    @SerializedName("s_pro_id")
    private String s_pro_id;
    @SerializedName("size_name")
    private String size_name;
    @SerializedName("size_qty")
    private String size_qty;
    @SerializedName("size_price")
    private String size_price;
    @SerializedName("size_discount")
    private String size_discount;
    @SerializedName("size_chart_img")
    private String size_chart_img;

    public ProductSize(String s_id, String s_pro_id, String size_name, String size_qty, String size_price, String size_discount, String size_chart_img) {
        this.s_id = s_id;
        this.s_pro_id = s_pro_id;
        this.size_name = size_name;
        this.size_qty = size_qty;
        this.size_price = size_price;
        this.size_discount = size_discount;
        this.size_chart_img = size_chart_img;
    }

    public String getS_id() {
        return s_id;
    }

    public void setS_id(String s_id) {
        this.s_id = s_id;
    }

    public String getS_pro_id() {
        return s_pro_id;
    }

    public void setS_pro_id(String s_pro_id) {
        this.s_pro_id = s_pro_id;
    }

    public String getSize_name() {
        return size_name;
    }

    public void setSize(String size_name) {
        this.size_name = size_name;
    }

    public String getSize_qty() {
        return size_qty;
    }

    public void setSize_qty(String size_qty) {
        this.size_qty = size_qty;
    }

    public String getSize_price() {
        return size_price;
    }

    public void setSize_price(String size_price) {
        this.size_price = size_price;
    }

    public String getSize_discount() {
        return size_discount;
    }

    public void setSize_discount(String size_discount) {
        this.size_discount = size_discount;
    }

    public String getSize_chart_img() {
        return size_chart_img;
    }

    public void setSize_chart_img(String size_chart_img) {
        this.size_chart_img = size_chart_img;
    }
}
