package com.vpn.wallpaperswitcher.Services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.shashank.sony.fancytoastlib.FancyToast;

public class IntervalManager {
    private Context context;
    private int time;

    public IntervalManager(Context context, int time) {
        this.context = context;
        this.time = time;
    }

    public void startInterval() {

        Intent intent = new Intent(context, IntervalBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 2, intent, 0);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (manager != null) {
            long triggerAfter = time * 60 * 1000;
            long triggerevery = time * 60 * 1000;
            manager.setRepeating(AlarmManager.RTC_WAKEUP, triggerAfter, triggerevery, pendingIntent);
            FancyToast.makeText(context, "Service Started", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
        }

    }

    public void stopInterval() {

        Intent intent = new Intent(context, IntervalBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 2, intent, 0);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (manager != null) {
            manager.cancel(pendingIntent);
            FancyToast.makeText(context, "Service Stopped", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
        }

    }

}
