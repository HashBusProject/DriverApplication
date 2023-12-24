package com.hashimte.hashbusdriver.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.hashimte.hashbusdriver.api.ServicesImp;
import com.hashimte.hashbusdriver.databinding.FragmentHomeBinding;
import com.hashimte.hashbusdriver.model.Bus;
import com.hashimte.hashbusdriver.model.DataSchedule;
import com.hashimte.hashbusdriver.model.Schedule;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private Gson gson;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        gson = new Gson();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recycleView.setHasFixedSize(true);
        binding.recycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        Bus bus = gson.fromJson(getContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                .getString("bus", null), Bus.class);
        ServicesImp.getInstance()
                .getDataSchedulesByBusId(bus.getId())
                .enqueue(new Callback<List<DataSchedule>>() {
                    @Override
                    public void onResponse(Call<List<DataSchedule>> call, Response<List<DataSchedule>> response) {
                        if (response.isSuccessful()) {
                            Log.e("Size Data :", "" + response.body().size());
                            binding.recycleView.setAdapter(new ScheduleAdapter(getContext(), response.body()));
                        }
                    }
                    @Override
                    public void onFailure(Call<List<DataSchedule>> call, Throwable t) {

                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}