package com.iteration.gandhinagarmart.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class WishlistList {

    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("wishlist")
    private ArrayList<Wishlist> wishlistList;

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

    public ArrayList<Wishlist> getWishlistList() {
        return wishlistList;
    }

    public void setWishlistList(ArrayList<Wishlist> wishlistList) {
        this.wishlistList = wishlistList;
    }
}
