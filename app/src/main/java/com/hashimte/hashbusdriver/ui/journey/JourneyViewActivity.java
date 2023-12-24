package com.hashimte.hashbusdriver.ui.journey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.hashimte.hashbusdriver.R;
import com.hashimte.hashbusdriver.api.ServicesImp;
import com.hashimte.hashbusdriver.databinding.ActivityJourneyViewBinding;
import com.hashimte.hashbusdriver.model.Bus;
import com.hashimte.hashbusdriver.model.DataSchedule;
import com.hashimte.hashbusdriver.model.Point;
import com.hashimte.hashbusdriver.ui.home.ScheduleAdapter;

import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import androidx.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JourneyViewActivity extends AppCompatActivity {
    private ActivityJourneyViewBinding binding;
    private Gson gson;
    private DataSchedule dataSchedule;
    private SharedPreferences journeyPrefs;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityJourneyViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Name");
        gson = new Gson();
        journeyPrefs = getSharedPreferences("journey_prefs", MODE_PRIVATE);
        bundle = getIntent().getExtras();
        binding.rvPoints.setHasFixedSize(true);
        binding.rvPoints.setLayoutManager(new LinearLayoutManager(JourneyViewActivity.this, RecyclerView.HORIZONTAL, false));
        if (journeyPrefs.getBoolean("started", false)) {
            setContentAsStarted();
        } else {
            setContentAsNotStarted();
        }
    }

    private void setContentAsNotStarted() {
        dataSchedule = gson.fromJson(bundle.getString("dataSchedule"), DataSchedule.class);
        ServicesImp.getInstance().getAllPointsForJourney(dataSchedule.getJourney().getId()).enqueue(new Callback<List<Point>>() {
            @Override
            public void onResponse(@NonNull Call<List<Point>> call, Response<List<Point>> response) {
                if (response.isSuccessful()) {
                    binding.rvPoints.setAdapter(new PointsAdapter(response.body()));
                } else {
//                    TODO, Handle it here!!
                }
            }

            @Override
            public void onFailure(Call<List<Point>> call, Throwable t) {
//                    TODO, Handle it here!!
            }
        });
        binding.txtTime.setText(getString(R.string.start_time, dataSchedule.getSchedule().getTime()));
        binding.btnStart.setOnClickListener(v -> {
            if(LocalTime.now().compareTo(LocalTime.parse(dataSchedule.getSchedule().getTime())) > 0){
                Snackbar.make(v, "", BaseTransientBottomBar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return;
            }
            setContentAsStarted();
        });
    }

    private void setContentAsStarted() {

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}