package com.example.swproject;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditActivity extends AppCompatActivity {

    private MyApi mMyAPI;
    private final String BASE_URL = "https://b6d9-175-119-83-193.jp.ngrok.io";
    ArrayList<String> imageList;
    ArrayList<String> urlList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        imageList = new ArrayList<>();
        urlList = new ArrayList<>();

        GetImageObjects();

        // Intent로 저장한 이미지 가져오기
        byte[] byteArray = getIntent().getByteArrayExtra("image");
        Bitmap image = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        Matrix matrix = new Matrix();
        matrix.postRotate(-90);
        Bitmap rotatedBitmap = Bitmap.createBitmap(image,0,0, image.getWidth(), image.getHeight(), matrix, false);

        ImageView ivImage = findViewById(R.id.img);
        ivImage.setImageBitmap(rotatedBitmap);

        // 편집버튼 기능
        Button editBtn = findViewById(R.id.editBtn);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditActivity.this, SelectMode.class);
                intent.putStringArrayListExtra("imageList", imageList);
                intent.putStringArrayListExtra("urlList", urlList);
                startActivity(intent);
            }
        });

    }

    // 서버 연결
    private void initMyAPI(String baseUrl){
        Log.d(TAG,"initMyAPI : " + baseUrl);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mMyAPI = retrofit.create(MyApi.class);
    }

    // 서버에 있는 Object 사진들 리스트로 저장
    private void GetImageObjects() {
        initMyAPI(BASE_URL);
        Log.d(TAG,"GET");
        Call<List<PostItem>> getCall = mMyAPI.getPosts();
        getCall.enqueue(new Callback<List<PostItem>>() {
            @Override
            public void onResponse(Call<List<PostItem>> call, Response<List<PostItem>> response) {
                if( response.isSuccessful()){
                    List<PostItem> mList = response.body();
                    for( PostItem item : mList){
                        imageList.add("https://" + item.getImage().substring(7));
                        Log.d("dsds", item.getUrl());
                        urlList.add(item.getUrl());
                    }
                }else {
                    Log.d(TAG,"Status Code : " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<PostItem>> call, Throwable t) {
                Log.d(TAG,"Fail msg : " + t.getMessage());
            }
        });
    }

}