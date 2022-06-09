package com.vpn.wallpaperswitcher;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.vpn.wallpaperswitcher.Services.MyService;
import com.vpn.wallpaperswitcher.Services.MyWorker;

import java.util.concurrent.TimeUnit;

public class SettingsFragment extends Fragment implements View.OnClickListener {

    private Button btnStartService, btnStopService;
    private Spinner spTime;
    private LinearLayout llFolder;

    private String[] timeList = {"1 Minute", "2 Minutes", "3 Minutes", "4 Minutes", "5 Minutes"};
    private String TAG = "MainActivity";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        btnStartService = view.findViewById(R.id.btnStartService);
        btnStopService = view.findViewById(R.id.btnStopService);
        spTime = view.findViewById(R.id.spTime);
        llFolder = view.findViewById(R.id.llFolder);

        //filling time interval spinner
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, timeList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spTime.setAdapter(arrayAdapter);

        //button click events
        btnStartService.setOnClickListener(this);
        btnStopService.setOnClickListener(this);
        llFolder.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnStartService:
                startServiceViaWorker();
                startService();
                break;

            case R.id.btnStopService:
                stopService();
                break;

            case R.id.llFolder:
                Intent intent = new Intent(getContext(), WallpaperFolderActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy called");
        stopService();
        super.onDestroy();
    }

    public void startService() {
        Log.d(TAG, "startService called");
        if (!MyService.isServiceRunning) {
            Intent serviceIntent = new Intent(getContext(), MyService.class);
            ContextCompat.startForegroundService(getContext(), serviceIntent);
            //startService(serviceIntent);
        }
    }

    public void stopService() {
        Log.d(TAG, "stopService called");
        if (MyService.isServiceRunning) {
            Intent serviceIntent = new Intent(getContext(), MyService.class);
            getActivity().stopService(serviceIntent);
        }
    }

    public void startServiceViaWorker() {
        Log.d(TAG, "startServiceViaWorker called");
        String UNIQUE_WORK_NAME = "StartMyServiceViaWorker";
        //String WORKER_TAG = "MyServiceWorkerTag";
        WorkManager workManager = WorkManager.getInstance(getContext());

        // As per Documentation: The minimum repeat interval that can be defined is 15 minutes (
        // same as the JobScheduler API), but in practice 15 doesn't work. Using 16 here
        PeriodicWorkRequest request =
                new PeriodicWorkRequest.Builder(
                        MyWorker.class,
                        16,
                        TimeUnit.MINUTES)
                        //.addTag(WORKER_TAG)
                        .build();
        // below method will schedule a new work, each time app is opened
        //workManager.enqueue(request);

        // to schedule a unique work, no matter how many times app is opened i.e. startServiceViaWorker gets called
        // https://developer.android.com/topic/libraries/architecture/workmanager/how-to/unique-work
        // do check for AutoStart permission
        workManager.enqueueUniquePeriodicWork(UNIQUE_WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, request);

    }

}