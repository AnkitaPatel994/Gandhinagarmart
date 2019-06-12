package com.iteration.gandhinagarmart.model;

import com.google.gson.annotations.SerializedName;

public class Slider {

    @SerializedName("id")
    private String id;
    @SerializedName("banner")
    private String banner;

    public Slider(String id, String banner) {
        this.id = id;
        this.banner = banner;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }
}
