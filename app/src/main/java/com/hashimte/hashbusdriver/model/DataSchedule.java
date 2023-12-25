package com.hashimte.hashbusdriver.model;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class DataSchedule {
    @SerializedName("journey")
    private Journey journey;
    @SerializedName("schedule")
    private Schedule schedule;

    public Journey getJourney() {
        return journey;
    }

    public void setJourney(Journey journey) {
        this.journey = journey;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }
}
