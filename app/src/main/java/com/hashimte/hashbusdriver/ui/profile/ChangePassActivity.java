package com.hashimte.hashbusdriver.ui.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import com.hashimte.hashbusdriver.R;
import com.hashimte.hashbusdriver.databinding.ActivityChangePassBinding;

import java.util.Objects;

public class ChangePassActivity extends AppCompatActivity {
    private ActivityChangePassBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePassBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Change Password");

    }
    @Override
    public boolean onOptionsItemSelected(@lombok.NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}