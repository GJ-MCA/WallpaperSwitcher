package com.vpn.wallpaperswitcher.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class IntervalBroadcast extends BroadcastReceiver {
    private String TAG = "ScreenLockReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive called: Change Wallpaper");
        new com.vpn.wallpaperswitcher.Services.Util().setRandomWallpaper(context);
    }
}
