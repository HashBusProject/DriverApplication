package com.hashimte.hashbusdriver.ui.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.gson.Gson;

import com.hashimte.hashbusdriver.api.ServicesImp;
import com.hashimte.hashbusdriver.databinding.ActivityChangePassBinding;
import com.hashimte.hashbusdriver.model.ChangePassword;
import com.hashimte.hashbusdriver.model.User;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePassActivity extends AppCompatActivity {
    private ActivityChangePassBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePassBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Change Password");
        Gson gson = new Gson();
        SharedPreferences appPrefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        User user = gson.fromJson(appPrefs.getString("driver", null), User.class);
        binding.btnChangePassword.setOnClickListener(v -> {
                    if (!(binding.retypePass.getText().toString().equals(binding.newPass.getText().toString()))) {
                        Toast.makeText(this, "New password does not match with repeated password", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    user.setPassword(binding.currentPass.getText().toString());
                    if(binding.newPass.getText().toString().equals(user.getPassword())){
                        Toast.makeText(this, "New password does match with old password", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    ChangePassword changePassword = new ChangePassword(
                            user, binding.newPass.getText().toString());
            Log.i("user" , "" + changePassword.toString()) ;

            ServicesImp.getInstance().changePassword(changePassword).enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            if(response.isSuccessful()){
                                Toast.makeText(ChangePassActivity.this, "Password change successfully", Toast.LENGTH_SHORT).show();
                                user.setPassword(null);
                                finish();
                            }else{
                                    // TODO
                                Log.i("saif" , "" + response.code()) ;
                                Log.i("saif" , "" + response.headers().toString()) ;

                            }
                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {
                                Log.i("no req" , "this") ;
                        }
                    });
                }
        );

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