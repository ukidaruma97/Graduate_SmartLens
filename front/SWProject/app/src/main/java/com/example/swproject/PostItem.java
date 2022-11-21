package com.example.swproject;

import com.google.gson.annotations.SerializedName;

public class PostItem {
    @SerializedName("id") private int id;
    @SerializedName("image") private String image;


    public PostItem(int id, String image) {
        this.id = id;
        this.image = image;
    }

    public int getId() {return this.id;}
    public String getImage() {return this.image;}
}
