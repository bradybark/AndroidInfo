package com.example.diagnosticsapp;

        import androidx.appcompat.app.AppCompatActivity;

        import android.app.ActivityManager;
        import android.os.Bundle;
        import android.os.Debug;
        import android.widget.TextView;

        import java.io.BufferedReader;
        import java.io.InputStreamReader;


public class MainActivity extends AppCompatActivity {

    public static float cpuTemperature()
    {
        Process process;
        try {
            process = Runtime.getRuntime().exec("cat sys/class/thermal/thermal_zone0/temp");
            process.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();
            if(line!=null) {
                float temp = Float.parseFloat(line);
                return temp / 1000.0f;
            }else{
                return 51.0f;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0f;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final long cpuTime1 = Debug.threadCpuTimeNanos();
        final long allocatedHeap = Debug.getNativeHeapAllocatedSize() / 1048576L;
        final long maxHeap = Debug.getNativeHeapSize() / 1048576L;
        final long freeHeap = (maxHeap - allocatedHeap);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView allocatedHeapTxt = (TextView) findViewById(R.id.allocatedHeapTextView);
        allocatedHeapTxt.setText("Allocated Heap:   " + allocatedHeap + " mb");
        TextView maxHeapTxt = (TextView) findViewById(R.id.maxHeapTextView);
        maxHeapTxt.setText("Max Heap:   " + maxHeap + " mb");
        TextView freeHeapTxt = (TextView) findViewById(R.id.freeHeapTextView);
        freeHeapTxt.setText("Free Heap:     " + freeHeap + " mb");

        float cpuTemp = cpuTemperature();
        TextView cpuTempTxt = (TextView) findViewById(R.id.cpuTempTextView);
        cpuTempTxt.setText("CPU Temp:   " + cpuTemp + " °F");

        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        long availableMegs = mi.availMem / 1048576L;
        TextView availRamText = (TextView) findViewById(R.id.availRamID);
        availRamText.setText("Available RAM:    " + availableMegs + " mb");

        long totalRam = mi.totalMem / 1048576L;
        TextView totalRamText = (TextView) findViewById(R.id.totalRamID);
        totalRamText.setText("Total RAM:    " + totalRam + " mb");

        long ramUsage = totalRam - availableMegs;
        TextView ramUsageTxt = (TextView) findViewById(R.id.ramUsageID);
        ramUsageTxt.setText("RAM Usage:    " + ramUsage + " mb");

        final long cpuTime2 = Debug.threadCpuTimeNanos();
        final long cpuTime = (cpuTime2- cpuTime1) / 1000000;
        TextView txtCpu = (TextView) findViewById(R.id.textCpu);
        txtCpu.setText("CPU Time:   " + cpuTime + " ms");

    }
}