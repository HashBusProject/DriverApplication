package com.hashimte.hashbusdriver.model;

import com.google.gson.annotations.SerializedName;

import java.util.HashSet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bus {
    @SerializedName("driver")
    private User driver;
    @SerializedName("id")
    private Integer id;
    @SerializedName("cap")
    private Integer cap;
    @SerializedName("schedules")
    private HashSet<Schedule> schedules;
    @SerializedName("isWorking")
    private Boolean isWorking;
    @SerializedName("x")
    private Double x;
    @SerializedName("y")
    private Double y;

    public User getDriver() {
        return driver;
    }

    public void setDriver(User driver) {
        this.driver = driver;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCap() {
        return cap;
    }

    public void setCap(Integer cap) {
        this.cap = cap;
    }

    public HashSet<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(HashSet<Schedule> schedules) {
        this.schedules = schedules;
    }

    public Boolean getWorking() {
        return isWorking;
    }

    public void setWorking(Boolean working) {
        isWorking = working;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }
}
