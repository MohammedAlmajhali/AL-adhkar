package com.mohammedalmajhali.aladhkar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.InflateException;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import java.util.Calendar;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    TextView btn_morning, btn_evening;
    final static int EVENING = 1, MORNING = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        try {
            Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(myToolbar);
            Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        }catch (Exception e){

        }

        try {
            btn_morning = findViewById(R.id.main_btn_morning);
            btn_evening = findViewById(R.id.main_btn_evening);
        }catch (Exception e){

        }

        try {

            new Thread(() -> {
                // نشوف هل قد المستخدم حدد متى يريد الاشعارات ولا احددهن انا بنفسي
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                // نكتب في الملف ان قد المستخدم حدد وقت مابش داعي نحدد وقت افراضي
                editor = sharedPreferences.edit();

                // موعد ظهور اشعار اذكار الصباح
                if (!sharedPreferences.getBoolean("morningTime", false)) {

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, 6);
                    calendar.set(Calendar.MINUTE, 35);
                    calendar.set(Calendar.SECOND, 0);
                    Intent intent1 = new Intent(MainActivity.this, TimeReceiver.class);
                    @SuppressLint("UnspecifiedImmutableFlag")
                    PendingIntent pendingIntent12 = PendingIntent.getBroadcast(MainActivity.this, 0,intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager am = (AlarmManager) MainActivity.this.getSystemService(ALARM_SERVICE);
                    am.cancel(pendingIntent12);
                    am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent12);


                    editor.putBoolean("morningTime",true);
                    editor.putInt("morningAlarmHour", 6);
                    editor.putInt("morningAlarmMinute", 35);
                    editor.apply();
                }
                // موعد ظهور اشعار اذكار المساء
                if (!sharedPreferences.getBoolean("eveningTime", false)) {


                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, 17);
                    calendar.set(Calendar.MINUTE, 30);
                    calendar.set(Calendar.SECOND, 0);
                    Intent intent1 = new Intent(MainActivity.this, TimeReceiver.class);
                    @SuppressLint("UnspecifiedImmutableFlag")
                    PendingIntent pendingIntent13 = PendingIntent.getBroadcast(MainActivity.this, 1,intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager pm = (AlarmManager) MainActivity.this.getSystemService(ALARM_SERVICE);
                    pm.cancel(pendingIntent13);
                    pm.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent13);



                    editor.putBoolean("eveningTime",true);
                    editor.putInt("eveningAlarmHour", 17);
                    editor.putInt("eveningAlarmMinute", 30);
                    editor.apply();
                }


                // نسجل البروتكاست حق اعادة التشغيل في النظام
                BootReceiver receiver = new BootReceiver();
                IntentFilter filter = new IntentFilter(Intent.ACTION_BOOT_COMPLETED);
                registerReceiver(receiver, filter);





                // هذولا الاشعارات اللي بايضهروا للمستخدم على شكل Toast
                editor.putString("0", "سبحان الله");
                editor.putString("1", "استغفر الله");
                editor.putString("2", "الحمد لله");
                editor.putString("3", "لا اله الا الله");
                editor.putString("4", "اللهم صل وسلم على سيدنا محمد");
                editor.putString("5", "سبحان الله وبحمده سبحان الله العظيم");
                editor.putString("6", "استغفر الله الذي لا اله الا هو الحي القيوم وأتوب إليه");
                editor.putString("7", "لا اله الا الله وحده لا شريك له");
                editor.putString("8", "الله اكبر");

                // مجموع كل الاذكار اللي على شكل toast عشان هناك اجلس ازيد لما اوصل للنهاية sum % (i+1)
                editor.putInt("sumAllToastDhekrs", 9);
                editor.apply();



                // الان نتأكد من ال job هل قدها شغالة من اول او اشغلها
                if (!isJobIdRunning(this,132)){
                    ComponentName componentName = new ComponentName(getBaseContext(), MyJopService.class);
                    JobInfo info;

                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                        info = new JobInfo.Builder(132, componentName).setPeriodic(15 * 60 * 1000).build();

                    }
                    else {
                        info = new JobInfo.Builder(132 , componentName).setMinimumLatency(15*60*1000).build();
                    }
                    JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);

                    scheduler.schedule(info);
                }


            }).start();

        }catch (Exception e){

        }




        try {

            btn_morning.setOnClickListener(v -> {
                Intent i = new Intent(getBaseContext(), SectionsActivity.class);
                i.putExtra("time", MORNING);
                startActivity(i);
                // overridePendingTransition(R.anim.enter, R.anim.exit);
            });

            btn_evening.setOnClickListener(v -> {
                Intent i = new Intent(getBaseContext(), SectionsActivity.class);
                i.putExtra("time", EVENING);
                startActivity(i);
                // overridePendingTransition(R.anim.enter, R.anim.exit);
            });


        }catch (Exception e){

        }

    }



    // هذه الداله تتأكد لي اذا ال job هذه شغاله او لا
    public static boolean isJobIdRunning( Context context, int JobId) {

        final JobScheduler jobScheduler = (JobScheduler) context.getSystemService( Context.JOB_SCHEDULER_SERVICE ) ;

        for ( JobInfo jobInfo : jobScheduler.getAllPendingJobs() ) {
            if ( jobInfo.getId() == JobId ) {
                return true;
            }
        }

        return false;
    }





    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        try {

            MenuInflater f = getMenuInflater();
            f.inflate(R.menu.menu_main, menu);

            int hourMorning = sharedPreferences.getInt("morningAlarmHour", 6);
            int minuteMorning = sharedPreferences.getInt("morningAlarmMinute", 35);

            String S_hourMorning;
            if (hourMorning <= 12) {
                if (hourMorning == 12) {
                    S_hourMorning = "12 م";
                }
                else if (hourMorning == 0) {
                    S_hourMorning = "12 ص";
                }
                else {
                    S_hourMorning = hourMorning + " ص ";
                }
            }
            else {
                S_hourMorning = (hourMorning - 12) + " م ";
            }

            menu.findItem(R.id.menu_item_morning_alert).setTitle(getString(R.string.time_alert_morning, S_hourMorning, minuteMorning+""));

            int hourEvening = sharedPreferences.getInt("eveningAlarmHour", 5);
            int minuteEvening = sharedPreferences.getInt("eveningAlarmMinute", 30);

            String S_hourEvening;
            if (hourEvening <= 12) {
                if (hourEvening == 12) {
                    S_hourEvening = "12 م";
                }
                else if (hourEvening == 0) {
                    S_hourEvening = "12 ص";
                }
                else {
                    S_hourEvening = hourEvening + " ص ";
                }
            }
            else {
                S_hourEvening = (hourEvening - 12) + " م ";
            }

            menu.findItem(R.id.menu_item_evening_alert).setTitle(getString(R.string.time_alert_evening, S_hourEvening, minuteEvening+"" ));

            return true;
        } catch (InflateException e) {
            e.printStackTrace();
            return false;
        }
        catch (Exception e){

            return false;
        }
    }





    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        boolean re = false;




        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;

        switch (item.getItemId()) {
            case R.id.menu_item_morning_alert:
                mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                        calendar.set(Calendar.MINUTE, selectedMinute);
                        calendar.set(Calendar.SECOND, 0);


                        // هذا هو الذي بايجلس يرسل التنبيهات او يرسل الانتنت الى البروتكاست
                        // بتعبير ادق هذا باينفذ خدمات التنبيه
                        // داخله داله التكرار نحدد ايش النيه(intent) اللي نريدها تتنفذ ومتى تتنفذ وكل كم يعيد تشغيل التنبيه

                        // نحدد المستقبل الذي بانفتحه او بانتعامل معاه
                        Intent intent1 = new Intent(MainActivity.this, TimeReceiver.class);


                        // نحول الـ intent الى PendingIntent
                        @SuppressLint("UnspecifiedImmutableFlag")
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0,intent1, PendingIntent.FLAG_UPDATE_CURRENT);



                        // هذا هو الذي بايجلس يرسل التنبيهات او يرسل الانتنت الى البروتكاست
                        // بتعبير ادق هذا باينفذ خدمات التنبيه
                        // داخله داله التكرار نحدد ايش النيه(intent) اللي نريدها تتنفذ ومتى تتنفذ وكل كم يعيد تشغيل التنبيه
                        AlarmManager am = (AlarmManager) MainActivity.this.getSystemService(MainActivity.this.ALARM_SERVICE);
                        am.cancel(pendingIntent);

                        // حددنا له نوع التنبيه (مش مهم يكون اي شي ، هنا حددنا استيقاظ من النوم ) والوقت حق التنبيه متى،
                        // وبعد كم يعيد التنبيه والانتنت اللي باينفذها
                        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);



                        // نكتب في الملف
                        editor.putBoolean("morningTime", true);
                        editor.putInt("morningAlarmHour", selectedHour);
                        editor.putInt("morningAlarmMinute", selectedMinute);
                        editor.apply();

                        Toast.makeText(getBaseContext(), "تم التعيين", Toast.LENGTH_SHORT).show();
                        invalidateOptionsMenu();
                    }
                }, hour, minute, false);//Yes 24 hour time
                // mTimePicker.setTitle("موعد التذكير بأذكار الصباح");
                mTimePicker.show();
                re = true;

                break;

            case R.id.menu_item_evening_alert:
                mTimePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        // Log.d("111111111111111111111111", selectedHour + ":" + selectedMinute);


                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                        calendar.set(Calendar.MINUTE, selectedMinute);
                        calendar.set(Calendar.SECOND, 0);


                        // هذا هو الذي بايجلس يرسل التنبيهات او يرسل الانتنت الى البروتكاست
                        // بتعبير ادق هذا باينفذ خدمات التنبيه
                        // داخله داله التكرار نحدد ايش النيه(intent) اللي نريدها تتنفذ ومتى تتنفذ وكل كم يعيد تشغيل التنبيه

                        // نحدد المستقبل الذي بانفتحه او بانتعامل معاه
                        Intent intent1 = new Intent(MainActivity.this, TimeReceiver.class);


                        // نحول الـ intent الى PendingIntent
                        @SuppressLint("UnspecifiedImmutableFlag")
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 1,intent1, PendingIntent.FLAG_UPDATE_CURRENT);



                        // هذا هو الذي بايجلس يرسل التنبيهات او يرسل الانتنت الى البروتكاست
                        // بتعبير ادق هذا باينفذ خدمات التنبيه
                        // داخله داله التكرار نحدد ايش النيه(intent) اللي نريدها تتنفذ ومتى تتنفذ وكل كم يعيد تشغيل التنبيه
                        AlarmManager pm = (AlarmManager) MainActivity.this.getSystemService(MainActivity.this.ALARM_SERVICE);
                        pm.cancel(pendingIntent);

                        // حددنا له نوع التنبيه (مش مهم يكون اي شي ، هنا حددنا استيقاظ من النوم ) والوقت حق التنبيه متى،
                        // وبعد كم يعيد التنبيه والانتنت اللي باينفذها
                        pm.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);




                        // نكتب في الملف
                        editor.putBoolean("eveningTime", true);
                        editor.putInt("eveningAlarmHour", selectedHour);
                        editor.putInt("eveningAlarmMinute", selectedMinute);
                        editor.apply();
                        Toast.makeText(getBaseContext(), "تم التعيين", Toast.LENGTH_SHORT).show();
                        invalidateOptionsMenu();
                    }
                }, hour, minute, false);//Yes 24 hour time
                // mTimePicker.setTitle("موعد التذكير بأذكار المساء");
                mTimePicker.show();

                return true;
        }


        return re;
    }

}