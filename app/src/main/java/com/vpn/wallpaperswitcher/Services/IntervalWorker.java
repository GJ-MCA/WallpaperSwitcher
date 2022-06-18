package com.vpn.wallpaperswitcher.Services;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class IntervalWorker extends Worker {

    private final Context context;
    private String TAG = "IntervalWorker";
    private String WALLPAPER_DIRECTORY = "Wallpaper Switcher";
    private String INTERVAL_DIRECTORY = "Wallpaper Interval";

    public IntervalWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {

        SimpleDateFormat sdf = new SimpleDateFormat("mm");
        Data data = getInputData();
        int interval = data.getInt("time_interval", 1);
        int minutes = data.getInt("minutes", Integer.parseInt(sdf.format(new Date())));
        int total = interval + minutes;
        Log.d(TAG, "doWork : Time minutes : " + minutes);
        Log.d(TAG, "doWork : Time Interval Number : " + interval);
        int fileNumber = 0;

        for (int i = 0; i >= 0; i++) {

            String wallpaperDirectoryPath = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                wallpaperDirectoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + File.separator + WALLPAPER_DIRECTORY + File.separator + INTERVAL_DIRECTORY + File.separator).toString();
                //Log.d("PATH1", wallpaperDirectoryPath);
            } else {
                wallpaperDirectoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + File.separator + WALLPAPER_DIRECTORY + File.separator  + INTERVAL_DIRECTORY + File.separator).toString();
                //Log.d("PATH2", wallpaperDirectoryPath);
            }

            File wallpaperDirectory = new File(wallpaperDirectoryPath);
            if (wallpaperDirectory.exists()) {
                try {
                    File[] files = wallpaperDirectory.listFiles();
                    for (int j = 0; j < files.length; j++) {
                        Log.d(TAG, "File number [" + j + "] : " + files[j]);
                    }
                    if (files.length > 0) {
                        Log.d(TAG, "Size: " + files.length);
                        //int randomFilePathIndex = getRandomInt(0, files.length - 1);
                        //File randomFile = files[randomFilePathIndex];
                        File randomFile = files[fileNumber];
                        fileNumber++;
                        if (fileNumber > files.length) {
                            fileNumber = 0;
                        }
                        Log.d(TAG, "file name: " + randomFile);
                        long fileSizeInKb = getFileSizeInKb(randomFile);
                        long maxFileSizeInKb = 4096;
                        Log.d(TAG, "Wallpaper file size: " + fileSizeInKb);
                        if (fileSizeInKb <= maxFileSizeInKb) {
                            String randomFilePath = randomFile.getAbsolutePath();
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            Bitmap image = BitmapFactory.decodeFile(randomFilePath, options);
                            WallpaperManager manager = WallpaperManager.getInstance(context);
                            manager.setBitmap(image);


                            while (Integer.parseInt(sdf.format(new Date())) != total) {
                            }
                            total += interval;
                            if (total >= 60) {
                                total = 0;
                            }
                            //Thread.sleep(interval * 1000L);
                        } else {
                            Log.d(TAG, "File size exceeds limit: " + maxFileSizeInKb);
                        }
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

        return Result.success();
    }

//    public static int getRandomInt(int min, int max) {
//        return new Random().nextInt((max - min) + 1) + min;
//    }

    public static long getFileSizeInKb(File file) {
        return file.length() / 1024;
    }

    @Override
    public void onStopped() {
        Log.d(TAG, "onStopped Called");
        super.onStopped();
    }
}
