package com.example.swproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import java.util.ArrayList;

public class SelectMode extends AppCompatActivity {

    ArrayList<String> nameList;ArrayList<String> phoneList;ArrayList<Integer> imageList;
    LayoutInflater layoutInflater;
    LinearLayout container;
    View view;

    int btnCheckNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_mode);

        container = findViewById(R.id.test1);
        layoutInflater = LayoutInflater.from(this);

        for (int i = 0; i < 10; i++)
        {
            view = layoutInflater.inflate(R.layout.activity_custom_select_view, null, false);
            //사진
            ImageView imageView = (ImageView) view.findViewById(R.id.item_image);
            imageView.setImageResource(R.drawable.test);
            //이름
            TextView nameText = view.findViewById(R.id.item_name);
            nameText.setText("Test" + i);
            //번호
            TextView phoneText = view.findViewById(R.id.item_phonenum);
            container.addView(view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(SelectMode.this, ObjectInformation.class);

                    //이미지 전달
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    Bitmap img = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                    img = Bitmap.createScaledBitmap(img, 800, 800, true);
                    img.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    intent.putExtra("image", byteArray);

                    intent.putExtra("name", nameText.getText());
                    startActivity(intent);
                }
            });
        }
    }
}