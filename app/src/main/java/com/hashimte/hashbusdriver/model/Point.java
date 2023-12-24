package com.hashimte.hashbusdriver.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashSet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
//It's Working Don't Touch
public class Point {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("pointName")
    @Expose
    private String pointName;
    @Expose
    @SerializedName("x")
    private Double x;
    @Expose
    @SerializedName("y")
    private Double y;
    @Expose
    @SerializedName("journeysID")
    private HashSet<Integer> journeysID;

    public Point(Point point) {
        this.pointName = point.getPointName();
        this.id = point.id;
        this.journeysID = point.journeysID;
        this.x = point.x;
        this.y = point.y;
    }
    public Point(){}

    public String getPointName() {
        return pointName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPointName(String pointName) {
        this.pointName = pointName;
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

    public HashSet<Integer> getJourneysID() {
        return journeysID;
    }

    public void setJourneysID(HashSet<Integer> journeysID) {
        this.journeysID = journeysID;
    }
}