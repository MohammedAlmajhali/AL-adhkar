package com.mohammedalmajhali.aladhkar;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


public class TimeReceiver extends BroadcastReceiver {

    static final String CHANNEL_ID = "ALAdhkar_channel1";
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        //****************************** انشاء الاشعار************************



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "أذكاري", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("وقت الأذكار");
            NotificationManager nm = context.getSystemService(NotificationManager.class);
            nm.createNotificationChannel(channel);
        }

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        builder.setContentTitle("حان وقت قراءة الأذكار");
        builder.setContentText("أذكاري");
        builder.setSmallIcon(R.drawable.app_icon);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText("(أَلاَ بِذِكْرِ اللّهِ تَطْمَئِنُّ الْقُلُوب)"));
        builder.setSound(alarmSound);


        Intent notifyIntent = new Intent(context , MainActivity.class);
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //to be able to launch your activity from the notification
        builder.setContentIntent(pendingIntent);

//        Notification notificationCompat = builder.build();

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(3, builder.build());




    }
}