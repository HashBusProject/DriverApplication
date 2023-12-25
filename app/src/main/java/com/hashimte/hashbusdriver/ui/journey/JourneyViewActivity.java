package com.hashimte.hashbusdriver.ui.journey;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.maps.model.LatLng;
import com.hashimte.hashbusdriver.R;
import com.hashimte.hashbusdriver.api.ServicesImp;
import com.hashimte.hashbusdriver.databinding.ActivityJourneyViewBinding;
import com.hashimte.hashbusdriver.map.DirectionsTask;
import com.hashimte.hashbusdriver.model.DataSchedule;
import com.hashimte.hashbusdriver.model.Point;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JourneyViewActivity extends AppCompatActivity {
    private ActivityJourneyViewBinding binding;
    private Gson gson;
    private DataSchedule dataSchedule;
    private SharedPreferences journeyPrefs;
    private static List<Point> points;
    private int position;
    private Bundle bundle;
    private PointsAdapter adapter;
    private Location busLocation;
    private Point startPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityJourneyViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Name");
        locationUpdate();
        gson = new Gson();
        journeyPrefs = getSharedPreferences("journey_prefs", MODE_PRIVATE);
        bundle = getIntent().getExtras();
        binding.rvPoints.setHasFixedSize(true);
        binding.rvPoints.setLayoutManager(new LinearLayoutManager(JourneyViewActivity.this, RecyclerView.HORIZONTAL, false));
        position = journeyPrefs.getInt("position", 0);
        dataSchedule = gson.fromJson(bundle.getString("dataSchedule"), DataSchedule.class);
        if (points == null || points.isEmpty()) {
            ServicesImp.getInstance().getAllPointsForJourney(dataSchedule.getJourney().getId()).enqueue(new Callback<List<Point>>() {
                @Override
                public void onResponse(@NonNull Call<List<Point>> call, @NonNull Response<List<Point>> response) {
                    if (response.isSuccessful()) {
                        points = response.body();
                        adapter = new PointsAdapter(points);
                        binding.rvPoints.setAdapter(adapter);
                        startPoint = points.get(0);
                    } else {
//                    TODO, Handle it here!!
                    }
                }

                @Override
                public void onFailure(Call<List<Point>> call, Throwable t) {
//                    TODO, Handle it here!!
                }
            });
        }
        if (journeyPrefs.getBoolean("started", false)) {
            setContentAsStarted();
        } else {
            setContentAsNotStarted();
        }
    }

    private void setContentAsNotStarted() {
        binding.txtTime.setText(getString(R.string.start_time, dataSchedule.getSchedule().getTime()));
        binding.btnGoTo.setOnClickListener(v -> goToGoogleMaps(0, 0, startPoint.getX(), startPoint.getY()));
        binding.btnStart.setOnClickListener(v -> {
            if (!LocalTime.now().isAfter(LocalTime.parse(dataSchedule.getSchedule().getTime()))) {
                Snackbar.make(v, "It's not time for the trip yet.", BaseTransientBottomBar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return;
            }
            Log.i("ID ::", dataSchedule.getSchedule().getId().toString());
            ServicesImp.getInstance().updateNextPointIndexByScheduleId(dataSchedule.getSchedule().getId(), 0).enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                    if (response.isSuccessful()) {
                        journeyPrefs.edit()
                                .putBoolean("started", true)
                                .putString("journeyStarted", bundle.getString("dataSchedule", null))
                                .putInt("position", 1)
                                .putBoolean("going", true)
                                .apply();
                        setContentAsStarted();
                    } else {
                        // TODO, Handle This
                        Log.e("onResponse :", response.body().toString());
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    // TODO, Handle This
                    Log.e("onResponse :", t.getMessage());
                }
            });
        });
    }

    private void setContentAsStarted() {
        position = journeyPrefs.getInt("position", 1);
        boolean going = journeyPrefs.getBoolean("going", false);
        DataSchedule journeyStarted = gson.fromJson(
                journeyPrefs.getString("journeyStarted", null),
                DataSchedule.class
        );
        Point nextPoint = points.get(position);
        binding.imgTime.setImageResource(R.drawable.go);
        if (going) {
            binding.btnStart.setText(getString(R.string.arrive_to, nextPoint.getPointName()));
            setEstimatedTimeToNextPoint(new LatLng(nextPoint.getX(), nextPoint.getY()));
            binding.btnGoTo.setOnClickListener(v -> goToGoogleMaps(0, 0, nextPoint.getX(), nextPoint.getY()));
            binding.btnStart.setOnClickListener(v -> {
                increasePosition(journeyStarted.getSchedule().getId(), position);
            });
        } else {
            binding.btnGoTo.setOnClickListener(v -> goToGoogleMaps(0, 0, nextPoint.getX(), nextPoint.getY()));
            binding.btnStart.setText(R.string.go_to_the_next_point);
            binding.btnStart.setOnClickListener(v -> {
                journeyPrefs.edit()
                        .putBoolean("going", true)
                        .apply();
                setContentAsStarted();
            });
        }
        if (position == points.size() - 1) {
            binding.btnStart.setText(R.string.finish_the_trip);
            binding.btnStart.setOnClickListener((v) -> {
                endTrip(journeyStarted.getSchedule().getId());
            });
        }
    }

    private void increasePosition(int scheduleID, int previousValue) {
        ServicesImp.getInstance().updateNextPointIndexByScheduleId(scheduleID, previousValue).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                if (response.isSuccessful() && Boolean.TRUE.equals(response.body())) {
                    journeyPrefs.edit()
                            .putBoolean("going", false)
                            .putInt("position", position + 1)
                            .apply();
                    setContentAsStarted();
                } else {
                    Log.e("Value :", response.body().toString());
                    // TODO, Handle This
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.e("onFailure :", t.getMessage());
                // TODO, Handle This
            }
        });
    }

    private void setEstimatedTimeToNextPoint(LatLng latLng) {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        busLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        new DirectionsTask(new LatLng(busLocation.getLatitude(), busLocation.getLongitude()), latLng) {
            @Override
            protected void onPostExecute(String time) {
                super.onPostExecute(time);
                binding.txtTime.setText(
                        getString(
                                R.string.time_to_arrive_into,
                                points.get(position).getPointName(),
                                time
                        )
                );
            }
        }.execute();
    }


    private void goToGoogleMaps(double lat1, double lng1, double lat2, double lng2) {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
        }
        busLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Uri uri = Uri.parse("https://www.google.com/maps/dir/" +
                busLocation.getLatitude() + "," + busLocation.getLongitude() + "/" + lat2 + "," + lng2);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setPackage("com.google.android.apps.maps");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);


    }

    private void locationUpdate() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, new LocationListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onLocationChanged(@NonNull Location location) {
                busLocation = location;
            }
        });
    }

    private void endTrip(Integer id) {
        ServicesImp.getInstance().setScheduleAsFinished(id).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && Boolean.TRUE.equals(response.body())) {
                    journeyPrefs.edit().clear().apply();
                    points = null;
                    finish();
                } else {
                    Log.e("onResponse :", response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.e("onFailure :", t.getMessage());
            }
        });
//        finish();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!journeyPrefs.getBoolean("started", false)) {
            journeyPrefs.edit().clear().apply();
            points = null;
        }
    }
}