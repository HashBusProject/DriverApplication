package com.hashimte.hashbusdriver.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.hashimte.hashbusdriver.R;
import com.hashimte.hashbusdriver.model.DataSchedule;
import com.hashimte.hashbusdriver.model.Schedule;
import com.hashimte.hashbusdriver.ui.journey.JourneyViewActivity;

import androidx.annotation.NonNull;

import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {

    private List<DataSchedule> schedules;
    private Context context;
    private Bundle bundle;

    public ScheduleAdapter(Context context, List<DataSchedule> schedules) {
        this.schedules = schedules;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.search_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final DataSchedule schedule = schedules.get(position);
        holder.startLocation.setText("Name :" + schedule.getJourney().getName());
        holder.endLocation.setText("end :" + schedule.getJourney().getName());
        holder.waitTime.setText(schedule.getSchedule().getTime().toString());
        // DO NOT TOUCH IT
        SharedPreferences journeyPrefs = context.getSharedPreferences("journey_prefs", Context.MODE_PRIVATE);
        DataSchedule dataSchedule = new Gson().fromJson(journeyPrefs.getString("journeyStarted", null), DataSchedule.class);
        if (journeyPrefs.getBoolean("started", false)
                && !dataSchedule.getSchedule().getId().equals(schedule.getSchedule().getId())) {
            holder.itemView.setEnabled(false);
        } else {
            Log.w("DataSchedule :", schedule.toString());
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, JourneyViewActivity.class);
                bundle = new Bundle();
                bundle.putString("dataSchedule",
                        new Gson().toJson(schedule));
                intent.putExtras(bundle);
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return schedules.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView startLocation, endLocation, waitMinTime, waitTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            startLocation = itemView.findViewById(R.id.startLocation);
            endLocation = itemView.findViewById(R.id.endLocation);
            waitTime = itemView.findViewById(R.id.waitTime);
        }
    }
}
