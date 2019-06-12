package com.iteration.gandhinagarmart.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ProductSizeList {

    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("ProductSize")
    private ArrayList<ProductSize> ProductSizeList;

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

    public ArrayList<ProductSize> getProductSizeList() {
        return ProductSizeList;
    }

    public void setProductSizeList(ArrayList<ProductSize> productSizeList) {
        ProductSizeList = productSizeList;
    }

}
