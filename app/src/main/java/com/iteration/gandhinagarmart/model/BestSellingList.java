package com.iteration.gandhinagarmart.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BestSellingList {

    @SerializedName("bestselling")
    private ArrayList<Product> bestsellingList;

    public ArrayList<Product> getBestSellingArrayList() {
        return bestsellingList;
    }

    public void setBestsellingList(ArrayList<Product> bestsellingList) {
        this.bestsellingList = bestsellingList;
    }
}
