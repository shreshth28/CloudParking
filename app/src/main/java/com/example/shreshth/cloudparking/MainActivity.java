package com.example.shreshth.cloudparking;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private final int SPLASH_SCREEN_DISPLAY_LENGTH = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent boardingIntent=new Intent(MainActivity.this,BoardingActivity.class);
                Intent loginIntent = new Intent(MainActivity.this,DashboardActivity.class);
                Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                        .getBoolean("isFirstRun", true);
                if (isFirstRun) {
                    //show start activity
                    startActivity(boardingIntent);
                    MainActivity.this.finish();
                }
                else
                {
                    startActivity(loginIntent);
                    MainActivity.this.finish();

                }
                getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                        .putBoolean("isFirstRun", false).commit();


            }
        },SPLASH_SCREEN_DISPLAY_LENGTH);
    }
}
