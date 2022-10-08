package com.example.swproject;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ObjectInformation extends AppCompatActivity {

    LayoutInflater layoutInflater;
    LinearLayout container;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object_information);

        byte[] byteArray = getIntent().getByteArrayExtra("image");
        Bitmap image = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        ImageView selectImage = findViewById(R.id.select_img);
        selectImage.setImageBitmap(image);

        container = findViewById(R.id.test2);
        layoutInflater = LayoutInflater.from(this);

        for (int i = 0; i < 10; i++) {
            view = layoutInflater.inflate(R.layout.activity_custom_select_view, null, false);
            //이름
            TextView nameText = view.findViewById(R.id.item_name);
            nameText.setText("Test" + i);
            //번호
            TextView phoneText = view.findViewById(R.id.item_phonenum);
            container.addView(view);
        }
    }
}