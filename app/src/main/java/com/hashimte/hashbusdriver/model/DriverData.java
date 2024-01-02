package com.hashimte.hashbusdriver.model;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class DriverData {
    @SerializedName("driver")
    private User driver;
    @SerializedName("bus")
    private Bus bus;

    public DriverData(User driver, Bus bus) {
        this.driver = driver;
        this.bus = bus;
    }

    public User getDriver() {
        return driver;
    }

    public Bus getBus() {
        return bus;
    }
}
