package com.hashimte.hashbusdriver.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hashimte.hashbusdriver.LauncherActivity;
import com.hashimte.hashbusdriver.databinding.FragmentProfileBinding;

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