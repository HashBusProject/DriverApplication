package com.hashimte.hashbusdriver.ui.auth;

import androidx.annotation.NonNull;
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
import com.hashimte.hashbusdriver.model.DriverData;
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
            authUser.setPassword(binding.txtInputPassword.getText().toString());
            authServicesImp.login(authUser).enqueue(new Callback<DriverData>() {
                @Override
                public void onResponse(Call<DriverData> call, Response<DriverData> response) {
                    Log.e("Code :", authUser.toString() + response.code());
                    if (response.isSuccessful()) {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finishAffinity();
                        Gson gson = new Gson();
                        getSharedPreferences("app_prefs", MODE_PRIVATE)
                                .edit()
                                .putString("driver", gson.toJson(response.body().getDriver()))
                                .putString("bus", gson.toJson(response.body().getBus()))
                                .apply();
                    } else if (response.code() == 400) {
                        Toast.makeText(LoginActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                        binding.loginButton.setEnabled(true);
                    } else {
                        Toast.makeText(LoginActivity.this, "error", Toast.LENGTH_SHORT).show();
                        Log.e("Error", response.errorBody().toString());
                        binding.txtInputUsernameOrEmail.setText(null);
                        binding.txtInputPassword.setText(null);
                        binding.loginButton.setEnabled(true);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<DriverData> call, @NonNull Throwable t) {
                    Log.e("Error: ", t.getMessage());
                    Toast.makeText(LoginActivity.this, "error", Toast.LENGTH_SHORT).show();
                    binding.loginButton.setEnabled(true);
                }
            });

        });
    }
}