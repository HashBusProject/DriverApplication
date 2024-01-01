package com.hashimte.hashbusdriver.ui.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.hashimte.hashbusdriver.MainActivity;
import com.hashimte.hashbusdriver.R;
import com.hashimte.hashbusdriver.api.ServicesImp;
import com.hashimte.hashbusdriver.databinding.ActivityLoginBinding;
import com.hashimte.hashbusdriver.model.Bus;
import com.hashimte.hashbusdriver.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.loginButton.setOnClickListener(v -> {
            binding.loginButton.setEnabled(false);
            ServicesImp authServicesImp = ServicesImp.getInstance();
            User authUser = new User();
            authUser.setUsername(binding.txtInputUsernameOrEmail.getText().toString());
            authUser.setPassword(binding.forgetPassword.getText().toString());
            authServicesImp.login(authUser).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finishAffinity();
                        // TODO, Return a bus with the token here!!!
                        Bus bus = new Bus();
                        bus.setDriver(response.body());
                        bus.setCap(40);
                        bus.setX(31.9943310);
                        bus.setY(35.9193310);
                        bus.setWorking(true);
                        bus.setId(1);
                        Gson gson = new Gson();
                        getSharedPreferences("app_prefs", MODE_PRIVATE)
                                .edit()
                                .putString("driver", gson.toJson(response.body()))
                                .putString("bus", gson.toJson(bus))
                                .apply();

                    } else {
                        Toast.makeText(LoginActivity.this, "error", Toast.LENGTH_SHORT).show();
                        Log.e("Error", response.errorBody().toString());
                        binding.txtInputUsernameOrEmail.setText(null);
                        binding.txtInputPassword.setText(null);
                        binding.loginButton.setEnabled(true);
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.e("Error: ", t.getMessage());
                    Toast.makeText(LoginActivity.this, "error", Toast.LENGTH_SHORT).show();
                    binding.loginButton.setEnabled(true);
                }
            });

        });
    }
}