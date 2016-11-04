package com.jikexueyuan.onekeyspeedup;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends Activity {

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppInfo = activityManager.getRunningAppProcesses();

        long beforeMem = getAvailMemory();

        if (runningAppInfo != null) {
            for (ActivityManager.RunningAppProcessInfo runningApp : runningAppInfo) {
                if (runningApp.importance > ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
                    String[] pkgList = runningApp.pkgList;
                    for (String pkg : pkgList) {
                        activityManager.killBackgroundProcesses(pkg);
                    }
                }
            }
        }

        long afterMem = getAvailMemory();

        Toast.makeText(MainActivity.this, "为您节省了" + (afterMem - beforeMem) + "M内存", Toast.LENGTH_SHORT).show();


    }

    //获取当前可用内存
    private long getAvailMemory() {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo.availMem / (1024 * 1024);
    }


}
