package com.mohammedalmajhali.aladhkar;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.work.Configuration;

public class MyJopService extends JobService {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    public boolean onStartJob(JobParameters params) {

        Configuration.Builder builder = new Configuration.Builder();
        builder.setJobSchedulerJobIdRange(0, 1000);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();

        int i = sharedPreferences.getInt("indexOfDhekr", 0);
        int sum = sharedPreferences.getInt("sumAllToastDhekrs",1);

        String dhekr = sharedPreferences.getString(String.valueOf(i),"استغفر الله");

        editor.putInt("indexOfDhekr", (i+1)%sum);
        editor.apply();

        Toast toast = new Toast(this);

        View v = LayoutInflater.from(this).inflate(R.layout.toast_view, null, false);
        TextView text = v.findViewById(R.id.textViewToast);
        text.setText(dhekr);
        toast.setView(v);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP | Gravity.RIGHT, 40, 40);
            toast.show();

        jobFinished(params, true);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
//cd appdata&cd local&cd android&cd sdk &cd emulator-5&emulator -avd New_Device_3_API_30
