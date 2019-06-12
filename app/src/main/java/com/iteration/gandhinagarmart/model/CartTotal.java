package com.iteration.gandhinagarmart.model;

import com.google.gson.annotations.SerializedName;

public class CartTotal {

    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("cart_total")
    private String cart_total;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCart_total() {
        return cart_total;
    }

    public void setCart_total(String cart_total) {
        this.cart_total = cart_total;
    }
}
