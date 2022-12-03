package com.example.swproject;

import com.google.gson.annotations.SerializedName;

public class PostItem {
    @SerializedName("id") private int id;
    @SerializedName("imgId") private String imgId;
    @SerializedName("image") private String image;
    @SerializedName("url") private String url;



    public PostItem(int id, String image, String imgId, String url) {
        this.id = id;
        this.image = image;
        this.imgId = imgId;
        this.url = url;
    }

    public int getId() {return this.id;}
    public String getImage() {return this.image;}
    public String getImgId() {return this.imgId;}
    public String getUrl() {return this.url;}
}
