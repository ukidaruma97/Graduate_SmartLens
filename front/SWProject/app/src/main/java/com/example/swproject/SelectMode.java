package com.example.swproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class SelectMode extends AppCompatActivity {

    LayoutInflater layoutInflater;
    LinearLayout container;
    View view;

    ImageView imageView;
    Bitmap bitmap;

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_mode);

        // EditActivity에서 intent로 보낸 ArrayList 받기
        ArrayList<String> imageList = getIntent().getStringArrayListExtra("imageList");

        // 동적으로 레이아웃 생성
        container = findViewById(R.id.test1);
        layoutInflater = LayoutInflater.from(this);
        for (int i = 0; i < imageList.size(); i++)
        {
            view = layoutInflater.inflate(R.layout.activity_custom_select_view, container, false);
            //사진
            imageView = (ImageView) view.findViewById(R.id.objectImage);
            UrlImageConversion(imageList.get(i));

            //이미지 전달
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap img = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
            float scale = (float) (1024/(float)bitmap.getWidth());
            int image_w = (int) (bitmap.getWidth() * scale);
            int image_h = (int) (bitmap.getHeight() * scale);
            Bitmap resize = Bitmap.createScaledBitmap(bitmap, image_w, image_h, true);
            img.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(SelectMode.this, ObjectInformation.class);
                    intent.putExtra("image", byteArray);
                    startActivity(intent);
                }
            });
            container.addView(view);
        }
    }

    // URL을 이미지로 변경
    public void UrlImageConversion(String imageUrl) {
        Thread uThread = new Thread() {
            @Override
            public void run(){
                try{
                    // 이미지 URL 경로
                    URL url = new URL(imageUrl);

                    // web에서 이미지를 가져와 ImageView에 저장할 Bitmap을 만든다.
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setDoInput(true); // 서버로부터 응답 수신
                    conn.connect(); //연결된 곳에 접속할 때 (connect() 호출해야 실제 통신 가능함)

                    InputStream is = conn.getInputStream(); //inputStream 값 가져오기
                    bitmap = BitmapFactory.decodeStream(is); // Bitmap으로 변환

                }catch (MalformedURLException e){
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        };
        uThread.start(); // 작업 Thread 실행
        try{
            //메인 Thread는 별도의 작업 Thread가 작업을 완료할 때까지 대기해야 한다.
            //join() 호출하여 별도의 작업 Thread가 종료될 때까지 메인 Thread가 기다리도록 한다.
            //join() 메서드는 InterruptedException을 발생시킨다.
            uThread.join();

            //작업 Thread에서 이미지를 불러오는 작업을 완료한 뒤
            //UI 작업을 할 수 있는 메인 Thread에서 ImageView에 이미지 지정
            imageView.setImageBitmap(bitmap);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

}