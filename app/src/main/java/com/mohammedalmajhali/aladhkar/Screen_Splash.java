package com.mohammedalmajhali.aladhkar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Screen_Splash extends AppCompatActivity {

    Handler handler = new Handler();


    TextView app_name, version;
    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_screen_splash);

        app_name =  findViewById(R.id.splash_app_name);
        app_name.setText(R.string.app_name);
        version =  findViewById(R.id.splash_version);
        version.setText(BuildConfig.VERSION_NAME);
        logo = findViewById(R.id.splash_logo);





        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                logo.animate().translationY(-2000).setDuration(500).setStartDelay(200);
                app_name.animate().translationY(2000).setDuration(500).setStartDelay(200);
                version.animate().translationY(2000).setDuration(500).setStartDelay(150);

            }
        };
        Runnable runnable2 = new Runnable() {
            @Override
            public void run() {

                run_activity();
            }
        };

        handler.postDelayed(runnable,800);
        handler.postDelayed(runnable2,1500);

    }

    public void run_activity(){

        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}