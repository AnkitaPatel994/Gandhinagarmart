package com.iteration.gandhinagarmart.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SliderList {

    @SerializedName("slider")
    private ArrayList<Slider> sliderList;

    public ArrayList<Slider> getSliderArrayList() {
        return sliderList;
    }

    public void setSliderList(ArrayList<Slider> sliderList) {
        this.sliderList = sliderList;
    }
}
