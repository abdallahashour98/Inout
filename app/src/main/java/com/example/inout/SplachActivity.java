package com.example.inout;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.inout.aut.LOGIN_Activity;

import java.util.Locale;


public class SplachActivity extends AppCompatActivity {
SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splach);

        loadLanguage();
        themes();

        new Handler() .postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplachActivity.this ,LOGIN_Activity.class));
                SplachActivity.this.finish();
            }
        },2000);
    }
    private void loadLanguage()
    {
        SharedPreferences get =getSharedPreferences("language", Activity.MODE_PRIVATE);
        String language =get.getString("my_language","");
        Locale l = new Locale(language);
        Locale.setDefault(l);
        Configuration configuration = new Configuration();
        configuration.locale = l;
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
    }
    private void themes()
    {
        boolean isDrak = getSharedPreferences("Dark", MODE_PRIVATE)
                .getBoolean("Dark",true);
        if (isDrak)
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else
        {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}