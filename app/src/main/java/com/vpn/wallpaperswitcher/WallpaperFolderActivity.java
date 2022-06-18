package com.vpn.wallpaperswitcher;

import static com.vpn.wallpaperswitcher.Services.Util.getFileSizeInKb;
import static com.vpn.wallpaperswitcher.Services.Util.getRandomInt;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.vpn.wallpaperswitcher.Adapter.WallpaparFolderAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WallpaperFolderActivity extends AppCompatActivity {
    private RecyclerView rvTimeWallpapers;
    private TextView tvNoWalls;
    private FloatingActionButton fabAddFolderWallpaper;

    WallpaparFolderAdapter adapter;
    ArrayList<Bitmap> folderWallapaperList;
    ArrayList<Bitmap> newWallpaperList;

    private final String WALLPAPER_DIRECTORY = "Wallpaper Switcher";
    private final String Interval_DIRECTORY = "Wallpaper Interval";
    private final String TAG = "Wallpaper Folder";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper_folder);

        rvTimeWallpapers = findViewById(R.id.rvTimeWallpapers);
        tvNoWalls = findViewById(R.id.tvNoWalls);
        fabAddFolderWallpaper = findViewById(R.id.fabAddFolderWallpaper);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rvTimeWallpapers.setLayoutManager(gridLayoutManager);

        fabAddFolderWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dexter.withContext(getApplicationContext())
                        .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, "Pick Image(s)"), 100);
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
        });

        getFolderWallpapers();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {

            if (data != null) {
                newWallpaperList = new ArrayList<>();

                if (data.getData() != null) {

                    try {
                        InputStream inputStream = getContentResolver().openInputStream(data.getData());
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        newWallpaperList.add(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                } else if (data.getClipData() != null) {

                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(data.getClipData().getItemAt(i).getUri());
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            newWallpaperList.add(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                    }

                }
                for (int j = 0; j < newWallpaperList.size(); j++) {
                    Log.d("Bitmap LIST", String.valueOf(newWallpaperList.get(j)));
                }
                //to store images into storage
                storageImagesIntoIntervalFolder();
            }
        }

    }

    private void storageImagesIntoIntervalFolder() {

        try {

            for (int i = 0; i < newWallpaperList.size(); i++) {

                SimpleDateFormat sdf = new SimpleDateFormat("_yyyyMMdd_HHmmss");
                String name = sdf.format(new Date());

//                Uri imageCollection = null;
//                ContentResolver resolver  =getContentResolver();
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                    //imageCollection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY + File.separator + WALLPAPER_DIRECTORY + File.separator + Interval_DIRECTORY + File.separator);
//                    imageCollection = Uri.parse("/storage/emulated/0/Pictures/" + WALLPAPER_DIRECTORY + File.separator + Interval_DIRECTORY + File.separator);
//                    Log.d("PATH 11", String.valueOf(imageCollection));
//                } else {
//                    imageCollection = Uri.parse((Uri) MediaStore.Images.Media.EXTERNAL_CONTENT_URI + WALLPAPER_DIRECTORY + File.separator + Interval_DIRECTORY + File.separator);
//                    Log.d("PATH 11", String.valueOf(imageCollection));
//                }
//
//                ContentValues values = new ContentValues();
//                values.put(MediaStore.Images.Media.DISPLAY_NAME, "WS" + name + i + ".jpg");
//                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
//
//                Uri uri = resolver.insert(imageCollection, values);
//
//                OutputStream outputStream = resolver.openOutputStream(Objects.requireNonNull(uri));
//                Bitmap bmp = newWallpaperList.get(i);
//                bmp.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
//                Objects.requireNonNull(outputStream);

                OutputStream imageOutStream;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.DISPLAY_NAME, "WS" + name + i + ".jpg");
                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                    values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + WALLPAPER_DIRECTORY + File.separator + Interval_DIRECTORY + File.separator);

                    Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    imageOutStream = getContentResolver().openOutputStream(uri);

                } else {
                    String imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + File.separator + WALLPAPER_DIRECTORY + File.separator + Interval_DIRECTORY + File.separator).toString();
                    File image = new File(imagesDir, "WS" + name + i + ".jpg");
                    imageOutStream = new FileOutputStream(image);
                }

                newWallpaperList.get(i).compress(Bitmap.CompressFormat.JPEG, 100, imageOutStream);
                imageOutStream.close();
                Log.d("IMAGE ", i + " done");

            }
            folderWallapaperList.addAll(newWallpaperList);
            //telling adapter to change in list
            adapter.notifyItemInserted(folderWallapaperList.size());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void getFolderWallpapers() {
        folderWallapaperList = new ArrayList<>();

        String wallpaperDirectoryPath = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            wallpaperDirectoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + File.separator + WALLPAPER_DIRECTORY + File.separator + Interval_DIRECTORY + File.separator).toString();
            Log.d("PATH1", wallpaperDirectoryPath);
        } else {
            wallpaperDirectoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + File.separator + WALLPAPER_DIRECTORY + File.separator + Interval_DIRECTORY + File.separator).toString();
            Log.d("PATH2", wallpaperDirectoryPath);
        }

        File wallpaperDirectory = new File(wallpaperDirectoryPath);
        //if folder dosen't exist it will create the folder
        if (wallpaperDirectory.exists()) {
            try {
                File[] files = wallpaperDirectory.listFiles();
                if (files != null && files.length > 0) {
                    tvNoWalls.setVisibility(View.GONE);
                    Log.d(TAG, "Size: " + files.length);

                    for (int i = 0; i < files.length; i++) {
                        File file = files[i];
                        String filePath = file.getAbsolutePath();
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        folderWallapaperList.add(BitmapFactory.decodeFile(filePath, options));
                    }
                    adapter = new WallpaparFolderAdapter(this, folderWallapaperList);
                    rvTimeWallpapers.setAdapter(adapter);

                } else {
                    Log.d(TAG, "Wallpaper directory is empty : " + wallpaperDirectoryPath);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            }

        } else {
            boolean createDirectoryResult = wallpaperDirectory.mkdirs();
            Log.d(TAG, "Wallpaper directory creation result: " + createDirectoryResult);
        }

    }
}