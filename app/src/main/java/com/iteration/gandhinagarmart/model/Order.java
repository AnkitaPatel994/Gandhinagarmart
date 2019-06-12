package com.iteration.gandhinagarmart.model;

import com.google.gson.annotations.SerializedName;

public class Order {

    @SerializedName("order_pro_id")
    private String order_pro_id;
    @SerializedName("pro_title")
    private String pro_title;
    @SerializedName("order_qty")
    private String order_qty;
    @SerializedName("order_size")
    private String order_size;
    @SerializedName("pro_oprice")
    private String pro_oprice;
    @SerializedName("pro_discount")
    private String pro_discount;
    @SerializedName("pro_price")
    private String pro_price;
    @SerializedName("order_status")
    private String order_status;
    @SerializedName("product_img")
    private String product_img;

    public String getOrder_pro_id() {
        return order_pro_id;
    }

    public void setOrder_pro_id(String order_pro_id) {
        this.order_pro_id = order_pro_id;
    }

    public String getPro_title() {
        return pro_title;
    }

    public void setPro_title(String pro_title) {
        this.pro_title = pro_title;
    }

    public String getOrder_qty() {
        return order_qty;
    }

    public void setOrder_qty(String order_qty) {
        this.order_qty = order_qty;
    }

    public String getOrder_size() {
        return order_size;
    }

    public void setOrder_size(String order_size) {
        this.order_size = order_size;
    }

    public String getPro_oprice() {
        return pro_oprice;
    }

    public void setPro_oprice(String pro_oprice) {
        this.pro_oprice = pro_oprice;
    }

    public String getPro_discount() {
        return pro_discount;
    }

    public void setPro_discount(String pro_discount) {
        this.pro_discount = pro_discount;
    }

    public String getPro_price() {
        return pro_price;
    }

    public void setPro_price(String pro_price) {
        this.pro_price = pro_price;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getProduct_img() {
        return product_img;
    }

    public void setProduct_img(String product_img) {
        this.product_img = product_img;
    }
}
