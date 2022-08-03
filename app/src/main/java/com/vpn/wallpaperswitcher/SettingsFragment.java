package com.vpn.wallpaperswitcher;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.shashank.sony.fancytoastlib.FancyToast;
import com.vpn.wallpaperswitcher.Services.IntervalManager;
import com.vpn.wallpaperswitcher.Services.IntervalWorker;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class SettingsFragment extends Fragment implements View.OnClickListener {

    private Button btnStartService, btnStopService;
    private Spinner spTime;
    private LinearLayout llFolder;

    private int time;
    boolean isServiceRunning = false;
    private String[] timeList = {"1 Minute", "2 Minutes", "3 Minutes", "4 Minutes", "5 Minutes"};
    private String TAG = "MainActivity";

    IntervalManager intervalManager;
    OneTimeWorkRequest oneTimeWorkRequest;
    PeriodicWorkRequest periodicWorkRequest;

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

        time = 1;

        //button click events
        btnStartService.setOnClickListener(this);
        btnStopService.setOnClickListener(this);
        llFolder.setOnClickListener(this);

        spTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                time = i+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnStartService:
//                startServiceViaWorker();
//                startService();
                startInterval();
                break;

            case R.id.btnStopService:
//                stopService();
                stopInterval();
                break;

            case R.id.llFolder:
                Intent intent = new Intent(getContext(), WallpaperFolderActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void stopInterval() {
////        WorkManager.getInstance(getContext()).cancelAllWorkByTag("time");
////        WorkManager.getInstance(getContext()).cancelWorkById(oneTimeWorkRequest.getId());
////        WorkManager.getInstance(getContext()).cancelAllWorkByTag("time1");
//        WorkManager.getInstance(getContext()).cancelWorkById(periodicWorkRequest.getId());
//        isServiceRunning = false;
//        FancyToast.makeText(getContext(), "Service Stopped", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
////        try {
////            WorkManager.getInstance(getContext()).getWorkInfoByIdLiveData(periodicWorkRequest.getId()).observe(this, new Observer<WorkInfo>() {
////                @Override
////                public void onChanged(WorkInfo workInfo) {
////                    Log.d("work status : ", String.valueOf(workInfo.getState()));
////                    if (!workInfo.getState().equals("SUCCEEDED")) {
////                        WorkManager.getInstance(getContext()).cancelWorkById(periodicWorkRequest.getId());
////                        FancyToast.makeText(getContext(), "Service Stopped", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
////                    }
////                }
////            });
////        } catch (Exception e) {
////            FancyToast.makeText(getContext(), "Service Stopped", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
////        }
        intervalManager.stopInterval();
    }

    private void startInterval() {

//        isServiceRunning = true;
//        SimpleDateFormat sdf = new SimpleDateFormat("mm");
//        Data data = new Data.Builder()
//                .putInt("time_interval", time)
//                .putInt("minutes", Integer.parseInt(sdf.format(new Date())))
//                .putBoolean("isServiceRunning", isServiceRunning)
//                .build();
//
////        oneTimeWorkRequest = new OneTimeWorkRequest.Builder(IntervalWorker.class)
////                .setInputData(data)
////                .addTag("time")
////                .build();
//
//        periodicWorkRequest = new PeriodicWorkRequest.Builder(IntervalWorker.class,16, TimeUnit.MINUTES)
//                .setInputData(data)
//                .addTag("time1")
//                .build();
//
//        //WorkManager.getInstance(requireContext()).enqueue(oneTimeWorkRequest);
//        WorkManager.getInstance(getContext()).enqueue(periodicWorkRequest);
//        FancyToast.makeText(getContext(), "Service Started", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
        intervalManager = new IntervalManager(getContext(), time);
        intervalManager.stopInterval();
        intervalManager.startInterval();

    }

//    @Override
//    public void onDestroy() {
//        Log.d(TAG, "onDestroy called");
//        //stopService();
//        super.onDestroy();
//    }
//
//    public void startService() {
//        Log.d(TAG, "startService called");
//        if (!MyService.isServiceRunning) {
//            Intent serviceIntent = new Intent(getContext(), MyService.class);
//            ContextCompat.startForegroundService(getContext(), serviceIntent);
//            //startService(serviceIntent);
//        }
//    }
//
//    public void stopService() {
//        Log.d(TAG, "stopService called");
//        if (MyService.isServiceRunning) {
//            Intent serviceIntent = new Intent(getContext(), MyService.class);
//            getActivity().stopService(serviceIntent);
//        }
//    }
//
//    public void startServiceViaWorker() {
//        Log.d(TAG, "startServiceViaWorker called");
//        String UNIQUE_WORK_NAME = "StartMyServiceViaWorker";
//        //String WORKER_TAG = "MyServiceWorkerTag";
//        WorkManager workManager = WorkManager.getInstance(getContext());
//
//        // As per Documentation: The minimum repeat interval that can be defined is 15 minutes (
//        // same as the JobScheduler API), but in practice 15 doesn't work. Using 16 here
//        PeriodicWorkRequest request =
//                new PeriodicWorkRequest.Builder(
//                        MyWorker.class,
//                        16,
//                        TimeUnit.MINUTES)
//                        //.addTag(WORKER_TAG)
//                        .build();
//        // below method will schedule a new work, each time app is opened
//        //workManager.enqueue(request);
//
//        // to schedule a unique work, no matter how many times app is opened i.e. startServiceViaWorker gets called
//        // https://developer.android.com/topic/libraries/architecture/workmanager/how-to/unique-work
//        // do check for AutoStart permission
//        workManager.enqueueUniquePeriodicWork(UNIQUE_WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, request);
//
//    }

}
