package com.example.swproject;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface MyApi {

    @GET("objects")
    Call<List<PostItem>> getPosts();

    @Multipart
    @POST("/imgs/")
    Call<ResponseBody> imagePost(
            @Part MultipartBody.Part image);

}
