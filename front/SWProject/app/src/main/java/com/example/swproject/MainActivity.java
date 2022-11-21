package com.example.swproject;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    Button photoBtn, callBtn;

    private MyApi mMyAPI;
    // ngrok홈페이지 URL
    private final String BASE_URL = "https://d461-175-119-83-193.jp.ngrok.io";
    // 이미지 저장 파일
    private File imageSelectFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 사진촬영 버튼기능
        photoBtn = (Button)findViewById(R.id.photoBtn);
        photoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i,0);
            }
        });

        // 사진 갤러리 불러오기 버튼기능
        callBtn = findViewById(R.id.callBtn);
        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        initMyAPI(BASE_URL);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap img = (Bitmap) extras.get("data");

                    float scale = (float) (1024/(float)img.getWidth());
                    int image_w = (int) (img.getWidth() * scale);
                    int image_h = (int) (img.getHeight() * scale);
                    img = Bitmap.createScaledBitmap(img, image_w, image_h, true);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    img.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byteArray = stream.toByteArray();

                    saveBitmapToJpeg(img, "SelectImage");
                    imageSelectFile = new File(getCacheDir().toString() + "/SelectImage.jpg");
                    ImagePost();

                    Intent intent = new Intent(MainActivity.this, EditActivity.class);
                    intent.putExtra("image", byteArray);
                    startActivity(intent);

                    //imageView.setImageBitmap(imageBitmap);
                }
                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    try {
                        InputStream in = getContentResolver().openInputStream(data.getData());
                        Bitmap img = BitmapFactory.decodeStream(in);
                        in.close();

                        float scale = (float) (1024/(float)img.getWidth());
                        int image_w = (int) (img.getWidth() * scale);
                        int image_h = (int) (img.getHeight() * scale);
                        img = Bitmap.createScaledBitmap(img, image_w, image_h, true);

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        img.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] byteArray = stream.toByteArray();

                        saveBitmapToJpeg(img, "SelectImage");
                        imageSelectFile = new File(getCacheDir().toString() + "/SelectImage.jpg");
                        ImagePost();

                        Intent intent = new Intent(MainActivity.this, EditActivity.class);
                        intent.putExtra("image", byteArray);
                        startActivity(intent);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    // 서버연동
    private void initMyAPI(String baseUrl){
        Log.d(TAG,"initMyAPI : " + baseUrl);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mMyAPI = retrofit.create(MyApi.class);
    }

    // 비트맵을 JPG로 변환
    private void saveBitmapToJpeg(Bitmap bitmap, String name) {
        //내부저장소 캐시 경로를 받아옵니다.
        File file = getCacheDir();
        //저장할 파일 이름
        String fileName = name + ".jpg";
        //storage 에 파일 인스턴스를 생성합니다.
        File tempFile = new File(file, fileName);

        try {
            // 자동으로 빈 파일을 생성합니다.
            tempFile.createNewFile();

            // 파일을 쓸 수 있는 스트림을 준비합니다.
            FileOutputStream out = new FileOutputStream(tempFile);

            // compress 함수를 사용해 스트림에 비트맵을 저장합니다.
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            // 스트림 사용후 닫아줍니다.
            out.close();
        } catch (FileNotFoundException e) {
            Log.e("MyTag","FileNotFoundException : " + e.getMessage());
        } catch (IOException e) {
            Log.e("MyTag","IOException : " + e.getMessage());
        }
    }

    // 이미지 포스터 기능
    private void ImagePost(){
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), imageSelectFile);
        MultipartBody.Part parts = MultipartBody.Part.createFormData("image", imageSelectFile.getName(), requestBody);
        Call<ResponseBody> postCall = mMyAPI.imagePost(parts);
        postCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Log.d(TAG,"등록 완료");
                }else {
                    Log.d(TAG,"Status Code : " + response.code());
                    Log.d(TAG,response.errorBody().toString());
                    Log.d(TAG,call.request().body().toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG,"Fail msg : " + t.getMessage());
            }
        });
    }
}