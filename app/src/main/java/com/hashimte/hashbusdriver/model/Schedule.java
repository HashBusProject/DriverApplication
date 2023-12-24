package com.hashimte.hashbusdriver.model;

import com.google.gson.annotations.SerializedName;

import java.sql.Date;
import java.sql.Time;

import lombok.Data;

public class Schedule {
    @SerializedName("id")
    private Integer id;
    @SerializedName("journey")
    private Integer journey; // 1 - 9
    @SerializedName("bus")
    private Integer bus;   // 1  2  3  4  5
    @SerializedName("time")
    private String time; // 10 11 12 13 18
    @SerializedName("nextPoint")
    private Integer nextPoint;
    @SerializedName("passengersNumber")
    private Integer passengersNumber;
    @SerializedName("date")
    private Date date;
    @SerializedName("finished")
    private Boolean finished;

    public Schedule(){}

    public Schedule(Schedule schedule){
        this.setId(schedule.id);
        this.setJourney(schedule.journey);
        this.setPassengersNumber(schedule.passengersNumber);
        this.setBus(schedule.bus);
        this.setTime(schedule.time);
        this.setNextPoint(schedule.nextPoint);
        this.setFinished(schedule.finished);
        this.setDate(schedule.date);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public Integer getJourney() {
        return journey;
    }

    public void setJourney(Integer journey) {
        this.journey = journey;
    }

    public Integer getBus() {
        return bus;
    }

    public void setBus(Integer bus) {
        this.bus = bus;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getNextPoint() {
        return nextPoint;
    }

    public void setNextPoint(Integer nextPoint) {
        this.nextPoint = nextPoint;
    }

    public Integer getPassengersNumber() {
        return passengersNumber;
    }

    public void setPassengersNumber(Integer passengersNumber) {
        this.passengersNumber = passengersNumber;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }
}
