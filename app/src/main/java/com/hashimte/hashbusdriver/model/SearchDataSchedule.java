package com.hashimte.hashbusdriver.model;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchDataSchedule {
    @SerializedName("journey")
    private Journey journey;
    @SerializedName("schedule")
    private Schedule schedule;
    @SerializedName("bus")
    private Bus bus;

    public Bus getBus() {
        return bus;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public Journey getJourney() {
        return journey;
    }
}
