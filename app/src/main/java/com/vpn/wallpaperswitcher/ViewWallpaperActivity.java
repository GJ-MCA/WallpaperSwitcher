package com.vpn.wallpaperswitcher;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class ViewWallpaperActivity extends AppCompatActivity {
    ImageView ivWall;
    Button btnSetWall;
    String imgUrl;
    FloatingActionButton fabDownloadWall;
    WallpaperManager wallpaperManager;
    ProgressDialog dialog;

    private final String WALLPAPER_DIRECTORY = "Wallpaper Switcher";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_wallpaper);

        ivWall = findViewById(R.id.ivWall);
        btnSetWall = findViewById(R.id.btnSetWall);
        fabDownloadWall = findViewById(R.id.fabDownloadWall);

        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading...");
        dialog.setMessage("Please Wait");
        dialog.setCancelable(false);

        //getting image from intent and set to imageview
        imgUrl = getIntent().getStringExtra("imgUrl");
        Glide.with(this).load(imgUrl).into(ivWall);

        fabDownloadWall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkStoragePermission();

            }
        });

        wallpaperManager = WallpaperManager.getInstance(getApplicationContext());

        btnSetWall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                Glide.with(ViewWallpaperActivity.this).asBitmap().load(imgUrl).listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {

                        FancyToast.makeText(ViewWallpaperActivity.this, "Failed to load image", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {

                        try {
                            wallpaperManager.setBitmap(resource);
                        } catch (IOException e) {
                            e.printStackTrace();
                            FancyToast.makeText(ViewWallpaperActivity.this, "Failed to set Wallpaper", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                        }
                        return false;
                    }
                }).submit();
                dialog.dismiss();
                FancyToast.makeText(ViewWallpaperActivity.this, "Home Screen Wallpaper Applied", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
            }
        });

    }

    private void checkStoragePermission() {

        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                        dialog.show();

                        BitmapDrawable drawable = (BitmapDrawable) ivWall.getDrawable();
                        Bitmap bitmap = drawable.getBitmap();
                        Log.d("BITMAP", String.valueOf(bitmap));

                        try {
                            saveWallpaperToStorage(bitmap);
                        } catch (IOException e) {
                            dialog.dismiss();
                            FancyToast.makeText(ViewWallpaperActivity.this, "Error While Downloading", FancyToast.ERROR, FancyToast.SUCCESS, false).show();
                            e.printStackTrace();
                        }

                        //for download wallpaper
//                        DownloadManager downloadManager = null;
//                        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
//
//                        Uri uri = Uri.parse(imgUrl);
//                        String sdCardPath = Environment.getExternalStorageDirectory().toString();
//                        String wallpaperDirectoryPath = sdCardPath + File.separator + WALLPAPER_DIRECTORY + "/";
//
//                        DownloadManager.Request request = new DownloadManager.Request(uri);
//                        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI)
//                                .setAllowedOverRoaming(false)
//                                .setTitle("WS" + name)
//                                .setMimeType("image/jpeg")
//                                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
//                                //.setDestinationInExternalFilesDir(getApplicationContext(), wallpaperDirectoryPath, "WS" + name + ".jpg");
//                                .setDestinationInExternalPublicDir(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/Wallpaper Switcher/", "WS" + name + ".jpg");
//
//                        downloadManager.enqueue(request);
//                        dialog.dismiss();
//                        FancyToast.makeText(getApplicationContext(), "Downloading...",FancyToast.DEFAULT, FancyToast.SUCCESS, false).show();

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();

    }

    private void saveWallpaperToStorage(Bitmap bitmap) throws IOException {

        SimpleDateFormat sdf = new SimpleDateFormat("_yyyyMMdd_HHmmss");
        String name = sdf.format(new Date());

        OutputStream imageOutStream;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DISPLAY_NAME, "WS" + name + ".jpg");
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + WALLPAPER_DIRECTORY + File.separator);

            Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            imageOutStream = getContentResolver().openOutputStream(uri);

        } else {
            String imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + File.separator + WALLPAPER_DIRECTORY + File.separator).toString();
            File image = new File(imagesDir, "WS" + name + ".jpg");
            imageOutStream = new FileOutputStream(image);
        }


        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, imageOutStream);
        imageOutStream.close();
        dialog.dismiss();
        FancyToast.makeText(ViewWallpaperActivity.this, "Downloading Completed", FancyToast.DEFAULT, FancyToast.SUCCESS, false).show();

    }

}