package com.iteration.gandhinagarmart.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BrandList {
    @SerializedName("brand")
    private ArrayList<Brand> brandList;

    public ArrayList<Brand> getBrandArrayList() {
        return brandList;
    }

    public void setBrandList(ArrayList<Brand> brandList) {
        this.brandList = brandList;
    }
}
