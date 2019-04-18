package com.qiku.fileprovidertest;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_TAKE_PHOTO = 0x110;
    private String mCurrentPhotoPath;
    private ImageView mIvPhoto;
    private static int REQUEST_EXTERN_STORAGE =0x01;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_EXTERNAL_STORAGE",
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mIvPhoto = findViewById(R.id.imageView);
        verityStoragePermiss(this);
    }

    public void takePhotoNoCompress(View view){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            String filename = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.CHINA)
                    .format(new Date()) + ".png";
            File file = new File(Environment.getExternalStorageDirectory(), "Android/data/com.qiku.fileprovidertest/files/" + filename);
            mCurrentPhotoPath = file.getAbsolutePath();
            if (Build.VERSION.SDK_INT >= 24) {
                Uri fileUri = FileProvider.getUriForFile(this, "com.qiku.fileprovidertest.fileprovider", file);
                Log.d("wanlihua", "wanlihua debug " + fileUri.toString() + " ,mCurrentPhotoPath" + mCurrentPhotoPath);
                Log.d("wanlihua", "wanlihua debug " + file.toString() + " ,filename " + filename);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, /*Uri.fromFile(file)*/fileUri);
            } else {
                Log.d("wanlihua", "wanlihua debug " + Uri.fromFile(file));

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            }
            startActivityForResult(takePictureIntent, REQUEST_CODE_TAKE_PHOTO);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode, data);
        //Log.d("wanlihua","wanlihua debug onActivityResult " + requestCode +" resultCode:" + resultCode);
        if (resultCode == RESULT_OK  &&  requestCode == REQUEST_CODE_TAKE_PHOTO){
            //Log.d("wanlihua","wanlihua debug onActivityResult ok ");
            mIvPhoto.setImageBitmap(BitmapFactory.decodeFile(mCurrentPhotoPath));
            //Log.d("wanlihua","wanlihua debug onActivityResult mIvPhoto.setImageBitmap :" + mCurrentPhotoPath);
            //mIvPhoto.forceLayout();
        }
    }
    public static void verityStoragePermiss(Activity activity){
        int permission = ActivityCompat.checkSelfPermission(activity,"android.permission.WRITE_EXTERNAL_STORAGE");
        //Log.d("wanlihua","wanlihua debug verityStoragePermiss ");
        if (permission != PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERN_STORAGE);
            //Log.d("wanlihua","wanlihua debug verityStoragePermiss requestPermissions ");
        }
        //Log.d("wanlihua","wanlihua debug verityStoragePermiss sucess ");

    }

}
