package com.devcentre.playmusicapp.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.devcentre.playmusicapp.BuildConfig;
import com.devcentre.playmusicapp.databinding.ActivitySplashScreenBinding;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {
    private final Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivitySplashScreenBinding binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        binding.tvVersion.setText(new StringBuilder().append("Version : ").append(BuildConfig.VERSION_NAME).toString());

        runTimer(1000);
    }

    private void runTimer(int delay) {
        handler.postDelayed(runnable, delay);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            runTimer(3000);

            startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
            finish();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}