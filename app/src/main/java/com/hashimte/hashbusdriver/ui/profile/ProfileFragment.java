package com.hashimte.hashbusdriver.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.hashimte.hashbusdriver.LauncherActivity;
import com.hashimte.hashbusdriver.databinding.FragmentProfileBinding;
import com.hashimte.hashbusdriver.model.Bus;
import com.hashimte.hashbusdriver.model.DriverData;
import com.hashimte.hashbusdriver.model.User;

import java.sql.Driver;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Gson gson = new Gson();
        SharedPreferences appPrefs = getContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        DriverData data = new DriverData(
                gson.fromJson(appPrefs.getString("driver", null), User.class),
                gson.fromJson(appPrefs.getString("bus", null), Bus.class)
        );
        binding.txtUsername.setText(data.getDriver().getUsername());
        binding.txtBusNumber.setText("" + data.getBus().getId());
        binding.txtEmail.setText(data.getDriver().getEmail());
        binding.logout.setOnClickListener(view1 -> {
            getActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE).edit()
                    .remove("bus")
                    .remove("driver")
                    .clear()
                    .apply();
            Intent intent = new Intent(getContext(), LauncherActivity.class);
            startActivity(intent);
            getActivity().finish();
        });
        binding.changepass.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), ChangePassActivity.class);
            startActivity(intent);
        });

        binding.changeemail.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), ChangeEmailActivity.class);
            startActivity(intent);
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}