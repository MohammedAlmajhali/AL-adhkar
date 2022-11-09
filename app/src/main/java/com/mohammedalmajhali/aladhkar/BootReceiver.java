package com.mohammedalmajhali.aladhkar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import java.util.Calendar;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {





        // نشوف هل قد المستخدم حدد متى يريد الاشعارات ولا احددهن انا بنفسي
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        // نكتب في الملف ان قد المستخدم حدد وقت مابش داعي نحدد وقت افراضي
        SharedPreferences.Editor editor = sharedPreferences.edit();



        /*
            بانجيب الساعه اول شي من الملف او القيمه الافتراضيه حقها
            بعدين نختبر هل عاد المستخدم ولا قد فتح التطبيق ولا مرة
            اذاً اخزن الاشعارات

            وشكله مابش له داعي الاختبار لان هذا البروتكاست يمكن ما يشتغل الا بعد ما يشتغل التطبيق على الاقل مرة
            لكن سويناه احتياط

        */


        int hourMorning = sharedPreferences.getInt("morningAlarmHour", 6);
        int minuteMorning = sharedPreferences.getInt("morningAlarmMinute", 35);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourMorning);
        calendar.set(Calendar.MINUTE, minuteMorning);
        calendar.set(Calendar.SECOND, 0);

        Intent intent1 = new Intent(context, TimeReceiver.class);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context, 0,intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        am.cancel(pendingIntent1);


        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent1);

        // موعد ظهور اشعار اذكار الصباح
        if (!sharedPreferences.getBoolean("morningTime", false)) {
            editor.putBoolean("morningTime",true);
            editor.putInt("morningAlarmHour", 6);
            editor.putInt("morningAlarmMinute", 35);
            editor.apply();
        }










        int hourEvening = sharedPreferences.getInt("eveningAlarmHour", 17);
        int minuteEvening = sharedPreferences.getInt("eveningAlarmMinute", 30);

        Calendar calendar2 = Calendar.getInstance();
            calendar2.set(Calendar.HOUR_OF_DAY, hourEvening);
            calendar2.set(Calendar.MINUTE, minuteEvening);
            calendar2.set(Calendar.SECOND, 0);
            Intent intent2 = new Intent(context, TimeReceiver.class);
            PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context, 1,intent2, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager pm = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
            pm.cancel(pendingIntent2);
            pm.setRepeating(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent2);


        // موعد ظهور اشعار اذكار المساء
        if (!sharedPreferences.getBoolean("eveningTime", false)) {
            editor.putBoolean("eveningTime",true);
            editor.putInt("eveningAlarmHour", 17);
            editor.putInt("eveningAlarmMinute", 30);
            editor.apply();
        }




        // الان نتأكد من ال job هل قدها شغالة من اول او اشغلها
        if (!MainActivity.isJobIdRunning(context,132)){
            ComponentName componentName = new ComponentName(context.getApplicationContext(), MyJopService.class);
            JobInfo info;

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                info = new JobInfo.Builder(132, componentName).setPeriodic(15 * 60 * 1000).build();

            }
            else {
                info = new JobInfo.Builder(132 , componentName).setMinimumLatency(15*60*1000).build();
            }
            JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

            scheduler.schedule(info);
        }

    }
}