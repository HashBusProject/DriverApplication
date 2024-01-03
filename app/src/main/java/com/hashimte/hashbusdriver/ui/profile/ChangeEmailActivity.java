package com.hashimte.hashbusdriver.ui.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hashimte.hashbusdriver.R;
import com.hashimte.hashbusdriver.api.ServicesImp;
import com.hashimte.hashbusdriver.databinding.ActivityChangeEmailBinding;
import com.hashimte.hashbusdriver.databinding.ActivityChangePassBinding;
import com.hashimte.hashbusdriver.model.User;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeEmailActivity extends AppCompatActivity {

    private ActivityChangeEmailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangeEmailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Change Email");
        Gson gson = new Gson();
        SharedPreferences appPrefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        User user = gson.fromJson(appPrefs.getString("driver", null), User.class);
        binding.btnChangeEmail.setOnClickListener(v -> {
            user.setEmail(binding.newEmail.getText().toString());
            user.setPassword(binding.emailPass.getText().toString());
            ServicesImp.getInstance().changeEmail(user).enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(ChangeEmailActivity.this, "Email change successfully", Toast.LENGTH_SHORT).show();
                        //TODO change email in shered
                        finish();
                    } else if(response.code() == 400 ) {
                        Toast.makeText(ChangeEmailActivity.this, "Password does not right", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Log.e("Error : ", t.getMessage());
                }
            });
        });
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