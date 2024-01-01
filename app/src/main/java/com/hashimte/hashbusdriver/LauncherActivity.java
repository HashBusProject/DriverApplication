package com.hashimte.hashbusdriver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.hashimte.hashbusdriver.ui.auth.LoginActivity;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences appPrefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        if(!appPrefs.contains("driver")){
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}