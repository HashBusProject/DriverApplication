package com.hashimte.hashbusdriver;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.gson.Gson;
import com.hashimte.hashbusdriver.databinding.ActivityMainBinding;
import com.hashimte.hashbusdriver.model.Bus;
import com.hashimte.hashbusdriver.model.User;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        User user = new User();
        user.setRole(2);
        user.setUserID(2);
        user.setName("Alice Smith");
        user.setEmail("driver1@example.com");
        user.setUsername("driver1");
        Bus bus = new Bus();
        bus.setDriver(user);
        bus.setCap(40);
        bus.setX(31.9943310);
        bus.setY(35.9193310);
        bus.setWorking(true);
        bus.setId(1);
        Gson gson = new Gson();
        getSharedPreferences("app_prefs", MODE_PRIVATE)
                .edit()
                .putString("driver", gson.toJson(user))
                .putString("bus", gson.toJson(bus))
                .apply();
    }

}