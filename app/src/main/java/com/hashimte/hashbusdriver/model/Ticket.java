package com.hashimte.hashbusdriver.model;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Ticket {
    @SerializedName("id")
    private Integer id;
    @SerializedName("journey")
    private Journey journey;
    @SerializedName("user")
    private User user;
}
