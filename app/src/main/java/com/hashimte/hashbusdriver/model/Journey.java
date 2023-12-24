package com.hashimte.hashbusdriver.model;

import com.google.gson.annotations.SerializedName;

import java.util.HashSet;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Journey {
    @SerializedName("id")
    private Integer id;
    @SerializedName("name")
    private String name;
    @SerializedName("sourcePoint")
    private Integer sourcePoint;
    @SerializedName("destinationPoint")
    private Integer destinationPoint;
    @SerializedName("stopPoints")
    private List<Integer> stopPoints;
    @SerializedName("journeys")
    private HashSet<Schedule> journeys;
    @SerializedName("price")
    private Double price;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSourcePoint() {
        return sourcePoint;
    }

    public void setSourcePoint(Integer sourcePoint) {
        this.sourcePoint = sourcePoint;
    }

    public Integer getDestinationPoint() {
        return destinationPoint;
    }

    public void setDestinationPoint(Integer destinationPoint) {
        this.destinationPoint = destinationPoint;
    }

    public List<Integer> getStopPoints() {
        return stopPoints;
    }

    public void setStopPoints(List<Integer> stopPoints) {
        this.stopPoints = stopPoints;
    }

    public HashSet<Schedule> getJourneys() {
        return journeys;
    }

    public void setJourneys(HashSet<Schedule> journeys) {
        this.journeys = journeys;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
