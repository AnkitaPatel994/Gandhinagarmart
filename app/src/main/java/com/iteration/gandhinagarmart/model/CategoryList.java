package com.iteration.gandhinagarmart.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CategoryList {
    @SerializedName("category")
    private ArrayList<Category> categoryList;

    public ArrayList<Category> getCategoryArrayList() {
        return categoryList;
    }

    public void setCategoryList(ArrayList<Category> categoryList) {
        this.categoryList = categoryList;
    }
}
